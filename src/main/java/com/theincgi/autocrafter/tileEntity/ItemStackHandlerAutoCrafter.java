package com.theincgi.autocrafter.tileEntity;

import com.theincgi.autocrafter.Recipe;
import com.theincgi.autocrafter.tileEntity.TileAutoCrafter;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.items.IItemHandler;

public class ItemStackHandlerAutoCrafter implements IItemHandler {

   private TileAutoCrafter tac;


   public ItemStackHandlerAutoCrafter(TileAutoCrafter tac) {
      this.tac = tac;
   }

   public int getSlots() {
      return 11;
   }

   public ItemStack getStackInSlot(int slot) {
      return this.tac.func_70301_a(slot);
   }

   public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
      if(this.tac.func_180462_a(slot, stack, EnumFacing.UP)) {
         int space = this.getSpaceFor(slot, stack);
         if(simulate) {
            if(stack.func_190916_E() <= space) {
               return ItemStack.field_190927_a;
            } else {
               ItemStack newCount1 = stack.func_77946_l();
               newCount1.func_190918_g(space);
               return newCount1;
            }
         } else {
            int newCount = this.getStackInSlot(slot).func_190916_E() + stack.func_190916_E();
            newCount = Math.min(newCount, this.getStackInSlot(slot).func_77976_d());
            if(this.getStackInSlot(slot).func_190926_b()) {
               this.tac.func_70299_a(slot, stack.func_77946_l().func_77979_a(newCount));
            } else {
               this.getStackInSlot(slot).func_190920_e(newCount);
            }

            ItemStack temp = stack.func_77946_l();
            temp.func_190918_g(space);
            return temp;
         }
      } else {
         return stack;
      }
   }

   public ItemStack extractItem(int slot, int amount, boolean simulate) {
      ItemStack stackIn = this.getStackInSlot(slot);
      return this.tac.func_180461_b(slot, stackIn, EnumFacing.DOWN)?(simulate?this.tac.SIMULATEdecrStackSize(slot, amount):this.tac.func_70298_a(slot, amount)):ItemStack.field_190927_a;
   }

   public int getSpaceFor(int indx, ItemStack s) {
      if(!this.tac.getRecipe().matchesRecipe(indx, s)) {
         boolean var3 = false;
      }

      if(this.tac.getRecipe().matchesRecipe(indx, s)) {
         if(Recipe.matches(s, this.getStackInSlot(indx))) {
            return s.func_77976_d() - this.getStackInSlot(indx).func_190916_E();
         }

         if(this.getStackInSlot(indx).func_190926_b()) {
            return s.func_77976_d();
         }
      }

      return 0;
   }

   public int getSlotLimit(int slot) {
      return this.getStackInSlot(slot).func_77976_d();
   }
}
