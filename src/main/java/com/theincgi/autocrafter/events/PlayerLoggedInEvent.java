package com.theincgi.autocrafter.events;

import com.theincgi.autocrafter.tiles.TileAutoCrafter;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.PacketDistributor;

@Mod.EventBusSubscriber
public class PlayerLoggedInEvent {
    @SubscribeEvent
    public static void onEvent(EntityJoinWorldEvent event)
    {
        if ((event.getEntity() instanceof PlayerEntity) && !event.getWorld().isRemote())
        {
            BlockPos pos = event.getEntity().getPosition();
            int distance = 64;
            AxisAlignedBB box = new AxisAlignedBB(
                new BlockPos(pos.getX() - distance, 0, pos.getZ() - distance),
                new BlockPos(pos.getX() + distance, 255, pos.getZ() + distance)
            );
            PacketDistributor.PacketTarget ptarget = PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(
                pos.getX(),
                pos.getY(),
                pos.getZ(),
                distance,
                event.getWorld().getDimensionKey()
            ));
            for (TileEntity te : event.getWorld().tickableTileEntities)
            {
                if (
                    te instanceof TileAutoCrafter
                    && event.getWorld().loadedTileEntityList.contains(te)
                    && event.getWorld().isBlockLoaded(te.getPos())
                    && box.contains(te.getPos().getX(), te.getPos().getY(), te.getPos().getZ())
                ){
//                    PacketHandler.getChannel().send(
//                        ptarget,
//                        new PacketForClientToUpdateTileAutoCrafter(pos, te.serializeNBT())
//                    );

                    BlockState blockState = te.getBlockState();
                    event.getWorld().notifyBlockUpdate(pos, blockState, blockState, 3);
                }
            }

        }
    }
}
