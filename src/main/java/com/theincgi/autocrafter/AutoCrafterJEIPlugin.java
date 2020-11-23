package com.theincgi.autocrafter;


import com.theincgi.autocrafter.containers.ContainerAutoCrafter;
import com.theincgi.autocrafter.gui.GuiAutoCrafter;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaRecipeCategoryUid;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.IRecipeTransferRegistration;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

@JeiPlugin
public class AutoCrafterJEIPlugin implements IModPlugin {
    @Override
    public ResourceLocation getPluginUid()
    {
        return new ResourceLocation(AutoCrafter.MODID, AutoCrafter.MODID);
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration)
    {
        IModPlugin.super.registerRecipes(registration);
//        registration.addIngredientInfo(new ItemStack(AutoCrafter.CRAFTER_ITEM.get()), VanillaTypes.ITEM,
        registration.addIngredientInfo(new ItemStack(AutoCrafter.ITEM_AUTOCRAFTER.get()), VanillaTypes.ITEM,
            I18n.format("tile.autocrafter.autocrafter.jei.1"),
            I18n.format("tile.autocrafter.autocrafter.jei.2"),
            I18n.format("tile.autocrafter.autocrafter.jei.3"),
            I18n.format("tile.autocrafter.autocrafter.jei.4"));
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration)
    {
        registration.addRecipeCatalyst(new ItemStack(AutoCrafter.ITEM_AUTOCRAFTER.get()), VanillaRecipeCategoryUid.CRAFTING);
    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration)
    {
        IModPlugin.super.registerRecipeTransferHandlers(registration);
        registration.addRecipeTransferHandler(ContainerAutoCrafter.class, VanillaRecipeCategoryUid.CRAFTING, 36, 9, 0, 36);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration)
    {
        registration.addRecipeClickArea(GuiAutoCrafter.class, 88, 32, 28, 23, VanillaRecipeCategoryUid.CRAFTING);
    }
}
