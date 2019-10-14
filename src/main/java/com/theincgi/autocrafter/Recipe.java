//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.theincgi.autocrafter;

import java.util.*;

import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.item.Item;
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
import net.minecraftforge.oredict.OreIngredient;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class Recipe {
    ItemStack output;
    public NonNullList<Recipe.ItemOptions> items;

    public Recipe() {
        this.items = NonNullList.withSize(9, Recipe.ItemOptions.EMPTY);
    }

    public void setRecipe(IRecipe iRecipe) {
        this.output = iRecipe.getRecipeOutput();

        for(int i = 0; i < this.items.size(); ++i)
            this.items.set(i, Recipe.ItemOptions.EMPTY);

        if (iRecipe instanceof ShapedRecipe) {
            ShapedRecipe sr = (ShapedRecipe)iRecipe;
            
            for(int i = 0; i < sr.getIngredients().size(); ++i) {
                ArrayList<ItemStack> alis = new ArrayList<>(Arrays.asList(sr.getIngredients().get(i).getMatchingStacks()));
                this.items.set(this.realIndx(i, sr.getRecipeWidth(), sr.getRecipeHeight()), new Recipe.ItemOptions(alis));
            }
        } else {
            Object o;
            ItemStack is;
            OreIngredient oreIngredient;
            CompoundIngredient ci;
            Ingredient ing;

            if (iRecipe instanceof ShapedOreRecipe) {
                ShapedOreRecipe sor = (ShapedOreRecipe)iRecipe;

                for(int i = 0; i < sor.func_192400_c().size(); ++i) {
                    o = sor.func_192400_c().get(i);
                    if (o == null) {
                        o = ItemStack.EMPTY;
                    }

                    if (o instanceof List)
                        this.items.set(this.realIndx(i, sor.getWidth(), sor.getHeight()), new Recipe.ItemOptions((List)o));
                    else if (o instanceof ItemStack)
                        this.items.set(this.realIndx(i, sor.getWidth(), sor.getHeight()), new Recipe.ItemOptions((ItemStack)o));
                    else if (o instanceof OreIngredient)
                        this.items.set(i, new Recipe.ItemOptions((OreIngredient)o));
                    else if (o instanceof CompoundIngredient)
                        this.items.set(i, new Recipe.ItemOptions((CompoundIngredient)o));
                    else if (o instanceof Ingredient)
                        this.items.set(i, new Recipe.ItemOptions((Ingredient)o));
                    else {
                        this.output = ItemStack.EMPTY;
                        Utils.log("AutoCrafter: ShapedOreRecipe missing case '" + o.getClass() + "'");
                    }
                }
            } else if (iRecipe instanceof ShapelessRecipe) {
                ShapelessRecipe sr = (ShapelessRecipe)iRecipe;

                for(int i = 0; i < sr.getIngredients().size(); ++i) {
                    ItemStack[] isa = sr.getIngredients().get(i).getMatchingStacks();

                    ArrayList<ItemStack> ios = new ArrayList<>();
                    ios.addAll(Arrays.asList(isa));

                    this.items.set(i, new Recipe.ItemOptions(ios));
                }
            } else if (iRecipe instanceof ShapelessOreRecipe) {
                ShapelessOreRecipe slor = (ShapelessOreRecipe)iRecipe;

                for(int i = 0; i < slor.func_192400_c().size(); ++i) {
                    o = slor.func_192400_c().get(i);
                    if (o == null) {
                        o = ItemStack.EMPTY;
                    }

                    if (o instanceof List)
                        this.items.set(i, new Recipe.ItemOptions((List)o));
                    else if (o instanceof ItemStack) {
                        is = (ItemStack)o;
                        this.items.set(i, new Recipe.ItemOptions(is));
                    } else if (o instanceof OreIngredient) {
                        oreIngredient = (OreIngredient)o;
                        this.items.set(i, new Recipe.ItemOptions(oreIngredient));
                    } else if (o instanceof CompoundIngredient) {
                        ci = (CompoundIngredient)o;
                        this.items.set(i, new Recipe.ItemOptions(ci));
                    } else if (o instanceof Ingredient) {
                        ing = (Ingredient)o;
                        this.items.set(i, new Recipe.ItemOptions(ing));
                    } else {
                        this.output = ItemStack.EMPTY;
                        Utils.log("AutoCrafter: ShaplessOreRecipe missing case '" + o.getClass() + "'");
                    }
                }
            } else {
                this.output = ItemStack.EMPTY;
                Utils.log("It seems " + iRecipe.getClass().toGenericString() + " isn't a supported recipe type.");
            }
        }

    }

    protected int realIndx(int i, int wid, int hei) {
        int x = i % wid;
        int y = (i - x) / wid;
        return x + y * 3;
    }

    public ItemStack getDisplayItem(int slot) {
        Recipe.ItemOptions opt = (Recipe.ItemOptions)this.items.get(slot);
        if (opt.opts.size() == 0) {
            return ItemStack.EMPTY;
        } else {
            long mod = System.currentTimeMillis() / 500L % (long)opt.opts.size();
            ItemStack is = (ItemStack)opt.opts.get((int)mod);
            ItemStack itemstack = new ItemStack(is.getItem(), 1);
            itemstack.setDamage(is.getDamage() == 32767 ? (int)mod % Math.max(1, is.getMaxDamage()) : is.getDamage());
            return itemstack;
        }
    }

    public boolean matchesRecipe(int slot, ItemStack itemStack) {
        Recipe.ItemOptions opt = (Recipe.ItemOptions)this.items.get(slot);
        if (opt.opts.size() == 0 && itemStack.isEmpty()) {
            return true;
        } else {
            Iterator var4 = opt.opts.iterator();

            for(ItemStack i : opt.opts){
                Item item1 = i.getItem();
                Item item2 = itemStack.getItem();
                if(matches(i, itemStack))
                    return true;
            }
            return false;
        }
    }

    public ListNBT getNBT() {
        ListNBT list = new ListNBT();
        list.add(this.getOutput().serializeNBT());

        for(int i = 0; i < this.items.size(); ++i) {
            CompoundNBT slot = new CompoundNBT();
            slot.put("item", ((Recipe.ItemOptions)this.items.get(i)).getNBT());
            list.add(slot);
        }

        return list;
    }

    public static Recipe fromNBT(ListNBT tags) {
        Recipe recipe = new Recipe();
        recipe.output = ItemStack.read(tags.getCompound(0));

        for(int i = 1; i < tags.size(); ++i) {
            CompoundNBT slot = tags.getCompound(i);
            Recipe.ItemOptions opts = Recipe.ItemOptions.fromNBT(slot.getList("item", 10));
            recipe.items.set(i - 1, opts);
        }

        return recipe;
    }

    public void clearRecipe() {
        for(int i = 0; i < this.items.size(); ++i) {
            this.items.set(i, Recipe.ItemOptions.EMPTY);
        }

        this.output = ItemStack.EMPTY;
    }

    public String toString() {
        String s = "Recipe:\n";

        for(int i = 0; i < this.items.size(); ++i) {
            Recipe.ItemOptions opts = (Recipe.ItemOptions)this.items.get(i);
            s = s + "\ti: " + i + " = {";

            for(int j = 0; j < opts.opts.size(); ++j) {
                s = s + ((ItemStack)opts.opts.get(j)).getItem().getRegistryName().toString() + " : ";
                s = s + ((ItemStack)opts.opts.get(j)).getDamage();
                if (j < opts.opts.size() - 1) {
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

    public static boolean matches(ItemStack crafts, ItemStack stack) {
        if (crafts.isEmpty() && stack.isEmpty())
            return true;
        else if (!crafts.getItem().equals(stack.getItem()))
            return false;
        else if (crafts.getDamage() != 32767 && stack.getDamage() != 32767)
            return crafts.getDamage() == stack.getDamage();
        else
            return true;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        } else if (!(obj instanceof Recipe)) {
            return false;
        } else {
            Recipe r = (Recipe)obj;

            for(int i = 0; i < this.items.size(); ++i) {
                if (!((Recipe.ItemOptions)r.items.get(i)).equals(this.items.get(i))) {
                    return false;
                }
            }

            return true;
        }
    }

    public NonNullList<ItemStack> getLeftovers(NonNullList<ItemStack> inv, int from, int to) {
        NonNullList<ItemStack> ret = NonNullList.withSize(to - from, ItemStack.EMPTY);

        for(int i = from; i < to; ++i) {
            ret.set(i, ForgeHooks.getContainerItem((ItemStack)inv.get(i)));
        }

        return ret;
    }

    public ItemStack getOutput() {
        return this.output == null ? ItemStack.EMPTY : this.output.copy();
    }

    public static class ItemOptions {
        private static final Recipe.ItemOptions EMPTY = new Recipe.ItemOptions();
        NonNullList<ItemStack> opts;

        public ItemOptions() {
            this.opts = NonNullList.create();
        }

        public static Recipe.ItemOptions fromNBT(ListNBT tagList) {
            Recipe.ItemOptions opts = new Recipe.ItemOptions();

            for(int i = 0; i < tagList.size(); ++i) {
                CompoundNBT itemTag = tagList.getCompound(i);
                opts.opts.add(ItemStack.read(itemTag));
            }

            return opts;
        }

        public NonNullList<ItemStack> getOpts() {
            return this.opts;
        }

        public ItemOptions(ItemStack[] matching) {
            this.opts = NonNullList.create();
            Collections.addAll(this.opts, matching);
        }

        public ItemOptions(OreIngredient i) {
            this(i.getMatchingStacks());
        }

        public ItemOptions(CompoundIngredient ci) {
            this(ci.getMatchingStacks());
        }

        public ItemOptions(ItemStack itemStack) {
            this.opts = NonNullList.create();
            this.opts.add(itemStack);
        }

        public ItemOptions(NonNullList<ItemStack> opts) {
            this.opts = NonNullList.create();
            this.opts = opts;
        }

        public ItemOptions(List<ItemStack> opts) {
            this.opts = NonNullList.create();

            for(int i = 0; i < opts.size(); ++i) {
                this.opts.add(opts.get(i));
            }

        }

        public ItemOptions(Ingredient ing) {
            this.opts = NonNullList.create();
            ItemStack[] var2 = ing.getMatchingStacks();
            int var3 = var2.length;

            for(int var4 = 0; var4 < var3; ++var4) {
                ItemStack i = var2[var4];
                this.opts.add(i);
            }

        }

        public ListNBT getNBT() {
            ListNBT list = new ListNBT();

            for(int i = 0; i < this.opts.size(); ++i) {
                list.add(((ItemStack)this.opts.get(i)).serializeNBT());
            }

            return list;
        }

        public boolean equals(Object obj) {
            if (obj != null && obj instanceof Recipe.ItemOptions) {
                Recipe.ItemOptions io = (Recipe.ItemOptions)obj;
                if (io.opts.size() != this.opts.size()) {
                    return false;
                } else {
                    for(int i = 0; i < io.opts.size(); ++i) {
                        boolean found = false;
                        ItemStack lookingFor = (ItemStack)io.opts.get(i);

                        for(int j = 0; j < this.opts.size(); ++j) {
                            if (Recipe.matches(lookingFor, (ItemStack)this.opts.get(j))) {
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
            } else {
                return false;
            }
        }
    }
}
