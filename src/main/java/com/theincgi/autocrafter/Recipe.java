//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.theincgi.autocrafter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
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
        this.items = NonNullList.func_191197_a(9, Recipe.ItemOptions.EMPTY);
    }

    public void setRecipe(IRecipe iRecipe) {
        this.output = iRecipe.func_77571_b();

        for(int i = 0; i < this.items.size(); ++i) {
            this.items.set(i, Recipe.ItemOptions.EMPTY);
        }

        int i;
        int j;
        if (iRecipe instanceof ShapedRecipes) {
            ShapedRecipes sr = (ShapedRecipes)iRecipe;

            for(i = 0; i < sr.field_77574_d.size(); ++i) {
                ItemStack[] s = ((Ingredient)sr.field_77574_d.get(i)).func_193365_a();
                ArrayList<ItemStack> alis = new ArrayList();

                for(j = 0; j < s.length; ++j) {
                    alis.add(s[j]);
                }

                this.items.set(this.realIndx(i, sr.field_77576_b, sr.field_77577_c), new Recipe.ItemOptions(alis));
            }
        } else {
            Object o;
            ItemStack is;
            OreIngredient oreIngredient;
            CompoundIngredient ci;
            Ingredient ing;
            if (iRecipe instanceof ShapedOreRecipe) {
                ShapedOreRecipe sor = (ShapedOreRecipe)iRecipe;

                for(i = 0; i < sor.func_192400_c().size(); ++i) {
                    o = sor.func_192400_c().get(i);
                    if (o == null) {
                        o = ItemStack.field_190927_a;
                    }

                    if (o instanceof List) {
                        this.items.set(this.realIndx(i, sor.getWidth(), sor.getHeight()), new Recipe.ItemOptions((List)o));
                    } else if (o instanceof ItemStack) {
                        is = (ItemStack)o;
                        this.items.set(this.realIndx(i, sor.getWidth(), sor.getHeight()), new Recipe.ItemOptions(is));
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
                        this.output = ItemStack.field_190927_a;
                        Utils.log("AutoCrafter: ShapedOreRecipe missing case '" + o.getClass() + "'");
                    }
                }
            } else if (iRecipe instanceof ShapelessRecipes) {
                ShapelessRecipes sr = (ShapelessRecipes)iRecipe;

                for(i = 0; i < sr.field_77579_b.size(); ++i) {
                    ArrayList<ItemStack> ios = new ArrayList();
                    ItemStack[] isa = ((Ingredient)sr.field_77579_b.get(i)).func_193365_a();

                    for(j = 0; j < isa.length; ++j) {
                        ios.add(isa[j]);
                    }

                    this.items.set(i, new Recipe.ItemOptions(ios));
                }
            } else if (iRecipe instanceof ShapelessOreRecipe) {
                ShapelessOreRecipe slor = (ShapelessOreRecipe)iRecipe;

                for(i = 0; i < slor.func_192400_c().size(); ++i) {
                    o = slor.func_192400_c().get(i);
                    if (o == null) {
                        o = ItemStack.field_190927_a;
                    }

                    if (o instanceof List) {
                        this.items.set(i, new Recipe.ItemOptions((List)o));
                    } else if (o instanceof ItemStack) {
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
                        this.output = ItemStack.field_190927_a;
                        Utils.log("AutoCrafter: ShaplessOreRecipe missing case '" + o.getClass() + "'");
                    }
                }
            } else {
                this.output = ItemStack.field_190927_a;
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
            return ItemStack.field_190927_a;
        } else {
            long mod = System.currentTimeMillis() / 500L % (long)opt.opts.size();
            ItemStack is = (ItemStack)opt.opts.get((int)mod);
            return new ItemStack(is.func_77973_b(), 1, is.func_77952_i() == 32767 ? (int)mod % Math.max(1, is.func_77958_k()) : is.func_77952_i());
        }
    }

    public boolean matchesRecipe(int slot, ItemStack itemStack) {
        Recipe.ItemOptions opt = (Recipe.ItemOptions)this.items.get(slot);
        if (opt.opts.size() == 0 && itemStack.func_190926_b()) {
            return true;
        } else {
            Iterator var4 = opt.opts.iterator();

            ItemStack i;
            do {
                if (!var4.hasNext()) {
                    return false;
                }

                i = (ItemStack)var4.next();
                Item item1 = i.func_77973_b();
                Item item2 = itemStack.func_77973_b();
            } while(!matches(i, itemStack));

            return true;
        }
    }

    public NBTTagList getNBT() {
        NBTTagList list = new NBTTagList();
        list.func_74742_a(this.getOutput().serializeNBT());

        for(int i = 0; i < this.items.size(); ++i) {
            NBTTagCompound slot = new NBTTagCompound();
            slot.func_74782_a("item", ((Recipe.ItemOptions)this.items.get(i)).getNBT());
            list.func_74742_a(slot);
        }

        return list;
    }

    public static Recipe fromNBT(NBTTagList tags) {
        Recipe recipe = new Recipe();
        recipe.output = new ItemStack(tags.func_150305_b(0));

        for(int i = 1; i < tags.func_74745_c(); ++i) {
            NBTTagCompound slot = tags.func_150305_b(i);
            Recipe.ItemOptions opts = Recipe.ItemOptions.fromNBT(slot.func_150295_c("item", 10));
            recipe.items.set(i - 1, opts);
        }

        return recipe;
    }

    public void clearRecipe() {
        for(int i = 0; i < this.items.size(); ++i) {
            this.items.set(i, Recipe.ItemOptions.EMPTY);
        }

        this.output = ItemStack.field_190927_a;
    }

    public String toString() {
        String s = "Recipe:\n";

        for(int i = 0; i < this.items.size(); ++i) {
            Recipe.ItemOptions opts = (Recipe.ItemOptions)this.items.get(i);
            s = s + "\ti: " + i + " = {";

            for(int j = 0; j < opts.opts.size(); ++j) {
                s = s + ((ItemStack)opts.opts.get(j)).func_77973_b().getRegistryName().toString() + " : ";
                s = s + ((ItemStack)opts.opts.get(j)).func_77952_i();
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
        if (crafts.func_190926_b() && stack.func_190926_b()) {
            return true;
        } else if (!crafts.func_77973_b().equals(stack.func_77973_b())) {
            return false;
        } else if (crafts.func_77952_i() != 32767 && stack.func_77952_i() != 32767) {
            return crafts.func_77952_i() == stack.func_77952_i();
        } else {
            return true;
        }
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
        NonNullList<ItemStack> ret = NonNullList.func_191197_a(to - from, ItemStack.field_190927_a);

        for(int i = from; i < to; ++i) {
            ret.set(i, ForgeHooks.getContainerItem((ItemStack)inv.get(i)));
        }

        return ret;
    }

    public ItemStack getOutput() {
        return this.output == null ? ItemStack.field_190927_a : this.output.func_77946_l();
    }

    public static class ItemOptions {
        private static final Recipe.ItemOptions EMPTY = new Recipe.ItemOptions();
        NonNullList<ItemStack> opts;

        public ItemOptions() {
            this.opts = NonNullList.func_191196_a();
        }

        public static Recipe.ItemOptions fromNBT(NBTTagList tagList) {
            Recipe.ItemOptions opts = new Recipe.ItemOptions();

            for(int i = 0; i < tagList.func_74745_c(); ++i) {
                NBTTagCompound itemTag = tagList.func_150305_b(i);
                opts.opts.add(new ItemStack(itemTag));
            }

            return opts;
        }

        public NonNullList<ItemStack> getOpts() {
            return this.opts;
        }

        public ItemOptions(ItemStack[] matching) {
            this.opts = NonNullList.func_191196_a();

            for(int j = 0; j < matching.length; ++j) {
                this.opts.add(matching[j]);
            }

        }

        public ItemOptions(OreIngredient i) {
            this(i.func_193365_a());
        }

        public ItemOptions(CompoundIngredient ci) {
            this(ci.func_193365_a());
        }

        public ItemOptions(ItemStack itemStack) {
            this.opts = NonNullList.func_191196_a();
            this.opts.add(itemStack);
        }

        public ItemOptions(NonNullList<ItemStack> opts) {
            this.opts = NonNullList.func_191196_a();
            this.opts = opts;
        }

        public ItemOptions(List<ItemStack> opts) {
            this.opts = NonNullList.func_191196_a();

            for(int i = 0; i < opts.size(); ++i) {
                this.opts.add(opts.get(i));
            }

        }

        public ItemOptions(Ingredient ing) {
            this.opts = NonNullList.func_191196_a();
            ItemStack[] var2 = ing.func_193365_a();
            int var3 = var2.length;

            for(int var4 = 0; var4 < var3; ++var4) {
                ItemStack i = var2[var4];
                this.opts.add(i);
            }

        }

        public NBTTagList getNBT() {
            NBTTagList list = new NBTTagList();

            for(int i = 0; i < this.opts.size(); ++i) {
                list.func_74742_a(((ItemStack)this.opts.get(i)).serializeNBT());
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
