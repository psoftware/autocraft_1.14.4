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
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketServerUpdated extends PacketTileChanged {
    public PacketServerUpdated() {
    }

    public PacketServerUpdated(BlockPos p, CompoundNBT nbt) {
        super(p, nbt);
    }

    public static class Handler implements IMessageHandler<PacketServerUpdated, IMessage> {
        public Handler() {
        }

        public IMessage onMessage(final PacketServerUpdated message, MessageContext ctx) {
            Minecraft.func_71410_x().func_152344_a(new Runnable() {
                public void run() {
                    TileAutoCrafter ourTile = (TileAutoCrafter)Minecraft.func_71410_x().field_71439_g.field_70170_p.func_175625_s(message.p);
                    if (message.nbt.func_74764_b("tile")) {
                        ourTile.func_145839_a(message.nbt.func_74775_l("tile"));
                    } else {
                        if (message.nbt.func_74764_b("targetSlot")) {
                            ItemStack newTarget = new ItemStack(message.nbt.func_74775_l("targetSlot"));
                            if (!Recipe.matches(ourTile.getCrafts(), newTarget)) {
                                ourTile.setCrafts(newTarget);
                            }
                        }

                        if (message.nbt.func_74764_b("recipe")) {
                            ourTile.setRecipe(message.nbt.func_150295_c("recipe", 10));
                        }

                        if (message.nbt.func_74764_b("rIndex")) {
                            ourTile.setCurrentRecipeIndex(message.nbt.func_74762_e("rIndex"));
                        }

                    }
                }
            });
            return null;
        }
    }
}
