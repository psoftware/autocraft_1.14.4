//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.theincgi.autocrafter;

import com.theincgi.autocrafter.blocks.BlockAutoCrafter;
import com.theincgi.autocrafter.tileEntity.TileAutoCrafter;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

public class BlockHandler {
    public BlockHandler() {
    }

    public static void init() {
        Core.blockAutoCrafter = new BlockAutoCrafter();
        Core.itemAutoCrafter = new BlockItem(Core.blockAutoCrafter, new Item.Properties().group(ItemGroup.DECORATIONS));
        // registry name always not null (if block has been correctly inserted) because
        // block registration event is fired always before item event (forge docs)
        Core.itemAutoCrafter.setRegistryName(Core.blockAutoCrafter.getRegistryName());
        Core.tileTypeAutoCraft = TileEntityType.Builder.create(TileAutoCrafter::new, Core.blockAutoCrafter).build(null)
                .setRegistryName(Core.MODID, "testblocktileentity");
    }

    // Model is loaded automatically for 1.14
    /*
    private static void regRenderer(Block block) {
        Item i = block.asItem();
        Minecraft.getInstance().func_175599_af().func_175037_a().func_178086_a(i, 0, new ModelResourceLocation(block.getRegistryName(), "inventory"));
    }*/

    @EventBusSubscriber(modid = Core.MODID)
    public static class RegHandler {
        public RegHandler() {
        }

        @SubscribeEvent
        public static void registerTE(RegistryEvent.Register<TileEntityType<?>> event) {
            event.getRegistry().register(Core.tileTypeAutoCraft);
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
