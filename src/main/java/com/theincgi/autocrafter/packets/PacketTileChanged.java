package com.theincgi.autocrafter.packets;

import com.theincgi.autocrafter.packets.TilePacket;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public abstract class PacketTileChanged extends TilePacket {

   CompoundNBT nbt;


   public PacketTileChanged() {}

   public PacketTileChanged(BlockPos p, CompoundNBT nbt) {
      super(p);
      this.nbt = nbt;
   }

   public void fromBytes(ByteBuf buf) {
      super.fromBytes(buf);
      this.nbt = ByteBufUtils.readTag(buf);
   }

   public void toBytes(ByteBuf buf) {
      super.toBytes(buf);
      ByteBufUtils.writeTag(buf, this.nbt);
   }
}
