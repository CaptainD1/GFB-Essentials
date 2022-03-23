package com.greenflameblade.gfb_essentials.common.item;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

import com.greenflameblade.gfb_essentials.common.container.CurrencyConverterContainer;

import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Pose;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

public class CurrencyItem extends LoreItem implements Comparable<CurrencyItem> {

    public static List<ITextComponent> CONVERSION_INSTRUCTIONS = Arrays.asList(
        new TranslationTextComponent("tooltip.gfb_essentials.currency_item.instructions_1"),
        new TranslationTextComponent("tooltip.gfb_essentials.currency_item.instructions_2"),
        new TranslationTextComponent("tooltip.gfb_essentials.currency_item.instructions_3")
    );
    
    private static TreeSet<CurrencyItem> sortedItems = new TreeSet<CurrencyItem>();
    private int value;

    public CurrencyItem(Properties properties, int value) {
        super(properties.rarity(Rarity.UNCOMMON), new StringTextComponent("\u00A72" + value + (value == 1 ? " Mark" : " Marks")));
        addHiddenDescription(CONVERSION_INSTRUCTIONS);
        this.value = value;
        sortedItems.add(this);
    }

    @Override
    public boolean canAttackBlock(BlockState state, World world, BlockPos pos,
            PlayerEntity player) {

        return player.getPose() != Pose.CROUCHING;
    }

    public int getValue() {
        return value;
    }

    public CurrencyItem getLessValuable() {
        return sortedItems.lower(this);
    }

    public CurrencyItem getMoreValuable() {
        return sortedItems.higher(this);
    }

    public int getQuantity(CurrencyItem other, int otherQuantity) {
        return other.value * otherQuantity / this.value;
    }

    @Override
    public int compareTo(CurrencyItem currency) {
        return value - currency.value;
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {

        ItemStack heldItem = player.getItemInHand(hand);

        // Make sure it's actually currency
        if (heldItem.getItem() instanceof CurrencyItem) {

            CurrencyItem currencyItem = (CurrencyItem) heldItem.getItem();
            CurrencyItem moreValuable = currencyItem.getMoreValuable();

            // If there is no more valuable currency or the currencies are not evenly divisible, stop
            // I don't want to convert to multiple types of coins
            if (moreValuable == null || moreValuable.value % currencyItem.value != 0) {
                return ActionResult.fail(heldItem);
            }
            
            int numRequiredEach = moreValuable.value / currencyItem.value;

            // Find all currency items of same type in inventory
            int itemsFound = 0;
            List<ItemStack> foundStacks = new LinkedList<ItemStack>();
            for(int slot = 0; slot < player.inventory.items.size(); ++slot) {
                ItemStack nextItem = player.inventory.items.get(slot);
                if (!player.inventory.items.get(slot).isEmpty() &&
                        ItemStack.isSame(heldItem, nextItem)
                        && player.inventory.selected != slot) { // Ignore selected slot until end
                    foundStacks.add(nextItem);
                    itemsFound += nextItem.getCount();
                }
            }

            // Use items from selected slot last
            itemsFound += heldItem.getCount();
            foundStacks.add(heldItem);

            // Determine how many new coins to create. As many as possible if sneaking, only one otherwise
            int numNewCoins;
            int numRequired;
            if(player.getPose() == Pose.CROUCHING) { // TODO: Detect if Sneak keybind is pressed instead
                numNewCoins = itemsFound / numRequiredEach;
                numRequired = numRequiredEach * numNewCoins;
            } else {
                numNewCoins = 1;
                numRequired = numRequiredEach;
            }

            // Only convert coins there are enough of them
            if(itemsFound >= numRequired && numNewCoins > 0) {

                // Start removing coins from stacks in order
                for (ItemStack stack : foundStacks) {
                    if (numRequired >= stack.getCount()) {
                        numRequired -= stack.getCount();
                        // If I just shrink the stacks, it removes any newly added items that appear in the selected slot
                        // ¯\_(ツ)_/¯
                        player.inventory.removeItem(stack);
                    } else {
                        stack.shrink(numRequired);
                        break;
                    }
                }

                // Add all new coins
                ItemStack newItem = new ItemStack(moreValuable, numNewCoins);
                if(!player.inventory.add(newItem)) {
                    player.drop(newItem, false);
                }

                return ActionResult.consume(heldItem);
            }
        }

        // TODO: Make GUI work instead
        /*if (player instanceof ServerPlayerEntity) {
            System.out.println("Item opens menu");
            player.openMenu(getMenuProvider(world, BlockPos.ZERO, heldItem));
            return ActionResult.success(heldItem);
        }*/

        return ActionResult.pass(player.getItemInHand(hand));
    }

    public INamedContainerProvider getMenuProvider(World world, BlockPos pos, ItemStack item) {
        return new SimpleNamedContainerProvider((id, inventory, entity) -> {
            return new CurrencyConverterContainer(id, inventory, item, IWorldPosCallable.create(world, pos));
        }, CurrencyConverterContainer.TITLE);
    }

    @Override
    public boolean onEntitySwing(ItemStack stack, LivingEntity entity) {

        if (stack.getItem() instanceof CurrencyItem && entity instanceof PlayerEntity &&
                entity.getPose() == Pose.CROUCHING) {
            CurrencyItem currencyItem = (CurrencyItem) stack.getItem();
            CurrencyItem lessValuable = currencyItem.getLessValuable();

            // Make sure there is a less valuable currency and the currencies are evenly divisible
            // I don't want to convert to multiple types of coins
            if (lessValuable != null && currencyItem.value % lessValuable.value == 0) {

                // Remove one held coin
                stack.shrink(1);
                
                // Add all new coins
                PlayerEntity player = (PlayerEntity)entity;
                int numNewCoins = currencyItem.value / lessValuable.value;
                ItemStack newItem = new ItemStack(lessValuable, numNewCoins);
                if(!player.inventory.add(newItem)) {
                    player.drop(newItem, false);
                }
                return true;
            }
        }
        return super.onEntitySwing(stack, entity);
    }
}