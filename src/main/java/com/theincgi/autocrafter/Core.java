package com.theincgi.autocrafter;

import com.theincgi.autocrafter.blocks.BlockAutoCrafter;
import com.theincgi.autocrafter.proxy.ClientProxy;
import com.theincgi.autocrafter.proxy.CommonProxy;
import com.theincgi.autocrafter.proxy.ServerProxy;
import net.minecraft.item.BlockItem;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;


import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;


@Mod(Core.MODID)
public class Core {

   public static CommonProxy proxy = DistExecutor.runForDist(() -> ClientProxy::new, () -> ServerProxy::new);
   public static final String MODID = "autocrafter";
   public static final String VERSION = "4.3";

   @Instance
   public static Core instance;

   public static BlockAutoCrafter blockAutoCrafter;
   public static BlockItem itemAutoCrafter;
   public static TileEntityType<?> tileTypeAutoCraft;

   public static SimpleNetworkWrapper network;

   @EventHandler
   public void preInit(FMLPreInitializationEvent e) {
      proxy.preInit(e);
   }

   @EventHandler
   public void init(FMLInitializationEvent e) {
      proxy.init(e);
   }

   @EventHandler
   public void postInit(FMLPostInitializationEvent e) {
      proxy.postInit(e);
   }
}
