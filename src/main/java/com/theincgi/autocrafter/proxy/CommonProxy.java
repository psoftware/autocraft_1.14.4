package com.theincgi.autocrafter.proxy;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.theincgi.autocrafter.BlockHandler;
import com.theincgi.autocrafter.Core;
import com.theincgi.autocrafter.GuiHandler;
import com.theincgi.autocrafter.blocks.BlockAutoCrafter;
import com.theincgi.autocrafter.packets.PacketClientChanged;
import com.theincgi.autocrafter.packets.PacketServerUpdated;
import com.theincgi.autocrafter.proxy.ClientProxy;
import com.theincgi.autocrafter.proxy.ServerProxy;
import com.theincgi.autocrafter.tileEntity.TileAutoCrafter;
import net.minecraft.block.Block;
import net.minecraft.command.CommandSource;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.relauncher.Side;

public class CommonProxy {


   public static final BlockAutoCrafter TESTBLOCK_TYPE = new BlockAutoCrafter();
   public static final TileEntityType<?> TESTTILEENTITY_TYPE =
           TileEntityType.Builder.create(TileAutoCrafter::new, TESTBLOCK_TYPE).build(null)
                   .setRegistryName(Core.MODID, "testblocktileentity");//todo

  /* public void preInit(FMLPreInitializationEvent e) {
      BlockHandler.init();
      BlockHandler.reg();
      MinecraftForge.EVENT_BUS.register(new BlockHandler.RegHandler());
      GameRegistry.registerTileEntity(TileAutoCrafter.class, "com.theincgi.autocrafter.tileentity");
   }

   public void init() {
      BlockAutoCrafter.addRecipe();
      NetworkRegistry.INSTANCE.registerGuiHandler(Core.instance, new GuiHandler());
      PacketClientChanged.Handler clientHandler = new PacketClientChanged.Handler();
      PacketServerUpdated.Handler serverHandler = new PacketServerUpdated.Handler();
      Core.network = NetworkRegistry.INSTANCE.newSimpleChannel("autocrafter");
      Core.network.registerMessage(clientHandler, PacketClientChanged.class, 0, Side.SERVER);
      Core.network.registerMessage(serverHandler, PacketServerUpdated.class, 1, Side.CLIENT);
   }

   //public void postInit(FMLPostInitializationEvent e) {}*/

   public boolean isClient() {
      return this instanceof ClientProxy;
   }

   public boolean isServer() {
      return this instanceof ServerProxy;
   }

   public void sendPacketServer(IMessage packetTargetChanged) {}


   @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
   public static class ForgeEventHandler {
      @SubscribeEvent
      public static void registerTE(RegistryEvent.Register<TileEntityType<?>> event) {
         event.getRegistry().register(TESTTILEENTITY_TYPE);
      }

      @SubscribeEvent
      public static void registerBlock(RegistryEvent.Register<Block> event) {
         event.getRegistry().register(TESTBLOCK_TYPE);
      }

      @SubscribeEvent
      public static void registerItems(RegistryEvent.Register<Item> event) {
         event.getRegistry().registerAll(new BlockItem(TESTBLOCK_TYPE, (new Item.Properties()).group(ItemGroup.DECORATIONS))
                 .setRegistryName("testblockitem"));
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
         proxy.commonSetup();
      }
   }

}
