//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.theincgi.autocrafter.packets;

import com.theincgi.autocrafter.Recipe;
import com.theincgi.autocrafter.tileEntity.TileAutoCrafter;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketServerUpdated extends PacketTileChanged {
    public PacketServerUpdated() {
    }

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
    }

    @Override
    public void subDecode(PacketBuffer buf) {
        super.subDecode(buf);
    }

    public PacketServerUpdated(BlockPos p, CompoundNBT nbt) {
        super(p, nbt);
    }

    public static class Handler {
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
    }
}
