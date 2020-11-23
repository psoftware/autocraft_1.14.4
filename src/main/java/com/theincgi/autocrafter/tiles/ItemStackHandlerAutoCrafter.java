package com.theincgi.autocrafter.tiles;

import com.theincgi.autocrafter.Recipe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;

import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;

public class ItemStackHandlerAutoCrafter implements IItemHandler {

    private TileAutoCrafter tac;


    public ItemStackHandlerAutoCrafter(TileAutoCrafter tac)
    {
        this.tac = tac;
    }

    @Override
    public int getSlots()
    {
        return 11;
    }

    @Nonnull
    @Override
    public ItemStack getStackInSlot(int slot)
    {
        return tac.getStackInSlot(slot);
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate)
    {
        if (!tac.canInsertItem(slot, stack, Direction.UP))
        {
            return stack;
        }
        int space = this.getSpaceFor(slot, stack);
        if (simulate)
        {
            if (stack.getCount() <= space)
            {
                return ItemStack.EMPTY;
            }
            else
            {
                ItemStack newCount1 = stack.copy();
                newCount1.shrink(space);
                return newCount1;
            }
        }
        else
        {
            int newCount = this.getStackInSlot(slot).getCount() + stack.getCount();
            newCount = Math.min(newCount, this.getStackInSlot(slot).getMaxStackSize());
            if (this.getStackInSlot(slot).isEmpty())
            {
                this.tac.setInventorySlotContents(slot, stack.copy().split(newCount));
            }
            else
            {
                this.getStackInSlot(slot).setCount(newCount);
            }

            ItemStack temp = stack.copy();
            temp.shrink(space);
            return temp;
        }
    }

    @Nonnull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate)
    {
        ItemStack stackIn = this.getStackInSlot(slot);
        return this.tac.canExtractItem(slot, stackIn, Direction.DOWN) ? (simulate ? this.tac.SIMULATEdecrStackSize(slot, amount) : this.tac.decrStackSize(slot, amount)) : ItemStack.EMPTY;
    }

    public int getSpaceFor(int indx, ItemStack s)
    {
        if (this.tac.getRecipe().stackForSlotFitsThisRecipe(indx, s))
        {
            if (Recipe.itemStacksMatch(s, this.getStackInSlot(indx)))
            {
                return s.getMaxStackSize() - this.getStackInSlot(indx).getCount();
            }

            if (this.getStackInSlot(indx).isEmpty())
            {
                return s.getMaxStackSize();
            }
        }

        return 0;
    }

    @Override
    public int getSlotLimit(int slot)
    {
        return this.getStackInSlot(slot).getMaxStackSize();
    }

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack)
    {//not sure
        return tac.isItemValidForSlot(slot, stack);
    }
}
