//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.theincgi.autocrafter;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.item.crafting.ShapelessRecipe;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.crafting.CompoundIngredient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class Recipe {
    ItemStack output;
    public NonNullList<Recipe.ItemOptions> items;

    public Recipe() {
        this.items = NonNullList.withSize(9, Recipe.ItemOptions.EMPTY);
    }

    public void setRecipe(IRecipe iRecipe) {

        this.output = iRecipe.getRecipeOutput();

        for (int i = 0; i < this.items.size(); ++i)
            this.items.set(i, Recipe.ItemOptions.EMPTY);

        if (iRecipe instanceof ShapedRecipe) {
            ShapedRecipe sr = (ShapedRecipe) iRecipe;

            for (int i = 0; i < sr.getIngredients().size(); ++i) {
                ArrayList<ItemStack> alis = new ArrayList<>(Arrays.asList(sr.getIngredients().get(i).getMatchingStacks()));
                this.items.set(this.realIndx(i, sr.getRecipeWidth(), sr.getRecipeHeight()), new Recipe.ItemOptions(alis));
            }
        } else if (iRecipe instanceof ShapelessRecipe) {
            ShapelessRecipe shapelessRecipe = (ShapelessRecipe) iRecipe;

            for (int i = 0; i < shapelessRecipe.getIngredients().size(); ++i) {
                ItemStack[] matchingStacks = shapelessRecipe.getIngredients().get(i).getMatchingStacks();

                ArrayList<ItemStack> matchingStacksAsArrayList = new ArrayList<>(Arrays.asList(matchingStacks));

                this.items.set(i, new Recipe.ItemOptions(matchingStacksAsArrayList));
            }
        } else {
            this.output = ItemStack.EMPTY;
            Utils.log("It seems that " + iRecipe.getClass().toGenericString() + " is not a supported recipe type.");
        }
    }

    protected int realIndx(int i, int wid, int hei) {
        int x = i % wid;
        int y = (i - x) / wid;
        return x + y * 3;
    }

    public ItemStack getDisplayItem(int slot) {
        Recipe.ItemOptions opt = this.items.get(slot);
        if (opt.possibleOptions.size() == 0) {
            return ItemStack.EMPTY;
        } else {
            long mod = System.currentTimeMillis() / 500L % (long) opt.possibleOptions.size();
            ItemStack is = opt.possibleOptions.get((int) mod);
            ItemStack itemstack = new ItemStack(is.getItem(), 1);
            itemstack.setDamage(is.getDamage() == 32767 ? (int) mod % Math.max(1, is.getMaxDamage()) : is.getDamage());
            return itemstack;
        }
    }

    public boolean stackForSlotFitsThisRecipe(int slot, ItemStack itemStack) {
        Recipe.ItemOptions opt = this.items.get(slot);
        if (opt.possibleOptions.size() == 0 && itemStack.isEmpty()) return true;

        for (ItemStack i : opt.possibleOptions) {
            if (itemStacksMatch(i, itemStack)) return true;
        }
        return false;
    }

    public ListNBT serializeNBT() {
        ListNBT listNBT = new ListNBT();
        listNBT.add(this.getOutput().serializeNBT());

        for (ItemOptions item : this.items) {
            CompoundNBT slotNBT = new CompoundNBT();
            slotNBT.put("item", item.getNBT());
            listNBT.add(slotNBT);
        }

        return listNBT;
    }

    public static Recipe read(ListNBT tags) {
        Recipe recipe = new Recipe();
        recipe.output = ItemStack.read(tags.getCompound(0));

        for (int i = 1; i < tags.size(); ++i) {
            CompoundNBT slot = tags.getCompound(i);
            Recipe.ItemOptions opts = Recipe.ItemOptions.fromNBT(slot.getList("item", 10));
            recipe.items.set(i - 1, opts);
        }

        return recipe;
    }

    public void clearRecipe() {
        for (int i = 0; i < this.items.size(); ++i) {
            this.items.set(i, Recipe.ItemOptions.EMPTY);
        }

        this.output = ItemStack.EMPTY;
    }

    public String toString() {
        String s = "Recipe:\n";

        for (int i = 0; i < this.items.size(); ++i) {
            Recipe.ItemOptions opts = (Recipe.ItemOptions) this.items.get(i);
            s = s + "\ti: " + i + " = {";

            for (int j = 0; j < opts.possibleOptions.size(); ++j) {
                s = s + ((ItemStack) opts.possibleOptions.get(j)).getItem().getRegistryName().toString() + " : ";
                s = s + ((ItemStack) opts.possibleOptions.get(j)).getDamage();
                if (j < opts.possibleOptions.size() - 1) {
                    s = s + ", ";
                }
            }

            s = s + "}";
            if (i != this.items.size() - 1) {
                s = s + "\n";
            }
        }

        return s;
    }

    public static boolean itemStacksMatch(ItemStack crafts, ItemStack stack) {
        if (crafts.isEmpty() && stack.isEmpty()) return true;
        if (!crafts.getItem().equals(stack.getItem())) return false;
//        if (crafts.getDamage() != 32767 && stack.getDamage() != 32767) return crafts.getDamage() == stack.getDamage();
        return true;
    }

    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (!(obj instanceof Recipe)) return false;

        Recipe r = (Recipe) obj;

        for (int i = 0; i < this.items.size(); ++i) {
            if (!r.items.get(i).equals(this.items.get(i))) {
                return false;
            }
        }

        return true;
    }

    public NonNullList<ItemStack> getLeftovers(NonNullList<ItemStack> inv, int from, int to) {
        NonNullList<ItemStack> leftovers = NonNullList.withSize(to - from, ItemStack.EMPTY);

        for (int i = from; i < to; ++i) {
            leftovers.set(i, ForgeHooks.getContainerItem(inv.get(i)));
        }

        return leftovers;
    }

    public ItemStack getOutput() {
        return this.output == null ? ItemStack.EMPTY : this.output.copy();
    }

    public static class ItemOptions {
        private static final Recipe.ItemOptions EMPTY = new Recipe.ItemOptions();
        NonNullList<ItemStack> possibleOptions;

        public ItemOptions() {
            this.possibleOptions = NonNullList.create();
        }

        public static Recipe.ItemOptions fromNBT(ListNBT tagList) {
            Recipe.ItemOptions opts = new Recipe.ItemOptions();

            for (int i = 0; i < tagList.size(); ++i) {
                CompoundNBT itemTag = tagList.getCompound(i);
                opts.possibleOptions.add(ItemStack.read(itemTag));
            }

            return opts;
        }

        public NonNullList<ItemStack> getPossibleOptions() {
            return this.possibleOptions;
        }

        public ItemOptions(ItemStack[] matching) {
            this.possibleOptions = NonNullList.create();
            Collections.addAll(this.possibleOptions, matching);
        }

       /* public ItemOptions(OreIngredient i) {
            this(i.getMatchingStacks());
        }*/

        public ItemOptions(CompoundIngredient ci) {
            this(ci.getMatchingStacks());
        }

        public ItemOptions(ItemStack itemStack) {
            this.possibleOptions = NonNullList.create();
            this.possibleOptions.add(itemStack);
        }

        public ItemOptions(NonNullList<ItemStack> possibleOptions) {
            this.possibleOptions = possibleOptions;
        }

        public ItemOptions(List<ItemStack> possibleOptions) {
            this.possibleOptions = NonNullList.create();
            this.possibleOptions.addAll(possibleOptions);
        }

        public ItemOptions(Ingredient ingredient) {
            this.possibleOptions = NonNullList.create();
            this.possibleOptions.addAll(Arrays.asList(ingredient.getMatchingStacks()));
        }

        public ListNBT getNBT() {
            ListNBT nbtList = new ListNBT();

            for (ItemStack opt : this.possibleOptions) {
                nbtList.add(opt.serializeNBT());
            }

            return nbtList;
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof ItemOptions)) return false;

            ItemOptions other = (ItemOptions) obj;
            if (other.possibleOptions.size() != this.possibleOptions.size()) return false;

            for (int i = 0; i < other.possibleOptions.size(); ++i) {
                ItemStack lookingFor = other.possibleOptions.get(i);
                boolean found = false;

                for (ItemStack possibleOption : this.possibleOptions) {
                    if (Recipe.itemStacksMatch(lookingFor, possibleOption)) {
                        found = true;
                        break;
                    }
                }

                if (!found) {
                    return false;
                }
            }

            return true;
        }
    }
}
