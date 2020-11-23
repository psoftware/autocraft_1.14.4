package com.theincgi.autocrafter.blocks;

import com.theincgi.autocrafter.PacketHandler;
import com.theincgi.autocrafter.packets.PacketForServerRequestingTileAutoCrafterUpdatePacket;
import com.theincgi.autocrafter.tiles.TileAutoCrafter;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockAutoCrafter extends ContainerBlock {

    public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING; // BlockStateProperties.HORIZONTAL_FACING;

    public BlockAutoCrafter()
    {
        super(Block.Properties.create(Material.ROCK).hardnessAndResistance(1.5f, 10));
        this.setDefaultState(this.stateContainer.getBaseState().with(FACING, Direction.NORTH));
    }

    public BlockState rotate(BlockState state, Rotation rot)
    {
        return state.with(FACING, rot.rotate(state.get(FACING)));
    }

    public BlockState mirror(BlockState state, Mirror mirrorIn)
    {
        return state.rotate(mirrorIn.toRotation(state.get(FACING)));
    }

    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        builder.add(FACING);
    }

    @Nullable
    public BlockState getStateForPlacement(BlockItemUseContext context)
    {
        return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing().getOpposite());
    }

    /*
        @Nullable
        @Override
        public BlockState getStateForPlacement(BlockItemUseContext context) {
            BlockState blockstate = this.getDefaultState();
            World world = context.getWorld();
            BlockPos blockpos = context.getPos();
            Direction [] nearestLookingDirections = context.getNearestLookingDirections();

            for (Direction direction : nearestLookingDirections) {
                if (direction.getAxis().isHorizontal()) {
                    blockstate = blockstate.with(FACING, direction);
                    if (blockstate.isValidPosition(world, blockpos)) {
                        return blockstate;
                    }
                }
            }

            return null;
        }
    */
    @Override
    public VoxelShape getRenderShape(BlockState state, IBlockReader worldIn, BlockPos pos)
    {
        return VoxelShapes.empty();
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack)
    {
        super.onBlockPlacedBy(world, pos, state, placer, stack);
//        TileAutoCrafter tac = (TileAutoCrafter) world.getTileEntity(pos);
    }

    @Override
    public void onBlockHarvested(World world, BlockPos pos, BlockState state, PlayerEntity player)
    {
        super.onBlockHarvested(world, pos, state, player);
        TileAutoCrafter tac = (TileAutoCrafter) world.getTileEntity(pos);
        if (!world.isRemote)
        {
            InventoryHelper.dropInventoryItems(world, pos, tac);
            System.out.println("Dropped item");
        }
    }

    @Override
    public boolean canConnectRedstone(BlockState state, IBlockReader world, BlockPos pos, Direction side)
    {
        return true;
    }

    @Nonnull
    @Override
    public BlockRenderType getRenderType(BlockState state)
    {
        return BlockRenderType.MODEL;
//        return BlockRenderType.INVISIBLE;
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit)
    {
        if (world.isRemote)
        {
            PacketHandler.getChannel().sendToServer(PacketForServerRequestingTileAutoCrafterUpdatePacket.requestAll(pos));
        }
        else
        {
            //  player.openGui(Core.instance, 0, worldIn, pos.getX(), pos.getY(), pos.getZ());
            TileEntity tileEntity = world.getTileEntity(pos);
            if (tileEntity instanceof INamedContainerProvider)
            {
                NetworkHooks.openGui((ServerPlayerEntity) player, (INamedContainerProvider) tileEntity, tileEntity.getPos());
            }
            else
            {
                throw new IllegalStateException("A named container provider is missing!");
            }
        }

        return ActionResultType.SUCCESS;
    }

    @Override
    public boolean hasTileEntity(BlockState state)
    {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(IBlockReader world)
    {
        return new TileAutoCrafter();
    }
}
