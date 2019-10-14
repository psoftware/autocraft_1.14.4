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
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;

@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
public class BlockRegistrationHandler {
    /* Model is loaded automatically for 1.13/1.14
    private static void regRenderer(Block block) {
        Item i = block.asItem();
        Minecraft.getInstance().func_175599_af().func_175037_a().func_178086_a(i, 0, new ModelResourceLocation(block.getRegistryName(), "inventory"));
    }*/

    @SubscribeEvent
    public static void registerTE(RegistryEvent.Register<TileEntityType<?>> event) {
        Core.tileTypeAutoCraft = TileEntityType.Builder.create(TileAutoCrafter::new, Core.blockAutoCrafter).build(null)
                .setRegistryName(Core.MODID, "testblocktileentity");
        event.getRegistry().register(Core.tileTypeAutoCraft);
    }

    @SubscribeEvent
    public static void regBlock(Register<Block> event) {
        System.out.println("Registering autocrafter....");
        Core.blockAutoCrafter = new BlockAutoCrafter();
        Core.blockAutoCrafter.setRegistryName("autocrafter");
        event.getRegistry().register(Core.blockAutoCrafter);
    }

    @SubscribeEvent
    public static void regItems(Register<Item> event) {
        Core.itemAutoCrafter = new BlockItem(Core.blockAutoCrafter, new Item.Properties().group(ItemGroup.DECORATIONS));
        // registry name always not null (if block has been correctly inserted) because
        // block registration event is fired always before item registration event (forge docs)
        Core.itemAutoCrafter.setRegistryName(Core.blockAutoCrafter.getRegistryName());
        event.getRegistry().register(Core.itemAutoCrafter);
    }

    @SubscribeEvent
    public static void serverStarting(FMLServerStartingEvent evt)
    {

    }

    @SubscribeEvent
    public static void worldLoadEvent(WorldEvent.Load event) {
        // Set renderer only on client side

    }

    @SubscribeEvent //PreInit
    public static void commonSetup(FMLCommonSetupEvent event) {
        //proxy.commonSetup();
    }
}
