package com.theincgi.autocrafter.blocks;

import com.theincgi.autocrafter.PacketHandler;
import com.theincgi.autocrafter.packets.PacketClientRequestAll;
import com.theincgi.autocrafter.tileEntity.TileAutoCrafter;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockAutoCrafter extends ContainerBlock {


   public BlockAutoCrafter() {
      super(Block.Properties.create(Material.ROCK).hardnessAndResistance(1.5f,10).lightValue(1));

      //TODO: set creative tab (where??)
      //this.func_149647_a(CreativeTabs.field_78028_d);
   }


   @Override
   public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
      super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
      TileAutoCrafter tac = (TileAutoCrafter)worldIn.getTileEntity(pos);
   }

   @Override
   public void onBlockHarvested(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) {
      super.onBlockHarvested(worldIn, pos, state, player);
      TileAutoCrafter tac = (TileAutoCrafter)worldIn.getTileEntity(pos);
      if(!worldIn.isRemote) {
         InventoryHelper.dropInventoryItems(worldIn, pos, tac);
         System.out.println("Droped item");
      }

   }

   @Override
   public boolean canConnectRedstone(BlockState state, IBlockReader world, BlockPos pos, Direction side) {
      return true;
   }

   @Nonnull
   @Override
   public BlockRenderType getRenderType(BlockState p_149645_1_) {
      return BlockRenderType.MODEL;
   }

   @Override
   public boolean onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
      if(!world.isRemote) {
       //  player.openGui(Core.instance, 0, worldIn, pos.getX(), pos.getY(), pos.getZ());
         TileEntity tileEntity = world.getTileEntity(pos);
         if (tileEntity instanceof INamedContainerProvider)
            NetworkHooks.openGui((ServerPlayerEntity) player, (INamedContainerProvider) tileEntity, tileEntity.getPos());
         else
            throw new IllegalStateException("Our named container provider is missing!");

      } else {
         PacketHandler.getChannel().sendToServer(PacketClientRequestAll.requestAll(pos));
      }

      return true;
   }

   @Override
   public boolean hasTileEntity(BlockState state) {
      return true;
   }

   @Nullable
   @Override
   public TileEntity createNewTileEntity(IBlockReader worldIn) {
      return new TileAutoCrafter();
   }
}
