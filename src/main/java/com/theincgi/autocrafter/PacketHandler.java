package com.theincgi.autocrafter;

import com.theincgi.autocrafter.packets.PacketClientChanged;
import com.theincgi.autocrafter.packets.PacketServerUpdated;
import com.theincgi.autocrafter.packets.PacketTileChanged;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

import javax.annotation.Nullable;

public class PacketHandler {
    private static final String PROTOCOL_VERSION = Integer.toString(1);
    private static final SimpleChannel HANDLER = NetworkRegistry.ChannelBuilder
            .named(new ResourceLocation(Core.MODID, "main_channel"))
            .clientAcceptedVersions(PROTOCOL_VERSION::equals)
            .serverAcceptedVersions(PROTOCOL_VERSION::equals)
            .networkProtocolVersion(() -> PROTOCOL_VERSION)
            .simpleChannel();

    public static void register()
    {
        int disc = 0;
        HANDLER.registerMessage(disc++, PacketClientChanged.class, PacketClientChanged::encode, PacketClientChanged::decode, PacketClientChanged.Handler::onMessage);
        HANDLER.registerMessage(disc++, PacketServerUpdated.class, PacketServerUpdated::encode, PacketServerUpdated::decode, PacketServerUpdated.Handler::onMessage);
    }

    public static SimpleChannel getChannel() {
        return HANDLER;
    }

    //public static void sendToAllNearExcept(@Nullable ServerPlayerEntity except, double x, double y, double z, double radius, DimensionType dimension, Object packetIn) {
    public static void sendToAllNearExcept(PacketDistributor.TargetPoint tp,  Object packetIn) {
        for (ServerPlayerEntity player : ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers())
            if (player != tp.except && player.dimension == dimension) {
                double d0 = x - player.posX;
                double d1 = y - player.posY;
                double d2 = z - player.posZ;
                if (d0 * d0 + d1 * d1 + d2 * d2 < radius * radius) {
                    HANDLER.sendTo(packetIn, player.connection.netManager, NetworkDirection.PLAY_TO_CLIENT);
                    HANDLER.se
                }
            }
        }

    }
}
