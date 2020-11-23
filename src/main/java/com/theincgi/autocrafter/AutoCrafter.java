package com.theincgi.autocrafter;

import com.theincgi.autocrafter.blocks.BlockAutoCrafter;
import com.theincgi.autocrafter.containers.ContainerAutoCrafter;
import com.theincgi.autocrafter.tiles.TileAutoCrafter;
import net.minecraft.block.Block;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;

import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;


@Mod(AutoCrafter.MODID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class AutoCrafter {
    public static final String MODID = "autocrafter";
    public static final String VERSION = "5";
    public static final String REGISTRY_NAME = "autocrafter";

    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    private static final DeferredRegister<TileEntityType<?>> TILES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, MODID);
    private static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, MODID);

    public static final RegistryObject<BlockAutoCrafter> BLOCK_AUTOCRAFTER = BLOCKS.register(REGISTRY_NAME,
        BlockAutoCrafter::new
    );

    public static final RegistryObject<Item> ITEM_AUTOCRAFTER = ITEMS.register(REGISTRY_NAME,
        () -> new BlockItem(BLOCK_AUTOCRAFTER.get(), new Item.Properties().group(ItemGroup.REDSTONE))
    );

    public static final RegistryObject<TileEntityType<TileAutoCrafter>> TILE_AUTOCRAFTER = TILES.register(REGISTRY_NAME,
        () -> TileEntityType.Builder.create(
            TileAutoCrafter::new, BLOCK_AUTOCRAFTER.get()
        ).build(null)
    );

    public static final RegistryObject<ContainerType<ContainerAutoCrafter>> CONTAINER_AUTOCRAFTER = CONTAINERS.register(REGISTRY_NAME,
        () -> IForgeContainerType.create(ContainerAutoCrafter::new)
    );

    public AutoCrafter()
    {
        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        TILES.register(modEventBus);
        CONTAINERS.register(modEventBus);

        modEventBus.addListener(ClientSetup::init);
        PacketHandler.register();
    }
}
