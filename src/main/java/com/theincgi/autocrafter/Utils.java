//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.theincgi.autocrafter;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.ITextComponent.Serializer;

public class Utils {
    public Utils() {
    }

    public static void log(String s) {
        Minecraft.func_71410_x().field_71456_v.func_146158_b().func_146227_a(Serializer.func_150699_a("{\"text\":\"" + s + "\"}"));
    }

    public static ITextComponent IText(String s) {
        return Serializer.func_150699_a("{\"text\":\"" + s + "\"}");
    }

    public static List<IRecipe> getValid(final ItemStack sItem) {
        final ArrayList<IRecipe> out = new ArrayList();
        Consumer<IRecipe> c = new Consumer<IRecipe>() {
            public void accept(IRecipe iRecipe) {
                ItemStack is = iRecipe.func_77571_b();
                if (sItem.func_77969_a(is)) {
                    out.add(iRecipe);
                }

            }
        };
        CraftingManager.field_193380_a.forEach(c);
        return out;
    }

    public static String itemStackToString(ItemStack temp) {
        return temp.func_77973_b().getRegistryName().toString() + ":" + temp.func_77952_i() + " x" + temp.func_190916_E();
    }
}
