package com.theincgi.autocrafter.packets;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public abstract class TilePacket implements IMessage {

   BlockPos p;


   public TilePacket() {}

   public TilePacket(BlockPos p) {
      this.p = p;
   }

   public void fromBytes(ByteBuf buf) {
      this.p = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
   }

   public void toBytes(ByteBuf buf) {
      buf.writeInt(this.p.getX());
      buf.writeInt(this.p.getY());
      buf.writeInt(this.p.getZ());
   }
}
