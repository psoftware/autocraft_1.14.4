package com.theincgi.autocrafter.packets;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public abstract class PacketTileChanged extends TilePacket {
   CompoundNBT nbt;

   public PacketTileChanged() {}

   public static void encode(PacketServerUpdated pkt, PacketBuffer buf) {
      pkt.subEncode(buf);
   }

   public static PacketServerUpdated decode(PacketBuffer buf) {
      PacketServerUpdated packet = new PacketServerUpdated();
      packet.subDecode(buf);
      return packet;
   }

   @Override
   public void subEncode(PacketBuffer buf) {
      super.subEncode(buf);
      buf.writeCompoundTag(this.nbt);
   }

   @Override
   public void subDecode(PacketBuffer buf) {
      super.subDecode(buf);
      this.nbt = buf.readCompoundTag();
   }

   public PacketTileChanged(BlockPos p, CompoundNBT nbt) {
      super(p);
      this.nbt = nbt;
   }
}
