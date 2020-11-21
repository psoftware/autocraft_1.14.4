package com.theincgi.autocrafter.packets;

import com.theincgi.autocrafter.PacketHandler;
import com.theincgi.autocrafter.tileEntity.TileAutoCrafter;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.function.Supplier;

public class PacketClientRequestAll extends TilePacket {
    public PacketClientRequestAll() {}
    public PacketClientRequestAll(BlockPos p) {
        super(p);
    }

    public static void encode(PacketClientRequestAll pkt, PacketBuffer buf) {
        pkt.subEncode(buf);
    }

    public static PacketClientRequestAll decode(PacketBuffer buf) {
        PacketClientRequestAll packet = new PacketClientRequestAll();
        packet.subDecode(buf);
        return packet;
    }

    @Override
    public void subEncode(PacketBuffer buf) {
        super.subEncode(buf);
    }

    @Override
    public void subDecode(PacketBuffer buf) {
        super.subDecode(buf);
    }

    public static PacketClientRequestAll requestAll(BlockPos pos) {
        return new PacketClientRequestAll(pos);
    }

    public static class Handler {
        public static void onMessage(final PacketClientRequestAll message, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> {
                TileAutoCrafter tac = (TileAutoCrafter)ctx.get().getSender().getServerWorld().getTileEntity(message.p);

                // Send message to players in tile entity radius
                PacketDistributor.PacketTarget ptarget = PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(
                        message.p.getX(), message.p.getY(), message.p.getZ(), 8.0D,
                        ctx.get().getSender().getEntityWorld().getDimensionKey())
                );
                PacketHandler.getChannel().send(ptarget, new PacketServerRequestAll(message.p, tac.serializeNBT()));
            });
            ctx.get().setPacketHandled(true);
        }
    }
}