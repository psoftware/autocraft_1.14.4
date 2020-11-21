package com.theincgi.autocrafter.packets;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;

public abstract class TilePacket {
    BlockPos p;

    public TilePacket() {
    }

    public TilePacket(BlockPos p) {
        this.p = p;
    }

    // p -> buff
    public void subEncode(PacketBuffer buf) {
        buf.writeBlockPos(this.p);
    }

    // buff -> p
    public void subDecode(PacketBuffer buf) {
        this.p = buf.readBlockPos();
    }
}
