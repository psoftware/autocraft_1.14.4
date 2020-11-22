//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.theincgi.autocrafter.packets;

import com.theincgi.autocrafter.tiles.TileAutoCrafter;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketServerRequestAll extends TilePacket {
    public CompoundNBT autocrafterNbt;

    private PacketServerRequestAll() {
    }

    ;

    public PacketServerRequestAll(BlockPos p, CompoundNBT crafterNbt) {
        super(p);
        this.autocrafterNbt = crafterNbt;
    }

    public static void encode(PacketServerRequestAll pkt, PacketBuffer buf) {
        pkt.subEncode(buf);
    }

    public static PacketServerRequestAll decode(PacketBuffer buf) {
        PacketServerRequestAll packet = new PacketServerRequestAll();
        packet.subDecode(buf);
        return packet;
    }

    @Override
    public void subEncode(PacketBuffer buf) {
        super.subEncode(buf);
        buf.writeCompoundTag(this.autocrafterNbt);
    }

    @Override
    public void subDecode(PacketBuffer buf) {
        super.subDecode(buf);
        autocrafterNbt = buf.readCompoundTag();
    }

    public static class Handler {
        public static void onMessage(final PacketServerRequestAll message, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> {
                // handled on client side
                World w = Minecraft.getInstance().player.world;
                TileAutoCrafter ourTile = (TileAutoCrafter) w.getTileEntity(message.p);
                ourTile.read(w.getBlockState(message.p), message.autocrafterNbt);
            });
            ctx.get().setPacketHandled(true);
        }
    }
}
