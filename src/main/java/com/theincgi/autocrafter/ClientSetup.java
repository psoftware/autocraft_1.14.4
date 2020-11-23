package com.theincgi.autocrafter;


import com.theincgi.autocrafter.gui.GuiAutoCrafter;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = AutoCrafter.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetup {

    public static void init(final FMLClientSetupEvent event)
    {
        ScreenManager.registerFactory(AutoCrafter.CONTAINER_AUTOCRAFTER.get(), GuiAutoCrafter::new);
//        RenderTypeLookup.setRenderLayer(AutoCrafter.BLOCK_AUTOCRAFTER.get(), (RenderType) -> true);
        ClientRegistry.bindTileEntityRenderer(AutoCrafter.TILE_AUTOCRAFTER.get(), RendererAutoCrafter::new);
    }

    @SubscribeEvent
    public void onTooltipPre(RenderTooltipEvent.Pre event)
    {
        Item item = event.getStack().getItem();
        if (item.getRegistryName().getNamespace().equals(AutoCrafter.MODID))
        {
            event.setMaxWidth(200);
        }
    }
}
