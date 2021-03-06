package com.theincgi.autocrafter.proxy;

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
}
