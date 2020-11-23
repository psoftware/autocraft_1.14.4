package com.theincgi.autocrafter.tiles;

import com.theincgi.autocrafter.AutoCrafter;
import com.theincgi.autocrafter.Recipe;
import com.theincgi.autocrafter.Utils;
import com.theincgi.autocrafter.containers.ContainerAutoCrafter;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class TileAutoCrafter extends TileEntity implements ITickableTileEntity, ISidedInventory, INamedContainerProvider {

    public static final int OUTPUT_SLOT = 9;
    public static final int TARGET_SLOT = 10;
    private static final int[] INPUT_SLOTS = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8};
    private static final int[] OUTPUT_SLOTS = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
    NonNullList<ItemStack> inventory, prevInventory;
    private Recipe recipe, prevRecipe;
    private ItemStack crafts, prevCrafts;
    private List recipes, prevRecipes;
    private int currentRecipeIndex, prevRecipeIndex;
    private final LazyOptional<ItemStackHandlerAutoCrafter> holder = LazyOptional.of(() -> new ItemStackHandlerAutoCrafter(this));

    public TileAutoCrafter()
    {
        super(AutoCrafter.TILE_AUTOCRAFTER.get());
        this.inventory = NonNullList.withSize(getSizeInventory(), ItemStack.EMPTY);
        this.prevInventory = NonNullList.withSize(getSizeInventory(), ItemStack.EMPTY);
        this.recipe = new Recipe();
        this.prevRecipe = new Recipe();
        this.crafts = ItemStack.EMPTY;
        this.prevCrafts = ItemStack.EMPTY;
        this.currentRecipeIndex = 0;
        this.prevRecipeIndex = 0;
    }

    public boolean inventoryChanged(){
        for (int i = 0; i < this.inventory.size(); i++)
        {
            ItemStack left = this.inventory.get(i);
            ItemStack right = this.prevInventory.get(i);
            if (!ItemStack.areItemStacksEqual(left, right))
            {
                // copy contents, not the var itself
                for (int j = 0; j < this.inventory.size(); j++){
                    this.prevInventory.set(j, this.inventory.get(j).copy());
                }
                return true;
            }
        }

        if (!recipe.equals(prevRecipe))
        {
            prevRecipe = Recipe.read(recipe.serializeNBT());
            return true;
        }

        if (!ItemStack.areItemStacksEqual(crafts, prevCrafts))
        {
            prevCrafts = crafts.copy();
            return true;
        }

        if (currentRecipeIndex != prevRecipeIndex)
        {
            // to have only value copied, not the var ref
            prevRecipeIndex = (currentRecipeIndex+1)-1;
            return true;
        }

        return false;
    }

    public ItemStack getRecipeResult()
    {
        return recipe.getOutput();
    }

    @Nonnull
    @Override
    public CompoundNBT write(CompoundNBT compound)
    {
        super.write(compound);
        compound.put("inventory", ItemStackHelper.saveAllItems(new CompoundNBT(), this.inventory));
        compound.put("recipe", this.recipe.serializeNBT());
        compound.put("crafts", this.crafts.serializeNBT());
        compound.putInt("currentRecipeIndex", this.currentRecipeIndex);
        return compound;
    }

    @Override
    public void read(BlockState state, CompoundNBT compound)
    {
        super.read(state, compound);

        if (compound.contains("inventory"))
        {
            ItemStackHelper.loadAllItems(compound.getCompound("inventory"), this.inventory);
        }

        if (compound.contains("recipe"))
        {
            this.recipe = Recipe.read(compound.getList("recipe", 10));
        }

        if (compound.contains("crafts"))
        {
            this.crafts = ItemStack.read(compound.getCompound("crafts"));
        }

        if (compound.contains("currentRecipeIndex"))
        {
            this.currentRecipeIndex = compound.getInt("currentRecipeIndex");
        }
    }

    @Override
    public CompoundNBT getUpdateTag()
    {
        return this.write(new CompoundNBT());
    }

    // we can sync a TileEntity from the server to all tracking clients by calling world.notifyBlockUpdate

    // when that happens, this method is called on the server to generate a packet to send to the client
    // if you have lots of data, it's a good idea to keep track of which data has changed since the last time
    // this TE was synced, and then only send the changed data;
    // this reduces the amount of packets sent, which is good
    // we only have one value to sync so we'll just write everything into the NBT again
    @Override
    public SUpdateTileEntityPacket getUpdatePacket()
    {
        CompoundNBT nbt = new CompoundNBT();
        this.write(nbt);
        // the number here is generally ignored for non-vanilla TileEntities, 0 is safest
        return new SUpdateTileEntityPacket(this.getPos(), 0, nbt);
    }

    // this method gets called on the client when it receives the packet that was sent in the previous method
    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket packet)
    {
        this.read(this.getBlockState(), packet.getNbtCompound());
    }

    @Override
    public int getSizeInventory()
    {
        return 11;
    }

    @Override
    public boolean isEmpty()
    {
        for (ItemStack itemStack : this.inventory)
        {
            if (!itemStack.isEmpty())
            {
                return false;
            }
        }

        return true;
    }

    @Nonnull
    @Override
    public ItemStack getStackInSlot(int index)
    {
        return index >= 0 && index < this.getSizeInventory() ? this.inventory.get(index) : ItemStack.EMPTY;
    }

    @Nonnull
    @Override
    public ItemStack decrStackSize(int index, int count)
    {
        ItemStack s = ItemStackHelper.getAndSplit(this.inventory, index, count);
        if (this.getStackInSlot(index).getCount() == 0)
        {
            this.setInventorySlotContents(index, ItemStack.EMPTY);
        }
        return s;
    }


    public ItemStack SIMULATEdecrStackSize(int index, int count)
    {
        ItemStack temp = getStackInSlot(index).copy();
        return temp.split(count);
    }

    @Nonnull
    @Override
    public ItemStack removeStackFromSlot(int index)
    {
        return ItemStackHelper.getAndRemove(inventory, index);
    }

    @Override
    public void setInventorySlotContents(int index, @Nonnull ItemStack stack)
    {
        this.inventory.set(index, stack);
        if (stack.getCount() > this.getInventoryStackLimit())
        {
            stack.setCount(this.getInventoryStackLimit());
        }
        this.markDirty();
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 64;
    }

    @Override
    public boolean isUsableByPlayer(@Nonnull PlayerEntity player)
    {
        return true;
    }

    @Override
    public void openInventory(PlayerEntity player)
    {
    }

    @Override
    public void closeInventory(PlayerEntity player)
    {
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack)
    {
        return true;
    }

    @Override
    public void clear()
    {
        for (int i = 0; i < inventory.size(); i++)
        {
            inventory.set(i, ItemStack.EMPTY);
        }
        markDirty();
    }

    @Nonnull
    @Override
    public ITextComponent getDisplayName()
    {
        return new TranslationTextComponent("tile.autocrafter.autocrafter.name");
    }

    @Nonnull
    @Override
    public int[] getSlotsForFace(Direction side)
    {
        if (side.equals(Direction.DOWN))
        {
            return OUTPUT_SLOTS;
        }
        return INPUT_SLOTS;
    }

    public boolean isSlotAllowed(int index, ItemStack itemStack)
    {
        return index < 9 && this.recipe.stackForSlotFitsThisRecipe(index, itemStack);
    }

    @Override
    public boolean canInsertItem(int index, @Nonnull ItemStack itemStackIn, Direction direction)
    {
        //System.out.printf("canInsertItem: %d %s %s\n",index, itemStackIn.getItem().getRegistryName().toString(), isSlotAllowed(index, itemStackIn) && nextHasSameOrMore(index, itemStackIn));
        return isSlotAllowed(index, itemStackIn);
    }

    @Override
    public boolean canExtractItem(int index, @Nonnull ItemStack stack, @Nonnull Direction direction)
    {
        return index == OUTPUT_SLOT || (index < 9 && !recipe.stackForSlotFitsThisRecipe(index, stack));
    }

    public void setRecipe(IRecipe recipe)
    {
        this.recipe.setRecipe(recipe);
        this.markDirty();
    }

    public void setRecipe(ListNBT recipeTag)
    {
        this.recipe = Recipe.read(recipeTag);
        this.markDirty();
    }

    public void updateRecipes(ItemStack crafts, int index)
    {
        this.crafts = crafts;
        if (this.world == null)
        {
            return;
        }
        this.recipes = Utils.getValid(world, crafts);
        this.currentRecipeIndex = index % Math.max(1, this.recipes.size());
        if (this.recipes.size() > 0)
        {
            this.setRecipe((IRecipe) this.recipes.get(this.currentRecipeIndex));
        }
        else
        {
            this.recipe.clearRecipe();
        }
        this.markDirty();
    }

    public Recipe getRecipe()
    {
        return this.recipe;
    }

    public ItemStack getCrafts()
    {
        return this.crafts;
    }

    public void nextRecipe()
    {
        if (this.recipes == null)
        {
            this.updateRecipes(this.getCrafts(), this.currentRecipeIndex);
        }

        if (this.recipes.size() != 0)
        {
            ++this.currentRecipeIndex;
            this.currentRecipeIndex %= this.recipes.size();
            this.setRecipe((IRecipe) this.recipes.get(this.currentRecipeIndex));
        }
    }

    public void prevRecipe()
    {
        if (this.recipes == null)
        {
            this.updateRecipes(this.getCrafts(), this.currentRecipeIndex);
        }

        if (this.recipes.size() != 0)
        {
            --this.currentRecipeIndex;
            if (this.currentRecipeIndex < 0)
            {
                this.currentRecipeIndex = this.recipes.size() - 1;
            }

            this.setRecipe((IRecipe) this.recipes.get(this.currentRecipeIndex));
        }
    }

    public int getCurrentRecipeIndex()
    {
        return this.currentRecipeIndex;
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> capability, Direction facing)
    {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
        {
            return holder.cast();
        }

        return super.getCapability(capability, facing);
    }


    public boolean gridFitsRecipe()
    {
        for (int slot = 0; slot < 9; ++slot)
        {
            if (!this.recipe.stackForSlotFitsThisRecipe(slot, inventory.get(slot)))
            {
                return false;
            }
        }
        return true;
    }


    @Override
    public void tick()
    {
        if (this.world == null
            || this.world.isRemote
            || this.recipe.getOutput().isEmpty()
            || !inventoryChanged()
        ){
            return;
        }

        int amountOfItemsInOutputSlot = getStackInSlot(OUTPUT_SLOT).getCount();
        int numberOfItemsPerCraft = this.recipe.getOutput().getCount();
        if (amountOfItemsInOutputSlot + numberOfItemsPerCraft > this.recipe.getOutput().getMaxStackSize())
        {
            return;
        }
        if (!getStackInSlot(OUTPUT_SLOT).isEmpty() && !Recipe.itemStacksMatch(getStackInSlot(OUTPUT_SLOT), this.recipe.getOutput()))
        {
            return;
        }

        this.distributeItems();

        if (!gridFitsRecipe()) return;

        // grid does fit recipe, let's craft!
        NonNullList<ItemStack> leftovers = this.recipe.getLeftovers(this.inventory, 0, 9);

        // decrease crafting grid
        for (int i = 0; i < 9; ++i)
        {
            (this.inventory.get(i)).shrink(1);
            if ((this.inventory.get(i)).getCount() <= 0)
            {
                this.setInventorySlotContents(i, ItemStack.EMPTY);
            }

            if (!(leftovers.get(i)).isEmpty())
            {
                if ((this.inventory.get(i)).isEmpty())
                {
                    this.setInventorySlotContents(i, leftovers.get(i));
                }
                else
                {
                    InventoryHelper.spawnItemStack(this.world, this.pos.getX(), this.pos.getY(), this.pos.getZ(), leftovers.get(i));
                }
            }
        }

        // provide output
        if (getStackInSlot(OUTPUT_SLOT).isEmpty())
        {
            setInventorySlotContents(OUTPUT_SLOT, this.recipe.getOutput());
        }
        else
        {
            getStackInSlot(OUTPUT_SLOT).grow(this.recipe.getOutput().getCount());
        }

        this.markDirty();
    }

    private void distributeItems()
    {
        for (int i = 0; i < 9; ++i)
        {
            ItemStack current = getStackInSlot(i);
            if (current.getCount() >= 2)
            {
                int nextMatch = this.nextMatch(i);
                if (nextMatch >= 0)
                {
                    if (getStackInSlot(nextMatch).isEmpty())
                    {
                        setInventorySlotContents(nextMatch, current.split(1));
                    }
                    else if (current.getCount() > getStackInSlot(nextMatch).getCount())
                    {
                        current.shrink(1);
                        getStackInSlot(nextMatch).grow(1);
                    }
                }
            }
        }
        this.markDirty();
    }

    private int nextMatch(int j)
    {
        ItemStack is = getStackInSlot(j);

        for (int i = 0; i < 9; ++i)
        {
            int c = (i + j + 1) % 9;
            if (Recipe.itemStacksMatch(is, getStackInSlot(c)) || getStackInSlot(c).isEmpty() && this.recipe.stackForSlotFitsThisRecipe(c, is))
            {
                return c == j ? -1 : c;
            }
        }

        return -1;
    }

    public void setCurrentRecipeIndex(int integer)
    {
        this.currentRecipeIndex = integer;
    }

    public void setCrafts(ItemStack itemStack)
    {
        this.crafts = itemStack;
    }

    @Nullable
    @Override
    public Container createMenu(int windowId, PlayerInventory playerInventory, PlayerEntity playerEntity)
    {
        return new ContainerAutoCrafter(windowId, playerInventory, this);
    }
}
