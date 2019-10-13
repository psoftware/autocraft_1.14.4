package com.theincgi.autocrafter.tileEntity;

import com.theincgi.autocrafter.Recipe;
import com.theincgi.autocrafter.Utils;
import com.theincgi.autocrafter.tileEntity.ItemStackHandlerAutoCrafter;
import java.util.List;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;

public class TileAutoCrafter extends TileEntity implements ITickable, ISidedInventory {

   public static final int OUTPUT_SLOT = 9;
   public static final int TARGET_SLOT = 10;
   private static final int[] INPUT_SLOTS = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8};
   private static final int[] OUTPUT_SLOTS = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
   NonNullList inventory;
   private Recipe recipe;
   private ItemStack crafts;
   private String customName;
   private List recipes;
   private ItemStackHandlerAutoCrafter ishac;
   private int currentRecipeIndex;


   public TileAutoCrafter() {
      this.inventory = NonNullList.func_191197_a(this.func_70302_i_(), ItemStack.field_190927_a);
      this.recipe = new Recipe();
      this.crafts = ItemStack.field_190927_a;
      this.currentRecipeIndex = 0;
      this.ishac = new ItemStackHandlerAutoCrafter(this);
   }

   public NBTTagCompound func_189515_b(NBTTagCompound compound) {
      super.func_189515_b(compound);
      if(this.func_145818_k_()) {
         compound.func_74778_a("customName", this.customName);
      }

      compound.func_74782_a("inventory", ItemStackHelper.func_191282_a(new NBTTagCompound(), this.inventory));
      compound.func_74782_a("recipe", this.recipe.getNBT());
      compound.func_74782_a("crafts", this.crafts.serializeNBT());
      return compound;
   }

   public void func_145839_a(NBTTagCompound compound) {
      super.func_145839_a(compound);
      if(compound.func_74764_b("customName")) {
         this.customName = compound.func_74779_i("customName");
      }

      if(compound.func_74764_b("inventory")) {
         ItemStackHelper.func_191283_b(compound.func_74775_l("inventory"), this.inventory);
      }

      if(compound.func_74764_b("recipe")) {
         this.recipe = Recipe.fromNBT(compound.func_150295_c("recipe", 10));
      }

      if(compound.func_74764_b("crafts")) {
         this.crafts = new ItemStack(compound.func_74775_l("crafts"));
      }

   }

   public int func_70302_i_() {
      return 11;
   }

   public boolean func_191420_l() {
      for(int i = 0; i < this.inventory.size(); ++i) {
         if(!((ItemStack)this.inventory.get(i)).func_190926_b()) {
            return false;
         }
      }

      return true;
   }

   public ItemStack func_70301_a(int index) {
      return index >= 0 && index < this.func_70302_i_()?(ItemStack)this.inventory.get(index):ItemStack.field_190927_a;
   }

   public ItemStack func_70298_a(int index, int count) {
      ItemStack s = ItemStackHelper.func_188382_a(this.inventory, index, count);
      if(this.func_70301_a(index).func_190916_E() == 0) {
         this.func_70299_a(index, ItemStack.field_190927_a);
      }

      return s;
   }

   public ItemStack SIMULATEdecrStackSize(int index, int count) {
      ItemStack temp = this.func_70301_a(index).func_77946_l();
      return temp.func_77979_a(count);
   }

   public ItemStack func_70304_b(int index) {
      return ItemStackHelper.func_188383_a(this.inventory, index);
   }

   public void func_70299_a(int index, ItemStack stack) {
      ItemStack itemstack = (ItemStack)this.inventory.get(index);
      this.inventory.set(index, stack);
      if(stack.func_190916_E() > this.func_70297_j_()) {
         stack.func_190920_e(this.func_70297_j_());
      }

      this.func_70296_d();
   }

   public int func_70297_j_() {
      return 64;
   }

   public boolean func_70300_a(EntityPlayer player) {
      return this.field_145850_b.func_175625_s(this.func_174877_v()) == this && player.func_174818_b(this.field_174879_c.func_177963_a(0.5D, 0.5D, 0.5D)) <= 64.0D;
   }

   public void func_174889_b(EntityPlayer player) {}

   public void func_174886_c(EntityPlayer player) {}

   public boolean func_94041_b(int index, ItemStack stack) {
      return true;
   }

   public int func_174887_a_(int id) {
      return 0;
   }

   public void func_174885_b(int id, int value) {}

   public int func_174890_g() {
      return 0;
   }

   public void func_174888_l() {
      for(int i = 0; i < this.inventory.size(); ++i) {
         this.inventory.set(i, ItemStack.field_190927_a);
      }

   }

   public String func_70005_c_() {
      return this.func_145818_k_()?this.customName:"Auto Crafter";
   }

   public boolean func_145818_k_() {
      return this.customName != null;
   }

   public ITextComponent func_145748_c_() {
      return Utils.IText(this.func_70005_c_());
   }

   public int[] func_180463_a(EnumFacing side) {
      return side.equals(EnumFacing.DOWN)?OUTPUT_SLOTS:INPUT_SLOTS;
   }

   public boolean isSlotAllowed(int index, ItemStack itemStack) {
      return index < 9 && this.recipe.matchesRecipe(index, itemStack);
   }

   public boolean func_180462_a(int index, ItemStack itemStackIn, EnumFacing direction) {
      return this.isSlotAllowed(index, itemStackIn);
   }

   public boolean func_180461_b(int index, ItemStack stack, EnumFacing direction) {
      return index == 9 || index < 9 && !this.recipe.matchesRecipe(index, stack);
   }

   public void setCustomName(String displayName) {
      this.customName = displayName;
      this.func_70296_d();
   }

   public void setRecipe(IRecipe recipe) {
      this.recipe.setRecipe(recipe);
      this.func_70296_d();
   }

   public void setRecipe(NBTTagList recipeTag) {
      this.recipe = Recipe.fromNBT(recipeTag);
   }

   public void updateRecipes(ItemStack crafts, int index) {
      this.crafts = crafts;
      this.recipes = Utils.getValid(crafts);
      this.currentRecipeIndex = index % Math.max(1, this.recipes.size());
      if(this.recipes.size() > 0) {
         this.setRecipe((IRecipe)this.recipes.get(this.currentRecipeIndex));
      } else {
         this.recipe.clearRecipe();
      }

      this.func_70296_d();
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

   public boolean hasCapability(Capability capability, EnumFacing facing) {
      return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY?true:super.hasCapability(capability, facing);
   }

   public Object getCapability(Capability capability, EnumFacing facing) {
      return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY?this.ishac:super.getCapability(capability, facing);
   }

   public void func_73660_a() {
      if(!this.field_145850_b.func_175640_z(this.field_174879_c) && this.field_145850_b.func_175687_A(this.field_174879_c) <= 0) {
         if(!this.recipe.getOutput().func_190926_b()) {
            if(this.func_70301_a(9).func_190916_E() + this.recipe.getOutput().func_190916_E() <= this.recipe.getOutput().func_77976_d()) {
               if(Recipe.matches(this.func_70301_a(9), this.recipe.getOutput()) || this.func_70301_a(9).func_190926_b()) {
                  this.distibuteItems();

                  for(int leftovers = 0; leftovers < 9; ++leftovers) {
                     if(!this.recipe.matchesRecipe(leftovers, (ItemStack)this.inventory.get(leftovers))) {
                        return;
                     }
                  }

                  NonNullList var3 = this.recipe.getLeftovers(this.inventory, 0, 9);

                  for(int i = 0; i < 9; ++i) {
                     ((ItemStack)this.inventory.get(i)).func_190918_g(1);
                     if(((ItemStack)this.inventory.get(i)).func_190916_E() <= 0) {
                        this.func_70299_a(i, ItemStack.field_190927_a);
                     }

                     if(!((ItemStack)var3.get(i)).func_190926_b()) {
                        if(((ItemStack)this.inventory.get(i)).func_190926_b()) {
                           this.func_70299_a(i, (ItemStack)var3.get(i));
                        } else {
                           InventoryHelper.func_180173_a(this.field_145850_b, (double)this.field_174879_c.func_177958_n(), (double)this.field_174879_c.func_177956_o(), (double)this.field_174879_c.func_177952_p(), (ItemStack)var3.get(i));
                        }
                     }
                  }

                  if(this.func_70301_a(9).func_190926_b()) {
                     this.func_70299_a(9, this.recipe.getOutput());
                  } else {
                     this.func_70301_a(9).func_190917_f(this.recipe.getOutput().func_190916_E());
                  }

                  this.func_70296_d();
               }
            }
         }
      }
   }

   private void distibuteItems() {
      for(int i = 0; i < 9; ++i) {
         ItemStack current = this.func_70301_a(i);
         if(!current.func_190926_b()) {
            int nextMatch = this.nextMatch(i);
            if(nextMatch >= 0) {
               if(this.func_70301_a(nextMatch).func_190926_b()) {
                  if(current.func_190916_E() >= 2) {
                     this.func_70299_a(nextMatch, current.func_77979_a(1));
                  }
               } else if(current.func_190916_E() > this.func_70301_a(nextMatch).func_190916_E()) {
                  current.func_190918_g(1);
                  this.func_70301_a(nextMatch).func_190917_f(1);
               }
            }
         }
      }

   }

   private int nextMatch(int j) {
      ItemStack is = this.func_70301_a(j);

      for(int i = 0; i < 9; ++i) {
         int c = (i + j + 1) % 9;
         if(Recipe.matches(is, this.func_70301_a(c)) || this.func_70301_a(c).func_190926_b() && this.recipe.matchesRecipe(c, is)) {
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

}
