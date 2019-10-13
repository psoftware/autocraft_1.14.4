//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.theincgi.autocrafter;

import com.theincgi.autocrafter.blocks.BlockAutoCrafter;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class BlockHandler {
    public BlockHandler() {
    }

    public static void init() {
        Core.blockAutoCrafter = new BlockAutoCrafter();
        Core.itemAutoCrafter = new ItemBlock(Core.blockAutoCrafter) {
            public String func_77653_i(ItemStack stack) {
                return "Auto Crafter";
            }
        };
        Core.itemAutoCrafter.setRegistryName(Core.blockAutoCrafter.getRegistryName());
    }

    public static void reg() {
    }

    public static void regRends() {
        regRenderer(Core.blockAutoCrafter);
    }

    private static void regRenderer(Block b) {
        Item i = Item.func_150898_a(b);
        Minecraft.func_71410_x().func_175599_af().func_175037_a().func_178086_a(i, 0, new ModelResourceLocation(b.getRegistryName(), "inventory"));
    }

    @EventBusSubscriber(
            modid = "autocrafter"
    )
    public static class RegHandler {
        public RegHandler() {
        }

        @SubscribeEvent
        public static void regBlock(Register<Block> event) {
            System.out.println("Registering autocrafter....");
            event.getRegistry().register(Core.blockAutoCrafter);
        }

        @SubscribeEvent
        public static void regItems(Register<Item> event) {
            event.getRegistry().register(Core.itemAutoCrafter);
        }
    }
}
