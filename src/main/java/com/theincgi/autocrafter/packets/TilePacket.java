package com.theincgi.autocrafter.packets;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;

public abstract class TilePacket {
   BlockPos p;

   public TilePacket() {}

   public TilePacket(BlockPos p) {
      this.p = p;
   }

   // p -> buff
   public void subEncode(PacketBuffer buf)
   {
      buf.writeInt(this.p.getX());
      buf.writeInt(this.p.getY());
      buf.writeInt(this.p.getZ());
   }

   // buff -> p
   public void subDecode(PacketBuffer buf)
   {
      this.p = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
   }
}
