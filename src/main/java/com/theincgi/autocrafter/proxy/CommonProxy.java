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
  /* TODO: port all events
   public void init() {
      // See this https://gist.github.com/williewillus/353c872bcf1a6ace9921189f6100d09a#gistcomment-2870154
      NetworkRegistry.INSTANCE.registerGuiHandler(Core.instance, new GuiHandler());
      PacketClientChanged.Handler clientHandler = new PacketClientChanged.Handler();
      PacketServerUpdated.Handler serverHandler = new PacketServerUpdated.Handler();
      Core.network = NetworkRegistry.INSTANCE.newSimpleChannel("autocrafter");
      Core.network.registerMessage(clientHandler, PacketClientChanged.class, 0, Side.SERVER);
      Core.network.registerMessage(serverHandler, PacketServerUpdated.class, 1, Side.CLIENT);
   }
   */

   public boolean isClient() {
      return this instanceof ClientProxy;
   }

   public boolean isServer() {
      return this instanceof ServerProxy;
   }

   public void sendPacketServer(IMessage packetTargetChanged) {}
}
