package com.theincgi.autocrafter.packets;

import com.theincgi.autocrafter.Recipe;
import com.theincgi.autocrafter.tiles.TileAutoCrafter;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketForClientToUpdateTileAutoCrafterRecipe extends TilePacket {
    public ItemStack targetSlot;
    public CompoundNBT recipeNBT;
    public int recipeIndex;

    private PacketForClientToUpdateTileAutoCrafterRecipe()
    {
    }

    ;

    public PacketForClientToUpdateTileAutoCrafterRecipe(BlockPos p, ItemStack targetSlot, Recipe recipe, int recipeIndex)
    {
        super(p);
        this.targetSlot = targetSlot;
        this.recipeNBT = new CompoundNBT();
        recipeNBT.put("recipe", recipe.serializeNBT());
        this.recipeIndex = recipeIndex;
    }

    public static void encode(PacketForClientToUpdateTileAutoCrafterRecipe pkt, PacketBuffer buf)
    {
        pkt.subEncode(buf);
    }

    public static PacketForClientToUpdateTileAutoCrafterRecipe decode(PacketBuffer buf)
    {
        PacketForClientToUpdateTileAutoCrafterRecipe packet = new PacketForClientToUpdateTileAutoCrafterRecipe();
        packet.subDecode(buf);
        return packet;
    }

    @Override
    public void subEncode(PacketBuffer buf)
    {
        super.subEncode(buf);
        buf.writeCompoundTag(this.targetSlot.serializeNBT());
        buf.writeCompoundTag(this.recipeNBT);
        buf.writeInt(recipeIndex);
    }

    @Override
    public void subDecode(PacketBuffer buf)
    {
        super.subDecode(buf);
        this.targetSlot = ItemStack.read(buf.readCompoundTag());
        this.recipeNBT = buf.readCompoundTag();
        this.recipeIndex = buf.readInt();
    }

    public static class Handler {
        public static void onMessage(final PacketForClientToUpdateTileAutoCrafterRecipe message, Supplier<NetworkEvent.Context> ctx)
        {
            ctx.get().enqueueWork(() -> {
                // handled on client side
                TileAutoCrafter ourTile = (TileAutoCrafter) Minecraft.getInstance().player.world.getTileEntity(message.p);

                ItemStack newTarget = message.targetSlot;
                if (!Recipe.itemStacksMatch(ourTile.getCrafts(), newTarget))
                {
                    ourTile.setCrafts(newTarget);
                }

                ourTile.setRecipe(message.recipeNBT.getList("recipe", 10));
                ourTile.setCurrentRecipeIndex(message.recipeIndex);
            });

            ctx.get().setPacketHandled(true);
        }
    }
}
