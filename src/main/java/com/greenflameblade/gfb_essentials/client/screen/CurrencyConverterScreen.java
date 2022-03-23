package com.greenflameblade.gfb_essentials.client.screen;

import com.greenflameblade.gfb_essentials.GFBEssentials;
import com.greenflameblade.gfb_essentials.common.container.CurrencyConverterContainer;
import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class CurrencyConverterScreen extends ContainerScreen<CurrencyConverterContainer> {

    public static final ResourceLocation TEXTURE = new ResourceLocation(GFBEssentials.MODID, "textures/gui/coin_converter.png");

    public CurrencyConverterScreen(CurrencyConverterContainer container, PlayerInventory playerInventory, ITextComponent title) {
        super(container, playerInventory, title);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void renderBg(MatrixStack matrixStack, float partialTicks, int mouseX, int mosueY) {
        if (minecraft == null) {
            return;
        }

        minecraft.getTextureManager().bind(TEXTURE);

        blit(matrixStack, leftPos, topPos, 0, 0, this.imageWidth, this.imageHeight);
    }
    
}
