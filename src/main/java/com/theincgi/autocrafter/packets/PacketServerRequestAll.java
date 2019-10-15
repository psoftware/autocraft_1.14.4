//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.theincgi.autocrafter.packets;

import com.theincgi.autocrafter.tileEntity.TileAutoCrafter;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketServerRequestAll extends TilePacket {
    public CompoundNBT autocrafterNbt;

    private PacketServerRequestAll() {};

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
                TileAutoCrafter ourTile = (TileAutoCrafter)Minecraft.getInstance().player.world.getTileEntity(message.p);
                ourTile.read(message.autocrafterNbt);
            });
            ctx.get().setPacketHandled(true);
        }
    }

    /*public static class Handler {
        public static void onMessage(final PacketServerUpdated message, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> {
                TileAutoCrafter ourTile = (TileAutoCrafter)Minecraft.getInstance().player.world.getTileEntity(message.p);
                if (message.nbt.contains("tile")) {
                    ourTile.read(message.nbt.getCompound("tile"));
                } else {
                    if (message.nbt.contains("targetSlot")) {
                        ItemStack newTarget =  ItemStack.read(message.nbt.getCompound("targetSlot"));
                        if (!Recipe.matches(ourTile.getCrafts(), newTarget)) {
                            ourTile.setCrafts(newTarget);
                        }
                    }

                    if (message.nbt.contains("recipe")) {
                        assert ourTile != null;
                        ourTile.setRecipe(message.nbt.getList("recipe", 10));
                    }

                    if (message.nbt.contains("rIndex")) {
                        ourTile.setCurrentRecipeIndex(message.nbt.getInt("rIndex"));
                    }

                }
            });
            ctx.get().setPacketHandled(true);
        }
    }*/
}
