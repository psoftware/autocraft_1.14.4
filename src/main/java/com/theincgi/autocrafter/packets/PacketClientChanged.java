
package com.theincgi.autocrafter.packets;

import com.theincgi.autocrafter.Core;
import com.theincgi.autocrafter.tileEntity.TileAutoCrafter;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.network.PacketDistributor.TargetPoint;

public class PacketClientChanged extends PacketTileChanged {
    public PacketClientChanged() {
    }

    public PacketClientChanged(BlockPos p, CompoundNBT nbt) {
        super(p, nbt);
    }

    public static PacketClientChanged nextRecipe(BlockPos pos) {
        CompoundNBT tag = new CompoundNBT();
        CompoundNBT chng = new CompoundNBT();
        tag.put("targetChanged", chng);
        chng.putBoolean("next", true);
        return new PacketClientChanged(pos, tag);
    }

    public static PacketClientChanged prevRecipe(BlockPos pos) {
        CompoundNBT tag = new CompoundNBT();
        CompoundNBT chng = new CompoundNBT();
        tag.put("targetChanged", chng);
        chng.putBoolean("prev", true);
        return new PacketClientChanged(pos, tag);
    }

    public static PacketClientChanged requestAll(BlockPos pos) {
        CompoundNBT tag = new CompoundNBT();
        tag.putBoolean("getAll", true);
        return new PacketClientChanged(pos, tag);
    }

    public static IMessage targetChanged(BlockPos pos, ItemStack heldItem) {
        CompoundNBT tag = new CompoundNBT();
        CompoundNBT tag2;
        tag.put("targetChanged", tag2 = new CompoundNBT());
        tag2.put("item", heldItem.serializeNBT());
        return new PacketClientChanged(pos, tag);
    }

    public static class Handler implements IMessageHandler<PacketClientChanged, PacketTileChanged> {
        public Handler() {
        }

        public PacketTileChanged onMessage(final PacketClientChanged message, final MessageContext ctx) {
            ctx.getServerHandler().playerEntity.getServerWorld().addScheduledTask(new Runnable() {
                public void run() {
                    TileAutoCrafter tac = (TileAutoCrafter)ctx.getServerHandler().playerEntity.getServerWorld().getTileEntity(message.p);
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
                        response.put("recipe", tac.getRecipe().getNBT());
                        response.put("targetSlot", tac.func_70301_a(10).serializeNBT());
                        response.putInt("rIndex", tac.getCurrentRecipeIndex());
                    }

                    if (response.keySet().size() > 0) {
                        TargetPoint target = new TargetPoint(ctx.getServerHandler().playerEntity.dimension, (double)message.p.getX(), (double)message.p.getY(), (double)message.p.getZ(), 8.0D);
                        Core.network.sendToAllAround(new PacketServerUpdated(message.p, response), target);
                    }

                }
            });
            return null;
        }
    }
}
