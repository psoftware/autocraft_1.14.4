//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.theincgi.autocrafter.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.theincgi.autocrafter.AutoCrafter;
import com.theincgi.autocrafter.PacketHandler;
import com.theincgi.autocrafter.container.ContainerAutoCrafter;
import com.theincgi.autocrafter.packets.PacketClientCrafterAction;
import com.theincgi.autocrafter.tileEntity.TileAutoCrafter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.loading.FMLEnvironment;

public class GuiAutoCrafter extends ContainerScreen<ContainerAutoCrafter> {
    ResourceLocation background = new ResourceLocation("autocrafter", "textures/gui/autocrafter_gui.png");
    GuiAutoCrafter.Button prev;
    GuiAutoCrafter.Button next;
    private PlayerInventory playerInv;
    private TileAutoCrafter tileAutoCrafter;
    private ContainerAutoCrafter container;

    public GuiAutoCrafter(ContainerAutoCrafter te, PlayerInventory playerInv, ITextComponent name) {
        super(te, playerInv, name);
        this.prev = new GuiAutoCrafter.Button(108, 17, 11, 18, 0.6901961F, 0.0F, 0.7294118F, 0.07058824F, this.background);
        this.next = new GuiAutoCrafter.Button(145, 17, 11, 18, 0.7294118F, 0.0F, 0.7764706F, 0.07058824F, this.background);
        this.playerInv = playerInv;

        this.container = super.getContainer();
        this.tileAutoCrafter = container.getTileAutoCrafter();
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    public void drawImage(ResourceLocation texture, int x, int y, int wid, int hei, float uMin, float vMin, float uMax, float vMax) {
        Minecraft.getInstance().getTextureManager().bindTexture(texture);
        GlStateManager.enableAlphaTest();
        BufferBuilder buffer = Tessellator.getInstance().getBuffer();
        buffer.begin(7, DefaultVertexFormats.POSITION_TEX);
        buffer.pos(x, y, 0.0D).tex(uMin, vMin).endVertex();
        buffer.pos(x, y + hei, 0.0D).tex(uMin, vMax).endVertex();
        buffer.pos(x + wid, y + hei, 0.0D).tex(uMax, vMax).endVertex();
        buffer.pos(x + wid, y, 0.0D).tex(uMax, vMin).endVertex();
        Tessellator.getInstance().draw();
    }

    private void drawRectangle(double x1, double y1, double x2, double y2, int color) {
        int colorB = (color & 0xFF);
        color = color >> 8;
        int colorG = (color & 0xFF);
        color = color >> 8;
        int colorR = (color & 0xFF);
        color = color >> 8;
        int colorA = (color & 0xFF);
        BufferBuilder buffer = Tessellator.getInstance().getBuffer();
        buffer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        buffer.pos(x1, y1, 0.0D).color(colorR, colorG, colorB, colorA).endVertex();
        buffer.pos(x1, y2, 0.0D).color(colorR, colorG, colorB, colorA).endVertex();
        buffer.pos(x2, y2, 0.0D).color(colorR, colorG, colorB, colorA).endVertex();
        buffer.pos(x2, y1, 0.0D).color(colorR, colorG, colorB, colorA).endVertex();
        Tessellator.getInstance().draw();
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY) {
        int x = this.width / 2 - 88;
        int y = this.height / 2 - 83;
        Minecraft.getInstance().getTextureManager().bindTexture(this.background);
        this.drawImage(this.background, x, y, 176, 166, 0.0F, 0.0F, 0.6875F, 0.6484375F);
        this.prev.draw(mouseX, mouseY, x, y);
        this.next.draw(mouseX, mouseY, x, y);

        Slot slot;
        for (int i = 0; i < this.tileAutoCrafter.getRecipe().items.size(); ++i) {
            slot = this.container.getSlot(i);
            ItemStack req = this.tileAutoCrafter.getRecipe().getDisplayItem(i);

            RenderHelper.disableStandardItemLighting();
            RenderHelper.enableStandardItemLighting();
            this.itemRenderer.renderItemAndEffectIntoGUI(req, slot.xPos + x, slot.yPos + y);
            GlStateManager.enableAlphaTest();
            GlStateManager.enableBlend();
            GlStateManager.disableDepthTest();

            RenderHelper.enableStandardItemLighting();

            GlStateManager.disableTexture();
            drawRectangle(slot.xPos + x, slot.yPos + y, slot.xPos + x + 16, slot.yPos + y + 16, req.isEmpty() ? 0xf0484848 : 0x708b8b8b);
            GlStateManager.enableTexture();
            GlStateManager.enableDepthTest();
        }

        ItemStack target = this.tileAutoCrafter.getCrafts();
        slot = this.container.targetSlot;
        GlStateManager.disableDepthTest();
        GlStateManager.color4f(1f, 1f, 1f, 1f);
        this.itemRenderer.renderItemAndEffectIntoGUI(target, x + slot.xPos, y + slot.yPos);
        GlStateManager.enableDepthTest();
    }

    @Override
    protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int mouseX, int mouseY) {
        String s = this.tileAutoCrafter.getDisplayName().getUnformattedComponentText();
        // super.drawString(Minecraft.getInstance().fontRenderer.d,s, 88,6, 0x404040);

        Minecraft.getInstance().fontRenderer.drawString(matrixStack, s, 88 - Minecraft.getInstance().fontRenderer.getStringWidth(s) / 2, 6, 0x404040);
        Minecraft.getInstance().fontRenderer.drawString(matrixStack, this.playerInv.getDisplayName().getString(), 8, 72, 0x404040);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        double x = (double) this.width / 2 - 88;
        double y = (double) this.height / 2 - 83;

        if (this.prev.isInBounds(mouseX - x, mouseY - y)) {
            PacketClientCrafterAction packet = PacketClientCrafterAction.nextRecipe(this.tileAutoCrafter.getPos());
            PacketHandler.getChannel().sendToServer(packet);
        } else if (this.next.isInBounds(mouseX - x, mouseY - y)) {
            PacketClientCrafterAction packet = PacketClientCrafterAction.prevRecipe(this.tileAutoCrafter.getPos());
            PacketHandler.getChannel().sendToServer(packet);
        } else if (this.container.targetSlot.equals(this.getSlotUnderMouse())) {
            // TODO: Only client side. While the others don't do the same ??
            if (FMLEnvironment.dist.isClient()) {
                PacketHandler.getChannel().sendToServer(PacketClientCrafterAction.targetChanged(
                    this.tileAutoCrafter.getPos(), Minecraft.getInstance().player.inventory.getItemStack())
                );
            }
        }

        return super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    class Button {
        int x;
        int y;
        int wid;
        int hei;
        float umin;
        float umax;
        float vmin;
        float vmax;
        ResourceLocation tex;

        public Button(int x, int y, int wid, int hei, float umin, float vmin, float umax, float vmax, ResourceLocation tex) {
            this.x = x;
            this.y = y;
            this.wid = wid;
            this.hei = hei;
            this.umin = umin;
            this.umax = umax;
            this.vmin = vmin;
            this.vmax = vmax;
            this.tex = tex;
        }

        public void draw(int mouseX, int mouseY, int dx, int dy) {
            float yOffset = this.isInBounds(mouseX - dx, mouseY - dy) ? (float) this.hei / 255.0F : 0.0F;
            GuiAutoCrafter.this.drawImage(this.tex, this.x + dx, this.y + dy, this.wid, this.hei, this.umin, this.vmin + yOffset, this.umax, this.vmax + yOffset);
        }

        public boolean isInBounds(double clickX, double clickY) {
            return clickX > this.x && clickY > this.y && clickX < this.x + this.wid && clickY < this.y + this.hei;
        }
    }
}
