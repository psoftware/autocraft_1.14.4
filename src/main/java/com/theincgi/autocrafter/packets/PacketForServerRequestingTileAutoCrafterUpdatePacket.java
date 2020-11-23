package com.theincgi.autocrafter.packets;

import com.theincgi.autocrafter.tiles.TileAutoCrafter;
import net.minecraft.block.BlockState;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketForServerRequestingTileAutoCrafterUpdatePacket extends TilePacket {
    public PacketForServerRequestingTileAutoCrafterUpdatePacket()
    {
    }

    public PacketForServerRequestingTileAutoCrafterUpdatePacket(BlockPos p)
    {
        super(p);
    }

    public static void encode(PacketForServerRequestingTileAutoCrafterUpdatePacket pkt, PacketBuffer buf)
    {
        pkt.subEncode(buf);
    }

    public static PacketForServerRequestingTileAutoCrafterUpdatePacket decode(PacketBuffer buf)
    {
        PacketForServerRequestingTileAutoCrafterUpdatePacket packet = new PacketForServerRequestingTileAutoCrafterUpdatePacket();
        packet.subDecode(buf);
        return packet;
    }

    @Override
    public void subEncode(PacketBuffer buf)
    {
        super.subEncode(buf);
    }

    @Override
    public void subDecode(PacketBuffer buf)
    {
        super.subDecode(buf);
    }

    public static PacketForServerRequestingTileAutoCrafterUpdatePacket requestAll(BlockPos pos)
    {
        return new PacketForServerRequestingTileAutoCrafterUpdatePacket(pos);
    }

    public static class Handler {
        public static void onMessage(final PacketForServerRequestingTileAutoCrafterUpdatePacket message, Supplier<NetworkEvent.Context> ctx)
        {
            ctx.get().enqueueWork(() -> {
                // handled by server
                TileAutoCrafter tac = (TileAutoCrafter) ctx.get().getSender().getServerWorld().getTileEntity(message.p);

                // Send message to players in tile entity radius
                BlockState blockState = tac.getBlockState();
                tac.getWorld().notifyBlockUpdate(tac.getPos(), blockState, blockState, 3);

//                PacketDistributor.PacketTarget ptarget = PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(
//                    message.p.getX(), message.p.getY(), message.p.getZ(), 32.0D,
//                    ctx.get().getSender().getEntityWorld().getDimensionKey())
//                );
//                PacketHandler.getChannel().send(ptarget, new PacketForClientToUpdateTileAutoCrafter(message.p, tac.serializeNBT()));
            });
            ctx.get().setPacketHandled(true);
        }
    }
}