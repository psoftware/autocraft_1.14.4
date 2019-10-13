
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
            ctx.getServerHandler().field_147369_b.func_71121_q().func_152344_a(new Runnable() {
                public void run() {
                    TileAutoCrafter tac = (TileAutoCrafter)ctx.getServerHandler().field_147369_b.func_71121_q().func_175625_s(message.p);
                    CompoundNBT response = new CompoundNBT();
                    if (message.nbt.func_74764_b("targetChanged")) {
                        CompoundNBT how = message.nbt.func_74775_l("targetChanged");
                        if (how.func_74764_b("next")) {
                            tac.nextRecipe();
                        } else if (how.func_74764_b("prev")) {
                            tac.prevRecipe();
                        } else if (how.func_74764_b("item")) {
                            tac.updateRecipes(new ItemStack(how.func_74775_l("item")), 0);
                        } else {
                            System.err.println("Unhandled case in PacketClientChanged");
                        }

                        response.func_74782_a("recipe", tac.getRecipe().getNBT());
                        response.func_74782_a("targetSlot", tac.getCrafts().serializeNBT());
                        response.func_74768_a("rIndex", tac.getCurrentRecipeIndex());
                    } else if (message.nbt.func_74764_b("getAll")) {
                        response.func_74782_a("tile", tac.serializeNBT());
                    } else {
                        response.func_74782_a("recipe", tac.getRecipe().getNBT());
                        response.func_74782_a("targetSlot", tac.func_70301_a(10).serializeNBT());
                        response.func_74768_a("rIndex", tac.getCurrentRecipeIndex());
                    }

                    if (response.func_150296_c().size() > 0) {
                        TargetPoint target = new TargetPoint(ctx.getServerHandler().field_147369_b.field_71093_bK, (double)message.p.func_177958_n(), (double)message.p.func_177956_o(), (double)message.p.func_177952_p(), 8.0D);
                        Core.network.sendToAllAround(new PacketServerUpdated(message.p, response), target);
                    }

                }
            });
            return null;
        }
    }
}
