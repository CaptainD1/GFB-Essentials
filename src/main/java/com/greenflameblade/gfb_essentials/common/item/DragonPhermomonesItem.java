package com.greenflameblade.gfb_essentials.common.item;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import com.greenflameblade.gfb_essentials.core.init.ItemInit;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DrinkHelper;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

public class DragonPhermomonesItem extends LoreItem {
    
    public static final List<EffectInstance> PHEROMONES_GENERIC_EFFECTS = Arrays.asList(
        new EffectInstance(Effects.BLINDNESS, 1000), new EffectInstance(Effects.CONFUSION, 1000),
        new EffectInstance(Effects.HEALTH_BOOST, 1000, 4), new EffectInstance(Effects.REGENERATION, 300, 3)
    );

    public static final List<EffectInstance> FIRE_DRAGON_PHEROMONES_EFFECTS = Arrays.asList(
        new EffectInstance(Effects.HUNGER, 1000), new EffectInstance(Effects.DAMAGE_BOOST, 300, 2),
        new EffectInstance(Effects.MOVEMENT_SPEED, 300)
    );
    public static final List<EffectInstance> ICE_DRAGON_PHEROMONES_EFFECTS = Arrays.asList(
        new EffectInstance(Effects.MOVEMENT_SLOWDOWN, 1000), new EffectInstance(Effects.DAMAGE_BOOST, 300, 1),
        new EffectInstance(Effects.DAMAGE_RESISTANCE, 300, 1)
    );
    public static final List<EffectInstance> LIGHTNING_DRAGON_PHEROMONES_EFFECTS = Arrays.asList(
        new EffectInstance(Effects.GLOWING, 1000), new EffectInstance(Effects.DAMAGE_BOOST, 300, 1),
        new EffectInstance(Effects.MOVEMENT_SPEED, 300, 2)
    );

    private List<EffectInstance> effects = new ArrayList<EffectInstance>();
    private List<Consumer<LivingEntity>> extraEffects = new ArrayList<Consumer<LivingEntity>>();

    public DragonPhermomonesItem(Properties properties, List<EffectInstance> effects) {
        super(properties.stacksTo(1), true);
        this.effects.addAll(effects);
    }

    public DragonPhermomonesItem(Properties properties, List<EffectInstance> effects, List<Consumer<LivingEntity>> extraEffects) {
        this(properties, effects);
        this.extraEffects.addAll(extraEffects);
    }

    public DragonPhermomonesItem(Properties properties, List<EffectInstance> effects, Consumer<LivingEntity> extraEffect) {
        this(properties, effects, Arrays.asList(extraEffect));
    }

    @Override
    public ItemStack finishUsingItem(ItemStack item, World world, LivingEntity entity) {
        super.finishUsingItem(item, world, entity);
        if(entity instanceof ServerPlayerEntity) {
            ServerPlayerEntity serverPlayer = (ServerPlayerEntity) entity;
            CriteriaTriggers.CONSUME_ITEM.trigger(serverPlayer, item);
            serverPlayer.awardStat(Stats.ITEM_USED.get(this));
        }

        if (!world.isClientSide) {
            effects.forEach(effect -> entity.addEffect(new EffectInstance(effect)));
            PHEROMONES_GENERIC_EFFECTS.forEach(effect -> entity.addEffect(new EffectInstance(effect)));
            extraEffects.forEach(effect -> effect.accept(entity));
        }

        if (item.isEmpty()) {
            return new ItemStack(ItemInit.ENCHANTED_BOTTLE.get());
        } else {
            if (entity instanceof PlayerEntity && !((PlayerEntity)entity).abilities.instabuild) {
                ItemStack bottleItem = new ItemStack(ItemInit.ENCHANTED_BOTTLE.get());
                PlayerEntity playerEntity = (PlayerEntity) entity;
                item.shrink(1);
                if (!playerEntity.inventory.add(bottleItem)) {
                    playerEntity.drop(bottleItem, false);
                }
            }
        }
        return item;
    }

    @Override
    public int getUseDuration(ItemStack item) {
        return 80;
    }

    @Override
    public UseAction getUseAnimation(ItemStack item) {
        return UseAction.DRINK;
    }

    @Override
    public SoundEvent getDrinkingSound() {
        return SoundEvents.GENERIC_DRINK;
    }

    @Override
    public SoundEvent getEatingSound() {
        return SoundEvents.GENERIC_DRINK;
    }

    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        return DrinkHelper.useDrink(world, player, hand);
    }

    public static void strikeLightning(LivingEntity entity) {
        LightningBoltEntity lightning = new LightningBoltEntity(EntityType.LIGHTNING_BOLT, entity.level);
        lightning.setPos(entity.getX(), entity.getY(), entity.getZ());
        entity.level.addFreshEntity(lightning);
    }
}
