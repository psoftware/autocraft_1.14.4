//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.theincgi.autocrafter.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import com.theincgi.autocrafter.Core;
import com.theincgi.autocrafter.container.ContainerAutoCrafter;
import com.theincgi.autocrafter.packets.PacketClientChanged;
import com.theincgi.autocrafter.tileEntity.TileAutoCrafter;
import java.io.IOException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class GuiAutoCrafter extends ContainerScreen<ContainerAutoCrafter> {
    ResourceLocation background = new ResourceLocation("autocrafter", "textures/gui/autocrafter_gui.png");
    private static final int GUIWID = 176;
    private static final int GUIHEI = 166;
    private static final int IMG_WID = 256;
    private static final int IMG_HEI = 256;
    private static final float uMax = 0.6875F;
    private static final float vMax = 0.6484375F;
    private static final float leftUMin = 0.6875F;
    private static final float leftUMax = 0.73046875F;
    private static final float rightUMin = 0.73046875F;
    private static final float rightUMax = 0.7734375F;
    private static final float buttonVMin = 0.0F;
    private static final float buttonVMax = 0.140625F;
    GuiAutoCrafter.Button prev;
    GuiAutoCrafter.Button next;
    private IInventory playerInv;
    private TileAutoCrafter tileAutoCrafter;
    private ContainerAutoCrafter container;

    public GuiAutoCrafter(ContainerAutoCrafter te, PlayerInventory playerInv , ITextComponent name)  {
        super(te,playerInv,name);
        this.prev = new GuiAutoCrafter.Button(108, 17, 11, 18, 0.6901961F, 0.0F, 0.7294118F, 0.07058824F, this.background);
        this.next = new GuiAutoCrafter.Button(145, 17, 11, 18, 0.7294118F, 0.0F, 0.7764706F, 0.07058824F, this.background);
        this.playerInv = playerInv;
       // this.tileAutoCrafter = te;
        this.container = (ContainerAutoCrafter)super.getContainer();
    }

    public boolean func_73868_f() {
        return false;
    }


    public void drawImage(ResourceLocation texture, int x, int y, int wid, int hei, float uMin, float vMin, float uMax, float vMax) {
        Minecraft.getInstance().getTextureManager().bindTexture(texture);
        GlStateManager.enableAlphaTest();
        BufferBuilder buffer = Tessellator.getInstance().getBuffer();
        buffer.begin(7, DefaultVertexFormats.POSITION_TEX);
        buffer.pos((double)x, (double)y, 0.0D).tex((double)uMin, (double)vMin).endVertex();
        buffer.pos((double)x, (double)(y + hei), 0.0D).tex((double)uMin, (double)vMax).endVertex();
        buffer.pos((double)(x + wid), (double)(y + hei), 0.0D).tex((double)uMax, (double)vMax).endVertex();
        buffer.pos((double)(x + wid), (double)y, 0.0D).tex((double)uMax, (double)vMin).endVertex();
        Tessellator.getInstance().draw();
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        int x = this.width / 2 - 88;
        int y = this.height / 2 - 83;
        Minecraft.getInstance().getTextureManager().bindTexture(this.background);
        this.drawImage(this.background, x, y, 176, 166, 0.0F, 0.0F, 0.6875F, 0.6484375F);
        this.prev.draw(mouseX, mouseY, x, y);
        this.next.draw(mouseX, mouseY, x, y);

        Slot slot;
        for(int i = 0; i < this.tileAutoCrafter.getRecipe().items.size(); ++i) {
            slot = this.container.getSlot(i);
            ItemStack req = this.tileAutoCrafter.getRecipe().getDisplayItem(i);

            RenderHelper.disableStandardItemLighting();
            RenderHelper.enableGUIStandardItemLighting();
            this.itemRenderer.renderItemAndEffectIntoGUI(req, slot.xPos + x, slot.yPos + y);
            GlStateManager.enableAlphaTest();
            GlStateManager.enableBlend();
            GlStateManager.disableDepthTest();

            RenderHelper.enableGUIStandardItemLighting();

            //todo:
           //  drawRect(slot.xPos+x, slot.yPos+y, slot.xPos+x+16, slot.yPos+y+16, req.isEmpty()?0xf0484848:0x708b8b8b);
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
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        String s = this.tileAutoCrafter.getDisplayName().getUnformattedComponentText();

        fontRendererObj.drawString(s, 88-fontRendererObj.getStringWidth(s)/2, 6, 0x404040);
        this.fontRendererObj.drawString(this.playerInv.getDisplayName().getUnformattedText(), 8, 72, 0x404040);
    }


    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        int x = this.width / 2 - 88;
        int y = this.height / 2 - 83;
        int playerDim = Minecraft.getInstance().player.dimension.getId();
        PacketClientChanged packet;
        if (this.prev.isInBounds(mouseX - x, mouseY - y)) {
            packet = PacketClientChanged.nextRecipe(this.tileAutoCrafter.getPos());
            Core.network.sendToServer(packet);
        } else if (this.next.isInBounds(mouseX - x, mouseY - y)) {
            packet = PacketClientChanged.prevRecipe(this.tileAutoCrafter.getPos());
            Core.network.sendToServer(packet);
        } else if (this.container.targetSlot.equals(this.getSlotUnderMouse())) {
            Core.proxy.sendPacketServer(PacketClientChanged.targetChanged(this.tileAutoCrafter.getPos(), Minecraft.getInstance().player.inventory.getItemStack()));
        }

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
            float yOffset = this.isInBounds(mouseX - dx, mouseY - dy) ? (float)this.hei / 255.0F : 0.0F;
            GuiAutoCrafter.this.drawImage(this.tex, this.x + dx, this.y + dy, this.wid, this.hei, this.umin, this.vmin + yOffset, this.umax, this.vmax + yOffset);
        }

        public boolean isInBounds(int clickX, int clickY) {
            return clickX > this.x && clickY > this.y && clickX < this.x + this.wid && clickY < this.y + this.hei;
        }
    }
}
