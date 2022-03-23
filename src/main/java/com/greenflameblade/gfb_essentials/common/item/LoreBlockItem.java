package com.greenflameblade.gfb_essentials.common.item;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class LoreBlockItem extends BlockItem{
    
    private List<ITextComponent> description;
    private boolean isFoiled;

    public LoreBlockItem(Block block, Properties properties, List<ITextComponent> description, boolean isFoiled) {
        super(block, properties);
        this.description = description;
        this.isFoiled = isFoiled;
    }

    public LoreBlockItem(Block block, Properties properties, List<ITextComponent> description) {
        this(block, properties, description, false);
    }

    public LoreBlockItem(Block block, Properties properties, ITextComponent singleLineDescription, boolean isFoiled) {
        super(block, properties);
        this.description = new ArrayList<ITextComponent>();
        this.description.add(singleLineDescription);
        this.isFoiled = isFoiled;
    }

    public LoreBlockItem(Block block, Properties properties, ITextComponent singleLineDescription) {
        this(block, properties, singleLineDescription, false);
    }

    public LoreBlockItem(Block block, Properties properties, boolean isFoiled) {
        super(block, properties);
        this.isFoiled = isFoiled;
        this.description = new ArrayList<ITextComponent>();
    }

    public LoreBlockItem(Block block, Properties properties) {
        this(block, properties, false);
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
    }
}
