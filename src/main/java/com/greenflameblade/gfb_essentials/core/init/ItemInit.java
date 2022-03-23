package com.greenflameblade.gfb_essentials.core.init;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.greenflameblade.gfb_essentials.GFBEssentials;
import com.greenflameblade.gfb_essentials.common.item.CurrencyItem;
import com.greenflameblade.gfb_essentials.common.item.DragonPhermomonesItem;
import com.greenflameblade.gfb_essentials.common.item.LoreBlockItem;
import com.greenflameblade.gfb_essentials.common.item.LoreItem;

import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemInit {
    
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, GFBEssentials.MODID);

    public static final RegistryObject<Item> MOLLYCODDLE = ITEMS.register("mollycoddle", () -> new Item(new Item.Properties().rarity(Rarity.RARE).tab(ItemGroup.TAB_BREWING)));

    public static final RegistryObject<Item> COPPER_COIN = ITEMS.register("copper_coin", () -> new CurrencyItem(new Item.Properties().tab(ItemGroup.TAB_MISC), 1));
    public static final RegistryObject<Item> BRONZE_COIN = ITEMS.register("bronze_coin", () -> new CurrencyItem(new Item.Properties().tab(ItemGroup.TAB_MISC), 10));
    public static final RegistryObject<Item> SILVER_COIN = ITEMS.register("silver_coin", () -> new CurrencyItem(new Item.Properties().tab(ItemGroup.TAB_MISC), 100));
    public static final RegistryObject<Item> GOLD_COIN = ITEMS.register("gold_coin", () -> new CurrencyItem(new Item.Properties().tab(ItemGroup.TAB_MISC), 1000));
    
    public static final RegistryObject<Item> TAB_ITEM = ITEMS.register("tab_item", () -> new Item(new Item.Properties()));
    
    public static final RegistryObject<Item> ENCHANTED_BOTTLE = ITEMS.register("enchanted_bottle", () -> new LoreItem(new Item.Properties().tab(ItemGroup.TAB_MISC)
            .fireResistant().rarity(Rarity.UNCOMMON).stacksTo(16), true));
    public static final RegistryObject<Item> FIRE_DRAGON_PHEROMONES = ITEMS.register("fire_dragon_pheromones", () -> new DragonPhermomonesItem(new Item.Properties().tab(ItemGroup.TAB_MISC)
            .fireResistant().rarity(Rarity.EPIC), DragonPhermomonesItem.FIRE_DRAGON_PHEROMONES_EFFECTS, entity -> entity.setSecondsOnFire(10)));
    public static final RegistryObject<Item> ICE_DRAGON_PHEROMONES = ITEMS.register("ice_dragon_pheromones", () -> new DragonPhermomonesItem(new Item.Properties().tab(ItemGroup.TAB_MISC)
            .fireResistant().rarity(Rarity.EPIC), DragonPhermomonesItem.ICE_DRAGON_PHEROMONES_EFFECTS));
    public static final RegistryObject<Item> LIGHTNING_DRAGON_PHEROMONES = ITEMS.register("lightning_dragon_pheromones", () -> new DragonPhermomonesItem(new Item.Properties().tab(ItemGroup.TAB_MISC)
            .fireResistant().rarity(Rarity.EPIC), DragonPhermomonesItem.LIGHTNING_DRAGON_PHEROMONES_EFFECTS, DragonPhermomonesItem::strikeLightning));

    // Block Items
    public static final RegistryObject<BlockItem> STONE_BRICKS = ITEMS.register("stonebrick", () -> new LoreBlockItem(BlockInit.STONE_BRICKS.get(), new Item.Properties().tab(ItemGroup.TAB_BUILDING_BLOCKS),
            new TranslationTextComponent("tooltip.gfb_essentials.stonebrick"), false));
    public static final RegistryObject<BlockItem> STONE_BRICK_STAIRS = ITEMS.register("stonebrick_stairs", () -> new LoreBlockItem(BlockInit.STONE_BRICK_STAIRS.get(), new Item.Properties().tab(ItemGroup.TAB_BUILDING_BLOCKS),
            new TranslationTextComponent("tooltip.gfb_essentials.stonebrick"), false));
    public static final RegistryObject<BlockItem> STONE_BRICK_SLAB = ITEMS.register("stonebrick_slab", () -> new LoreBlockItem(BlockInit.STONE_BRICK_SLAB.get(), new Item.Properties().tab(ItemGroup.TAB_BUILDING_BLOCKS),
            new TranslationTextComponent("tooltip.gfb_essentials.stonebrick"), false));
    public static final RegistryObject<BlockItem> ENCHANTED_GLASS = ITEMS.register("enchanted_glass", () -> new BlockItem(BlockInit.ENCHANTED_GLASS.get(), new Item.Properties().tab(ItemGroup.TAB_BUILDING_BLOCKS)));
    public static final RegistryObject<BlockItem> MOLLYCODDLE_VINE = ITEMS.register("mollycoddle_vine", () -> new BlockItem(BlockInit.MOLLYCODDLE_VINE.get(), new Item.Properties().rarity(Rarity.RARE).tab(ItemGroup.TAB_DECORATIONS)));

    public static List<ItemStack> getTabItems() {
        return Arrays.asList(
            MOLLYCODDLE, MOLLYCODDLE_VINE, COPPER_COIN, BRONZE_COIN, SILVER_COIN, GOLD_COIN,
            ENCHANTED_BOTTLE, FIRE_DRAGON_PHEROMONES, ICE_DRAGON_PHEROMONES, LIGHTNING_DRAGON_PHEROMONES,
            STONE_BRICKS, STONE_BRICK_SLAB, STONE_BRICK_STAIRS, ENCHANTED_GLASS
        ).stream().map(e -> e.get().getDefaultInstance()).collect(Collectors.toList());
    }
}