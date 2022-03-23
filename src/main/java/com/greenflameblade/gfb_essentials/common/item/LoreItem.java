package com.greenflameblade.gfb_essentials.common.item;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.glfw.GLFW;

import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.client.util.InputMappings;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class LoreItem extends Item {
    
    private List<ITextComponent> description = new ArrayList<ITextComponent>();
    private List<ITextComponent> hiddenDescription = new ArrayList<ITextComponent>();
    private boolean isFoiled;

    public LoreItem(Properties properties, List<ITextComponent> description, boolean isFoiled) {
        super(properties);
        this.description.addAll(description);
        this.isFoiled = isFoiled;
    }

    public LoreItem(Properties properties, List<ITextComponent> description) {
        this(properties, description, false);
    }

    public LoreItem(Properties properties, ITextComponent singleLineDescription, boolean isFoiled) {
        super(properties);
        this.description.add(singleLineDescription);
        this.isFoiled = isFoiled;
    }

    public LoreItem(Properties properties, ITextComponent singleLineDescription) {
        this(properties, singleLineDescription, false);
    }

    public LoreItem(Properties properties, boolean isFoiled) {
        super(properties);
        this.isFoiled = isFoiled;
    }

    public LoreItem(Properties properties) {
        this(properties, false);
    }

    @Override
    public boolean isFoil(ItemStack item) {
        return isFoiled;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack item, World world, List<ITextComponent> tooltip,
            ITooltipFlag flagIn) {
        tooltip.addAll(description);
        if(hiddenDescription.size() > 0) {
            if (InputMappings.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), GLFW.GLFW_KEY_LEFT_SHIFT)) {
                tooltip.addAll(hiddenDescription);
            } else {
                tooltip.add(new TranslationTextComponent("tooltip.gfb_essentials.hold_shift"));
            }
        }
    }

    public void addHiddenDescription(List<ITextComponent> descriptions) {
        hiddenDescription.addAll(descriptions);
    }

    public void addHiddenDescription(ITextComponent description) {
        hiddenDescription.add(description);
    }
}
