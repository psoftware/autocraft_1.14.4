
package com.theincgi.autocrafter.packets;

import com.theincgi.autocrafter.Core;
import com.theincgi.autocrafter.PacketHandler;
import com.theincgi.autocrafter.tileEntity.TileAutoCrafter;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.PacketDistributor.TargetPoint;

import java.util.function.Supplier;

public class PacketClientChanged extends PacketTileChanged {
    public PacketClientChanged() {
    }

    public PacketClientChanged(BlockPos p, CompoundNBT nbt) {
        super(p, nbt);
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
        buf.writeCompoundTag(this.nbt);
    }

    @Override
    public void subDecode(PacketBuffer buf) {
        super.subDecode(buf);
        this.nbt = buf.readCompoundTag();
    }

    // MsgFormat: {TargetChanged, next}
    public static PacketClientChanged nextRecipe(BlockPos pos) {
        CompoundNBT chng = new CompoundNBT();
        chng.putBoolean("next", true);

        CompoundNBT tag = new CompoundNBT();
        tag.put("targetChanged", chng);

        return new PacketClientChanged(pos, tag);
    }

    // MsgFormat: {TargetChanged, prev}
    public static PacketClientChanged prevRecipe(BlockPos pos) {
        CompoundNBT chng = new CompoundNBT();
        chng.putBoolean("prev", true);

        CompoundNBT tag = new CompoundNBT();
        tag.put("targetChanged", chng);

        return new PacketClientChanged(pos, tag);
    }

    // MsgFormat: {TargetChanged, item}
    public static PacketClientChanged targetChanged(BlockPos pos, ItemStack heldItem) {
        CompoundNBT tag2 = new CompoundNBT();
        tag2.put("item", heldItem.serializeNBT());

        CompoundNBT tag = new CompoundNBT();
        tag.put("targetChanged", tag2);

        return new PacketClientChanged(pos, tag);
    }

    // MsgFormat: {getAll}
    public static PacketClientChanged requestAll(BlockPos pos) {
        CompoundNBT tag = new CompoundNBT();
        tag.putBoolean("getAll", true);
        return new PacketClientChanged(pos, tag);
    }

    public static class Handler {
        public void onMessage(final PacketClientChanged message, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> {
                TileAutoCrafter tac = (TileAutoCrafter)ctx.get().getSender().getServerWorld().getTileEntity(message.p);
                CompoundNBT response = new CompoundNBT();
                if (message.nbt.contains("targetChanged")) {
                    CompoundNBT how = message.nbt.getCompound("targetChanged");
                    if (how.contains("next")) {
                        tac.nextRecipe();
                    } else if (how.contains("prev")) {
                        tac.prevRecipe();
                    } else if (how.contains("item")) {
                        tac.updateRecipes(ItemStack.read(how.getCompound("item")), 0);
                    } else {
                        System.err.println("Unhandled case in PacketClientChanged");
                    }

                    response.put("recipe", tac.getRecipe().getNBT());
                    response.put("targetSlot", tac.getCrafts().serializeNBT());
                    response.putInt("rIndex", tac.getCurrentRecipeIndex());
                } else if (message.nbt.contains("getAll")) {
                    response.put("tile", tac.serializeNBT());
                } else {
                    // Ma ci arriviamo in questo else?
                    response.put("recipe", tac.getRecipe().getNBT());
                    response.put("targetSlot", tac.getStackInSlot(TileAutoCrafter.TARGET_SLOT).serializeNBT());
                    response.putInt("rIndex", tac.getCurrentRecipeIndex());
                }

                if (response.keySet().size() > 0) {
                    // Send message to players in tile entity radius
                    PacketDistributor.PacketTarget ptarget = PacketDistributor.NEAR.with(() -> new TargetPoint(
                            message.p.getX(), message.p.getY(), message.p.getZ(), 8.0D,
                            ctx.get().getSender().getEntityWorld().dimension.getType())
                    );
                    PacketHandler.getChannel().send(ptarget, new PacketServerUpdated(message.p, response));
                }
            });
            ctx.get().setPacketHandled(true);
        }
    }
}
