//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.theincgi.autocrafter;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Utils {
    public Utils()
    {
    }

    public static void log(String s)
    {
        Minecraft.getInstance().ingameGUI.getChatGUI().printChatMessage(new StringTextComponent(System.currentTimeMillis() + " |> " + s));
    }

    public static ITextComponent IText(String s)
    {
        return new StringTextComponent(s);
    }

    public static List<IRecipe> getValid(World world, final ItemStack sItem)
    {
        final ArrayList<IRecipe> out = new ArrayList<>();
        Consumer<IRecipe> c = new Consumer<IRecipe>() {
            public void accept(IRecipe iRecipe)
            {
                ItemStack is = iRecipe.getRecipeOutput();
                if (sItem.isItemEqual(is) && iRecipe.getType() == IRecipeType.CRAFTING)
                {
                    out.add(iRecipe);
                }
            }
        };

        world.getRecipeManager().getRecipes().forEach(c);

        return out;
    }

    public static String itemStackToString(ItemStack temp)
    {
        return temp.getItem().getRegistryName().toString() + ":" + temp.getDamage() + " x" + temp.getCount();
    }
}
