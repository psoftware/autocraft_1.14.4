package com.theincgi.autocrafter.packets;

import com.theincgi.autocrafter.tiles.TileAutoCrafter;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketForServerRequestingTileAutoCrafterUpdateRecipePacket extends TilePacket {
    enum CrafterAction {PREV, NEXT, TARGETCHANGED}

    public CrafterAction action;
    public ItemStack heldItem;

    private PacketForServerRequestingTileAutoCrafterUpdateRecipePacket()
    {
    }

    public PacketForServerRequestingTileAutoCrafterUpdateRecipePacket(BlockPos p, CrafterAction action, ItemStack heldItem)
    {
        super(p);
        this.action = action;
        this.heldItem = heldItem;
    }

    public static void encode(PacketForServerRequestingTileAutoCrafterUpdateRecipePacket pkt, PacketBuffer buf)
    {
        pkt.subEncode(buf);
    }

    public static PacketForServerRequestingTileAutoCrafterUpdateRecipePacket decode(PacketBuffer buf)
    {
        PacketForServerRequestingTileAutoCrafterUpdateRecipePacket packet = new PacketForServerRequestingTileAutoCrafterUpdateRecipePacket();
        packet.subDecode(buf);
        return packet;
    }

    @Override
    public void subEncode(PacketBuffer buf)
    {
        super.subEncode(buf);
        buf.writeInt(action.ordinal());

        if (action == CrafterAction.TARGETCHANGED)
        {
            buf.writeCompoundTag(heldItem.serializeNBT());
        }
    }

    @Override
    public void subDecode(PacketBuffer buf)
    {
        super.subDecode(buf);
        action = CrafterAction.values()[buf.readInt()];

        if (action == CrafterAction.TARGETCHANGED)
        {
            heldItem = ItemStack.read(buf.readCompoundTag());
        }
    }

    public static PacketForServerRequestingTileAutoCrafterUpdateRecipePacket nextRecipe(BlockPos pos)
    {
        return new PacketForServerRequestingTileAutoCrafterUpdateRecipePacket(pos, CrafterAction.NEXT, null);
    }

    public static PacketForServerRequestingTileAutoCrafterUpdateRecipePacket prevRecipe(BlockPos pos)
    {
        return new PacketForServerRequestingTileAutoCrafterUpdateRecipePacket(pos, CrafterAction.PREV, null);
    }

    public static PacketForServerRequestingTileAutoCrafterUpdateRecipePacket targetChanged(BlockPos pos, ItemStack heldItem)
    {
        return new PacketForServerRequestingTileAutoCrafterUpdateRecipePacket(pos, CrafterAction.TARGETCHANGED, heldItem);
    }

    public static class Handler {
        public static void onMessage(final PacketForServerRequestingTileAutoCrafterUpdateRecipePacket message, Supplier<NetworkEvent.Context> ctx)
        {
            ctx.get().enqueueWork(() -> {
                // handled by server
                TileAutoCrafter tac = (TileAutoCrafter) ctx.get().getSender().getServerWorld().getTileEntity(message.p);
                switch (message.action)
                {
                    case NEXT:
                        tac.nextRecipe();
                        break;
                    case PREV:
                        tac.prevRecipe();
                        break;
                    case TARGETCHANGED:
                        tac.updateRecipes(message.heldItem, 0);
                        break;
                }

                // Send message to players in tile entity radius
                BlockState blockState = tac.getBlockState();
                tac.getWorld().notifyBlockUpdate(tac.getPos(), blockState, blockState, 3);

//                PacketDistributor.PacketTarget ptarget = PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(
//                    message.p.getX(), message.p.getY(), message.p.getZ(), 32.0D,
//                    ctx.get().getSender().getEntityWorld().getDimensionKey())
//                );
//                PacketHandler.getChannel().send(
//                    ptarget,
//                    new PacketForClientToUpdateTileAutoCrafterRecipe(message.p, tac.getCrafts(), tac.getRecipe(), tac.getCurrentRecipeIndex())
//                );

            });
            ctx.get().setPacketHandled(true);
        }
    }
}