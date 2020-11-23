//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.theincgi.autocrafter.packets;

import com.theincgi.autocrafter.Utils;
import com.theincgi.autocrafter.tiles.TileAutoCrafter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketForClientToUpdateTileAutoCrafter extends TilePacket {
    public CompoundNBT autocrafterNbt;

    private PacketForClientToUpdateTileAutoCrafter()
    {
    }

    public PacketForClientToUpdateTileAutoCrafter(BlockPos p, CompoundNBT crafterNbt)
    {
        super(p);
        this.autocrafterNbt = crafterNbt;
    }

    public static void encode(PacketForClientToUpdateTileAutoCrafter pkt, PacketBuffer buf)
    {
        pkt.subEncode(buf);
    }

    public static PacketForClientToUpdateTileAutoCrafter decode(PacketBuffer buf)
    {
        PacketForClientToUpdateTileAutoCrafter packet = new PacketForClientToUpdateTileAutoCrafter();
        packet.subDecode(buf);
        return packet;
    }

    @Override
    public void subEncode(PacketBuffer buf)
    {
        super.subEncode(buf);
        buf.writeCompoundTag(this.autocrafterNbt);
    }

    @Override
    public void subDecode(PacketBuffer buf)
    {
        super.subDecode(buf);
        autocrafterNbt = buf.readCompoundTag();
    }

    public static class Handler {
        public static void onMessage(final PacketForClientToUpdateTileAutoCrafter message, Supplier<NetworkEvent.Context> ctx)
        {
            if (!FMLEnvironment.dist.isClient())
            {
                return;
            }
            ctx.get().enqueueWork(() -> {
                // handled on client side
                Utils.log("received autocrafter update packet at position " + message.p.toString());
                ClientPlayerEntity player = Minecraft.getInstance().player;
                if (player == null)
                {
                    Utils.log("received packed, but player is null");
                    return;
                }
                World w = player.getEntityWorld();
                TileAutoCrafter ourTile = (TileAutoCrafter) w.getTileEntity(message.p);
                if (ourTile == null)
                {
                    Utils.log("tile at " + message.p.toString() + " is null on client :/ wtf?");
                    return;
                }
                ourTile.read(w.getBlockState(message.p), message.autocrafterNbt);
            });
            ctx.get().setPacketHandled(true);
        }
    }
}
