package com.theincgi.autocrafter.packets;

import com.theincgi.autocrafter.PacketHandler;
import com.theincgi.autocrafter.tileEntity.TileAutoCrafter;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.function.Supplier;

public class PacketClientCrafterAction extends TilePacket {
    enum CrafterAction{PREV, NEXT, TARGETCHANGED};

    public CrafterAction action;
    public ItemStack heldItem;

    private PacketClientCrafterAction() {}
    public PacketClientCrafterAction(BlockPos p, CrafterAction action, ItemStack heldItem) {
        super(p);
        this.action = action;
        this.heldItem = heldItem;
    }

    public static void encode(PacketClientCrafterAction pkt, PacketBuffer buf) {
        pkt.subEncode(buf);
    }

    public static PacketClientCrafterAction decode(PacketBuffer buf) {
        PacketClientCrafterAction packet = new PacketClientCrafterAction();
        packet.subDecode(buf);
        return packet;
    }

    @Override
    public void subEncode(PacketBuffer buf) {
        super.subEncode(buf);
        buf.writeInt(action.ordinal());

        if(action == CrafterAction.TARGETCHANGED)
            buf.writeCompoundTag(heldItem.serializeNBT());
    }

    @Override
    public void subDecode(PacketBuffer buf) {
        super.subDecode(buf);
        action = CrafterAction.values()[buf.readInt()];

        if(action == CrafterAction.TARGETCHANGED)
            heldItem = ItemStack.read(buf.readCompoundTag());
    }

    public static PacketClientCrafterAction nextRecipe(BlockPos pos) {
        return new PacketClientCrafterAction(pos, CrafterAction.NEXT, null);
    }

    public static PacketClientCrafterAction prevRecipe(BlockPos pos) {
        return new PacketClientCrafterAction(pos, CrafterAction.PREV, null);
    }

    public static PacketClientCrafterAction targetChanged(BlockPos pos, ItemStack heldItem) {
        return new PacketClientCrafterAction(pos, CrafterAction.TARGETCHANGED, heldItem);
    }

    public static class Handler {
            public static void onMessage(final PacketClientCrafterAction message, Supplier<NetworkEvent.Context> ctx) {
                ctx.get().enqueueWork(() -> {
                    TileAutoCrafter tac = (TileAutoCrafter)ctx.get().getSender().getServerWorld().getTileEntity(message.p);
                    switch (message.action) {
                        case NEXT:
                            tac.nextRecipe(); break;
                        case PREV:
                            tac.prevRecipe(); break;
                        case TARGETCHANGED:
                            tac.updateRecipes(message.heldItem, 0); break;
                    }

                    // Send message to players in tile entity radius
                    PacketDistributor.PacketTarget ptarget = PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(
                            message.p.getX(), message.p.getY(), message.p.getZ(), 8.0D,
                            ctx.get().getSender().getEntityWorld().getDimensionKey())
                    );
                    PacketHandler.getChannel().send(ptarget,
                            new PacketServerCrafterAction(message.p, tac.getCrafts(), tac.getRecipe(), tac.getCurrentRecipeIndex()));

                });
                ctx.get().setPacketHandled(true);
            }
        }
}