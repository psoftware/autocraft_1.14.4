package com.theincgi.autocrafter;

import com.theincgi.autocrafter.blocks.BlockAutoCrafter;
import com.theincgi.autocrafter.container.ContainerAutoCrafter;
import com.theincgi.autocrafter.gui.GuiAutoCrafter;
import com.theincgi.autocrafter.proxy.ClientProxy;
import com.theincgi.autocrafter.proxy.CommonProxy;
import com.theincgi.autocrafter.proxy.ServerProxy;
import com.theincgi.autocrafter.tileEntity.TileAutoCrafter;
import net.minecraft.block.Block;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;

import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


@Mod(AutoCrafter.MODID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class AutoCrafter {
   public static CommonProxy proxy = DistExecutor.runForDist(() -> ClientProxy::new, () -> ServerProxy::new);
   public static final String MODID = "autocrafter";
   public static final String VERSION = "4.3";

   public static Logger LOGGER = LogManager.getLogger(MODID);

   public static BlockAutoCrafter blockAutoCrafter;
   public static BlockItem itemAutoCrafter;
   public static TileEntityType<?> tileTypeAutoCraft;
   public static ContainerType<ContainerAutoCrafter> containerAutoCraft;

   public AutoCrafter() { }

   @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
   public static class BlockRegistrationHandler {
       /* Model is loaded automatically for 1.13/1.14
       private static void regRenderer(Block block) {
           Item i = block.asItem();
           Minecraft.getInstance().func_175599_af().func_175037_a().func_178086_a(i, 0, new ModelResourceLocation(block.getRegistryName(), "inventory"));
       }*/

      @SubscribeEvent
      public static void registerContainer(RegistryEvent.Register<ContainerType<?>> event){
         AutoCrafter.containerAutoCraft =
                 (ContainerType<ContainerAutoCrafter>) (IForgeContainerType.create(ContainerAutoCrafter::new)
                         .setRegistryName(AutoCrafter.MODID, "autocrafter"));
         event.getRegistry().register(AutoCrafter.containerAutoCraft);
         ScreenManager.registerFactory(AutoCrafter.containerAutoCraft, GuiAutoCrafter::new);
      }

      @SubscribeEvent
      public static void registerTE(RegistryEvent.Register<TileEntityType<?>> event) {
         AutoCrafter.tileTypeAutoCraft = TileEntityType.Builder.create(TileAutoCrafter::new, AutoCrafter.blockAutoCrafter).build(null)
                 .setRegistryName(AutoCrafter.MODID, "autocrafter");
         event.getRegistry().register(AutoCrafter.tileTypeAutoCraft);
      }

      @SubscribeEvent
      public static void regBlock(RegistryEvent.Register<Block> event) {
         LOGGER.info("Registering autocrafter....");
         AutoCrafter.blockAutoCrafter = new BlockAutoCrafter();
         AutoCrafter.blockAutoCrafter.setRegistryName(AutoCrafter.MODID, "autocrafter");
         event.getRegistry().register(AutoCrafter.blockAutoCrafter);
      }

      @SubscribeEvent
      public static void regItems(RegistryEvent.Register<Item> event) {
         AutoCrafter.itemAutoCrafter = new BlockItem(AutoCrafter.blockAutoCrafter, new Item.Properties().group(ItemGroup.DECORATIONS));
         // registry name always not null (if block has been correctly inserted) because
         // block registration event is fired always before item registration event (forge docs)
         AutoCrafter.itemAutoCrafter.setRegistryName(AutoCrafter.MODID, "autocrafter");
         event.getRegistry().register(AutoCrafter.itemAutoCrafter);
      }

      @SubscribeEvent //PreInit
      public static void commonSetup(FMLCommonSetupEvent event) {
         //proxy.commonSetup();
         PacketHandler.register();
      }
   }
}
