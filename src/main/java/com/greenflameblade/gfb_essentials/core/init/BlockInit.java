package com.greenflameblade.gfb_essentials.core.init;

import java.util.Random;

import com.greenflameblade.gfb_essentials.GFBEssentials;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.GlassBlock;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.StairsBlock;
import net.minecraft.block.SugarCaneBlock;
import net.minecraft.block.material.Material;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.PlantType;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class BlockInit {
    
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, GFBEssentials.MODID);

    public static final RegistryObject<Block> STONE_BRICKS = BLOCKS.register("stonebrick", () -> new Block(AbstractBlock.Properties.copy(Blocks.STONE_BRICKS).strength(-1.0F, 3600000.8F)));
    public static final RegistryObject<Block> STONE_BRICK_STAIRS = BLOCKS.register("stonebrick_stairs",
            () -> new StairsBlock(() -> STONE_BRICKS.get().defaultBlockState(),
                    AbstractBlock.Properties.copy(STONE_BRICKS.get())));

    public static final RegistryObject<Block> STONE_BRICK_SLAB = BLOCKS.register("stonebrick_slab",
            () -> new SlabBlock(AbstractBlock.Properties.copy(STONE_BRICKS.get())));

    public static final RegistryObject<Block> ENCHANTED_GLASS = BLOCKS.register("enchanted_glass", () -> new GlassBlock(AbstractBlock.Properties.copy(Blocks.GLASS).strength(40.0F, 1000.0F).harvestLevel(4).requiresCorrectToolForDrops().harvestTool(ToolType.PICKAXE)));

    public static final RegistryObject<Block> MOLLYCODDLE_VINE = BLOCKS.register("mollycoddle_vine", () -> new SugarCaneBlock(AbstractBlock.Properties.of(Material.PLANT).randomTicks().noCollission().sound(SoundType.VINE).strength(0f, 0f).lightLevel(s -> 0)) {
        @Override
        public int getFlammability(BlockState state, IBlockReader world, BlockPos pos, Direction face) {
            return 100;
        }

        @Override
        public int getFireSpreadSpeed(BlockState state, IBlockReader world, BlockPos pos, Direction face) {
            return 60;
        }

        @Override
        public PlantType getPlantType(IBlockReader world, BlockPos pos) {
            return PlantType.PLAINS;
        }

        @Override
        public void tick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
            if (world.isEmptyBlock(pos.above())) {
                int i = 1;
                for(; world.getBlockState(pos.below(i)).getBlock() == this; ++i);
                if (i < 3) {
                    int j = state.getValue(AGE);
                    if (j == 15) {
                        world.setBlockAndUpdate(pos.above(), defaultBlockState());
                        world.setBlock(pos, state.setValue(AGE, 0), 4);
                    } else {
                        world.setBlock(pos, state.setValue(AGE, j + 1), 4);
                    }
                }
            }
        }
    });
}
