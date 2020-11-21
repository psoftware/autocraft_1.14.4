package com.theincgi.autocrafter.tileEntity;

import com.theincgi.autocrafter.AutoCrafter;
import com.theincgi.autocrafter.Recipe;
import com.theincgi.autocrafter.Utils;

import java.util.List;


import com.theincgi.autocrafter.container.ContainerAutoCrafter;
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
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileAutoCrafter extends TileEntity implements ITickableTileEntity, ISidedInventory, INamedContainerProvider {

   public static final int OUTPUT_SLOT = 9;
   public static final int TARGET_SLOT = 10;
   private static final int[] INPUT_SLOTS = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8};
   private static final int[] OUTPUT_SLOTS = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
   NonNullList<ItemStack> inventory;
   private Recipe recipe;
   private ItemStack crafts;
   private List recipes;

   private final LazyOptional<ItemStackHandlerAutoCrafter> holder =
           LazyOptional.of(() -> new ItemStackHandlerAutoCrafter(this));

   private int currentRecipeIndex;


   public TileAutoCrafter() {
      super(AutoCrafter.tileTypeAutoCraft);
      this.inventory = NonNullList.withSize(getSizeInventory(), ItemStack.EMPTY);
      this.recipe = new Recipe();
      this.crafts = ItemStack.EMPTY;
      this.currentRecipeIndex = 0;
   }

   @Nonnull
   @Override
   public CompoundNBT write(CompoundNBT compound) {
      super.write(compound);

      compound.put("inventory", ItemStackHelper.saveAllItems(new CompoundNBT(), this.inventory));
      compound.put("recipe", this.recipe.getNBT());
      compound.put("crafts", this.crafts.serializeNBT());
      return compound;
   }

   @Override
   public void read(BlockState state, CompoundNBT compound) {
      super.read(state, compound);

      if(compound.contains("inventory")) {
         ItemStackHelper.loadAllItems(compound.getCompound("inventory"), this.inventory);
      }

      if(compound.contains("recipe")) {
         this.recipe = Recipe.fromNBT(compound.getList("recipe", 10));
      }

      if(compound.contains("crafts")) {
         this.crafts =  ItemStack.read(compound.getCompound("crafts"));
      }

   }

   @Override
   public int getSizeInventory() {
      return 11;
   }

   @Override
   public boolean isEmpty() {
      for (ItemStack itemStack : this.inventory) {
         if (!itemStack.isEmpty()) {
            return false;
         }
      }

      return true;
   }

   @Nonnull
   @Override
   public ItemStack getStackInSlot (int index) {
      return index >= 0 && index < this.getSizeInventory() ? this.inventory.get(index) :ItemStack.EMPTY;
   }

   @Nonnull
   @Override
   public ItemStack decrStackSize(int index, int count) {
      ItemStack s = ItemStackHelper.getAndSplit(this.inventory, index, count);
      if(this.getStackInSlot(index).getCount() == 0) {
         this.setInventorySlotContents(index, ItemStack.EMPTY);
      }

      return s;
   }


   public ItemStack SIMULATEdecrStackSize(int index, int count) {
      ItemStack temp = getStackInSlot(index).copy();
      return temp.split(count);
   }

   @Nonnull
   @Override
   public ItemStack removeStackFromSlot(int index) {
      return ItemStackHelper.getAndRemove(inventory, index);
   }

   @Override
   public void setInventorySlotContents(int index, @Nonnull ItemStack stack) {
      ItemStack itemstack = this.inventory.get(index);
      this.inventory.set(index, stack);
      if(stack.getCount() > this.getInventoryStackLimit()) {
         stack.setCount(this.getInventoryStackLimit());
      }

      this.markDirty();
   }

   @Override
   public int getInventoryStackLimit() {
      return 64;
   }

   @Override
   public boolean isUsableByPlayer(@Nonnull PlayerEntity player) {
      assert world != null;
      return world.getTileEntity(getPos()) == this &&
              player.getDistanceSq(.5, .5, .5) <= 64;
   }

   @Override
   public void openInventory(PlayerEntity player) {
   }

   @Override
   public void closeInventory(PlayerEntity player) {
   }

   @Override
   public boolean isItemValidForSlot(int index, ItemStack stack) {
      return true;

   }

   //removed in 1.14
   /*
   @Override
   public int getField(int id) {
      return 0;
   }

   @Override
   public void setField(int id, int value) {
   }

   @Override
   public int getFieldCount() {
      return 0;
   }*/

   @Override
   public void clear() {

      for(int i = 0; i<inventory.size(); i++){
         inventory.set(i, ItemStack.EMPTY);
      }
   }

   @Nonnull
   @Override
   public ITextComponent getDisplayName() {
      return Utils.IText("Auto Crafter");
   }

   @Nonnull
   @Override
   public int[] getSlotsForFace(Direction side) {
      if(side.equals(Direction.DOWN)){return OUTPUT_SLOTS;}
      return INPUT_SLOTS;
   }

   public boolean isSlotAllowed(int index, ItemStack itemStack) {
      return index < 9 && this.recipe.matchesRecipe(index, itemStack);
   }

   @Override
   public boolean canInsertItem(int index, @Nonnull ItemStack itemStackIn, Direction direction) {
      //System.out.printf("canInsertItem: %d %s %s\n",index, itemStackIn.getItem().getRegistryName().toString(), isSlotAllowed(index, itemStackIn) && nextHasSameOrMore(index, itemStackIn));
      return isSlotAllowed(index, itemStackIn);
   }

   @Override
   public boolean canExtractItem(int index, @Nonnull ItemStack stack, @Nonnull Direction direction) {
      return index==OUTPUT_SLOT || (index<9&&!recipe.matchesRecipe(index, stack));
   }

   public void setRecipe(IRecipe recipe) {
      this.recipe.setRecipe(recipe);
      this.markDirty();
   }

   public void setRecipe(ListNBT recipeTag) {
      this.recipe = Recipe.fromNBT(recipeTag);
   }

   public void updateRecipes(ItemStack crafts, int index) {
      this.crafts = crafts;
      assert world != null;
      this.recipes = Utils.getValid(world,crafts);
      this.currentRecipeIndex = index % Math.max(1, this.recipes.size());
      if(this.recipes.size() > 0) {
         this.setRecipe((IRecipe)this.recipes.get(this.currentRecipeIndex));
      } else {
         this.recipe.clearRecipe();
      }

      this.markDirty();
   }

   public Recipe getRecipe() {
      return this.recipe;
   }

   public ItemStack getCrafts() {
      return this.crafts;
   }

   public void nextRecipe() {
      if(this.recipes == null) {
         this.updateRecipes(this.getCrafts(), this.currentRecipeIndex);
      }

      if(this.recipes.size() != 0) {
         ++this.currentRecipeIndex;
         this.currentRecipeIndex %= this.recipes.size();
         this.setRecipe((IRecipe)this.recipes.get(this.currentRecipeIndex));
      }
   }

   public void prevRecipe() {
      if(this.recipes == null) {
         this.updateRecipes(this.getCrafts(), this.currentRecipeIndex);
      }

      if(this.recipes.size() != 0) {
         --this.currentRecipeIndex;
         if(this.currentRecipeIndex < 0) {
            this.currentRecipeIndex = this.recipes.size() - 1;
         }

         this.setRecipe((IRecipe)this.recipes.get(this.currentRecipeIndex));
      }
   }

   public int getCurrentRecipeIndex() {
      return this.currentRecipeIndex;
   }

   @Nonnull
   @Override
   public <T> LazyOptional<T> getCapability(Capability<T> capability, Direction facing) {
      if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
         return holder.cast();

      return super.getCapability(capability, facing);
   }

   @Override
   public void tick() {
      assert this.world != null;
      if(!this.world.isBlockPowered(pos) /* && world.isBlockIndirectlyGettingPowered(pos)<= 0*/) {
         if(!this.recipe.getOutput().isEmpty()) {
            if(getStackInSlot(OUTPUT_SLOT).getCount() + this.recipe.getOutput().getCount() <= this.recipe.getOutput().getMaxStackSize()) {
               if(Recipe.matches(getStackInSlot(OUTPUT_SLOT), this.recipe.getOutput()) || getStackInSlot(OUTPUT_SLOT).isEmpty()) {

                  this.distributeItems();

                  for(int leftovers = 0; leftovers < 9; ++leftovers) {
                     if(!this.recipe.matchesRecipe(leftovers, this.inventory.get(leftovers))) {
                        return;
                     }
                  }

                  NonNullList <ItemStack> leftovers = this.recipe.getLeftovers(this.inventory, 0, 9);

                  for(int i = 0; i < 9; ++i) {
                     (this.inventory.get(i)).shrink(1);
                     if((this.inventory.get(i)).getCount() <= 0) {
                        this.setInventorySlotContents(i, ItemStack.EMPTY);
                     }

                     if(!(leftovers.get(i)).isEmpty()) {
                        if((this.inventory.get(i)).isEmpty()) {
                           this.setInventorySlotContents(i, leftovers.get(i));
                        } else {
                           InventoryHelper.spawnItemStack(this.world, this.pos.getX(), this.pos.getY(), this.pos.getZ(), leftovers.get(i));
                        }
                     }
                  }

                  if(getStackInSlot(OUTPUT_SLOT).isEmpty()) {
                     setInventorySlotContents(OUTPUT_SLOT, this.recipe.getOutput());
                  } else {
                     getStackInSlot(OUTPUT_SLOT).grow(this.recipe.getOutput().getCount());
                  }

                  this.markDirty();
               }
            }
         }
      }
   }

   private void distributeItems() {
      for(int i = 0; i < 9; ++i) {
         ItemStack current = getStackInSlot(i);
         if(!current.isEmpty()) {
            int nextMatch = this.nextMatch(i);
            if(nextMatch >= 0) {
               if(getStackInSlot(nextMatch).isEmpty()) {
                  if(current.getCount() >= 2) {
                     setInventorySlotContents(nextMatch, current.split(1));
                  }
               } else if(current.getCount() > getStackInSlot(nextMatch).getCount()) {
                  current.shrink(1);
                  getStackInSlot(nextMatch).grow(1);
               }
            }
         }
      }

   }

   private int nextMatch(int j) {
      ItemStack is = getStackInSlot(j);

      for(int i = 0; i < 9; ++i) {
         int c = (i + j + 1) % 9;
         if(Recipe.matches(is, getStackInSlot(c)) || getStackInSlot(c).isEmpty() && this.recipe.matchesRecipe(c, is)) {
            return c == j?-1:c;
         }
      }

      return -1;
   }

   public void setCurrentRecipeIndex(int integer) {
      this.currentRecipeIndex = integer;
   }

   public void setCrafts(ItemStack itemStack) {
      this.crafts = itemStack;
   }


   @Nullable
   @Override
   public Container createMenu(int windowId, PlayerInventory playerInventory, PlayerEntity playerEntity) {
      return new ContainerAutoCrafter(windowId, playerInventory, this);
   }
}
