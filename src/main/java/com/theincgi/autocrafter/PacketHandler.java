package com.theincgi.autocrafter;

import com.theincgi.autocrafter.packets.PacketClientCrafterAction;
import com.theincgi.autocrafter.packets.PacketClientRequestAll;
import com.theincgi.autocrafter.packets.PacketServerCrafterAction;
import com.theincgi.autocrafter.packets.PacketServerRequestAll;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class PacketHandler {
    private static final String PROTOCOL_VERSION = Integer.toString(1);
    private static final SimpleChannel HANDLER = NetworkRegistry.ChannelBuilder
            .named(new ResourceLocation(AutoCrafter.MODID, "main_channel"))
            .clientAcceptedVersions(PROTOCOL_VERSION::equals)
            .serverAcceptedVersions(PROTOCOL_VERSION::equals)
            .networkProtocolVersion(() -> PROTOCOL_VERSION)
            .simpleChannel();

    public static void register()
    {
        int disc = 0;
        HANDLER.registerMessage(disc++, PacketClientRequestAll.class, PacketClientRequestAll::encode, PacketClientRequestAll::decode, PacketClientRequestAll.Handler::onMessage);
        HANDLER.registerMessage(disc++, PacketServerRequestAll.class, PacketServerRequestAll::encode, PacketServerRequestAll::decode, PacketServerRequestAll.Handler::onMessage);
        HANDLER.registerMessage(disc++, PacketClientCrafterAction.class, PacketClientCrafterAction::encode, PacketClientCrafterAction::decode, PacketClientCrafterAction.Handler::onMessage);
        HANDLER.registerMessage(disc++, PacketServerCrafterAction.class, PacketServerCrafterAction::encode, PacketServerCrafterAction::decode, PacketServerCrafterAction.Handler::onMessage);
    }

    public static SimpleChannel getChannel() {
        return HANDLER;
    }

    /* // Not used because it's implemented by forge, but can be useful in future
    public static void sendToAllNearExcept(@Nullable ServerPlayerEntity except, double x, double y, double z, double radius, DimensionType dimension, Object packetIn) {
        for (ServerPlayerEntity player : ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers())
            if (player != except && player.dimension == dimension) {
                double d0 = x - player.posX;
                double d1 = y - player.posY;
                double d2 = z - player.posZ;
                if (d0 * d0 + d1 * d1 + d2 * d2 < radius * radius)
                    HANDLER.sendTo(packetIn, player.connection.netManager, NetworkDirection.PLAY_TO_CLIENT);
            }
    }
     */
}
