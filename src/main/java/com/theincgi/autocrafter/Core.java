package com.theincgi.autocrafter;

import com.theincgi.autocrafter.blocks.BlockAutoCrafter;
import com.theincgi.autocrafter.proxy.ClientProxy;
import com.theincgi.autocrafter.proxy.CommonProxy;
import com.theincgi.autocrafter.proxy.ServerProxy;
import com.theincgi.autocrafter.tileEntity.TileAutoCrafter;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;

import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;


@Mod(Core.MODID)
public class Core {
   public static CommonProxy proxy = DistExecutor.runForDist(() -> ClientProxy::new, () -> ServerProxy::new);
   public static final String MODID = "autocrafter";
   public static final String VERSION = "4.3";

   public static BlockAutoCrafter blockAutoCrafter;
   public static BlockItem itemAutoCrafter;
   public static TileEntityType<?> tileTypeAutoCraft;

   public Core() {
      MinecraftForge.EVENT_BUS.register(BlockRegistrationHandler.class);
   }

   @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
   public static class BlockRegistrationHandler {
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
      public static void regBlock(RegistryEvent.Register<Block> event) {
         System.out.println("Registering autocrafter....");
         Core.blockAutoCrafter = new BlockAutoCrafter();
         Core.blockAutoCrafter.setRegistryName("autocrafter");
         event.getRegistry().register(Core.blockAutoCrafter);
      }

      @SubscribeEvent
      public static void regItems(RegistryEvent.Register<Item> event) {
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
         PacketHandler.register();
      }
   }
}
