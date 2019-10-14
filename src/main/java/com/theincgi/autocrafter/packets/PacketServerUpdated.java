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
            Minecraft.getInstance().execute(new Runnable() {
                public void run() {
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
                }
            });
            return null;
        }
    }
}
