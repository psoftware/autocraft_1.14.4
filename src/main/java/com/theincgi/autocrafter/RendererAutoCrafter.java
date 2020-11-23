package com.theincgi.autocrafter;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.theincgi.autocrafter.blocks.BlockAutoCrafter;
import com.theincgi.autocrafter.tiles.TileAutoCrafter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.vector.Vector3f;

public class RendererAutoCrafter extends TileEntityRenderer<TileAutoCrafter> {
    public RendererAutoCrafter(TileEntityRendererDispatcher tileEntityRendererDispatcher)
    {
        super(tileEntityRendererDispatcher);
    }

    public void render(TileAutoCrafter tileEntity, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay)
    {
        ItemStack itemStack = tileEntity.getRecipeResult();
        if (itemStack == null || itemStack == ItemStack.EMPTY)
        {
            return;
        }
        Direction direction = tileEntity.getBlockState().get(BlockAutoCrafter.FACING);
        float scale = 0.6f;

        matrixStack.push();
        // move to center
        matrixStack.translate(0.5, 0.5, 0.5);
        // rotate to face
        float f = -direction.getHorizontalAngle();
        matrixStack.rotate(Vector3f.YP.rotationDegrees(f));
        // move to front
        matrixStack.translate(0, 0.05, 0.5);
        // Push it out of the block face a bit to avoid z-fighting
        matrixStack.translate(0, 0, 0.01f);
        // The Z-scaling by 0.0002 causes the model to be visually "flattened"
        // This cannot replace a proper projection, but it's cheap and gives the desired
        // effect at least from head-on
        matrixStack.scale(scale, scale, 0.0002f);

        Minecraft.getInstance().getItemRenderer().renderItem(
            itemStack,
            ItemCameraTransforms.TransformType.GUI,
            15728880,
            OverlayTexture.NO_OVERLAY,
            matrixStack,
            buffer
        );

        matrixStack.pop();
    }

}