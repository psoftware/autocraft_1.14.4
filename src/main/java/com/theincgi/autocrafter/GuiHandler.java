package com.theincgi.autocrafter;

import com.theincgi.autocrafter.container.ContainerAutoCrafter;
import com.theincgi.autocrafter.gui.GuiAutoCrafter;
import com.theincgi.autocrafter.tileEntity.TileAutoCrafter;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {

   public static final int AUTOCRAFTER_GUI = 0;


   public Object getServerGuiElement(int ID, PlayerEntity player, World world, int x, int y, int z) {
      switch(ID) {
      case 0:
         TileAutoCrafter tac = (TileAutoCrafter)world.getTileEntity(new BlockPos(x, y, z));
         return new ContainerAutoCrafter(player.inventory, tac);
      default:
         return null;
      }
   }

   public Object getClientGuiElement(int ID, PlayerEntity player, World world, int x, int y, int z) {
      switch(ID) {
      case 0:
         TileAutoCrafter tac = (TileAutoCrafter)world.getTileEntity(new BlockPos(x, y, z));
         return new GuiAutoCrafter(player.inventory, tac);
      default:
         return null;
      }
   }
}
