package com.theincgi.autocrafter.proxy;

import com.theincgi.autocrafter.BlockHandler;
import com.theincgi.autocrafter.Core;
import com.theincgi.autocrafter.proxy.CommonProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class ClientProxy extends CommonProxy {

   public void preInit(FMLPreInitializationEvent e) {
      super.preInit(e);
   }

   public void init(FMLInitializationEvent e) {
      super.init(e);
      BlockHandler.regRends();
   }

   public void postInit(FMLPostInitializationEvent e) {
      super.postInit(e);
   }

   public void sendPacketServer(IMessage packetTargetChanged) {
      Core.network.sendToServer(packetTargetChanged);
   }
}
