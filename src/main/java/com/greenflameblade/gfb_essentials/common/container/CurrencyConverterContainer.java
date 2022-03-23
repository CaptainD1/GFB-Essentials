package com.greenflameblade.gfb_essentials.common.container;

import com.greenflameblade.gfb_essentials.common.item.CurrencyItem;
import com.greenflameblade.gfb_essentials.core.init.ContainerTypeInit;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.SSetSlotPacket;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

public class CurrencyConverterContainer extends Container {

    public static final TranslationTextComponent TITLE = new TranslationTextComponent("container.gfb_essentials.currency_converter_title");

    private final Inventory inventory = new Inventory(3);
    private final PlayerEntity player;
    private final IWorldPosCallable access;

    public CurrencyConverterContainer(int id, PlayerInventory playerInventory, PacketBuffer buffer) {
        this(id, playerInventory);
    }

    public CurrencyConverterContainer(int id, PlayerInventory playerInventory) {
        this(id, playerInventory, ItemStack.EMPTY, IWorldPosCallable.NULL);
    }

    public CurrencyConverterContainer(int id, PlayerInventory playerInventory, ItemStack activatingItem, IWorldPosCallable callable) {
        super(ContainerTypeInit.CURRENCY_CONVERTER.get(), id);
        player = playerInventory.player;
        access = callable;

        // Coin center slot
        this.addSlot(new Slot(this.inventory, 0, 80, 34) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return stack.getItem() instanceof CurrencyItem;
            }
        });

        // Coin right slot
        this.addSlot(new Slot(this.inventory, 2, 98, 34) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return false;
            }
        });

        // Coin left slot
        this.addSlot(new Slot(this.inventory, 1, 62, 34) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return false;
            }
        });

        // Player inventory
        for (int y = 0; y < 3; ++y) {
            for (int x = 0; x < 9; ++x) {
                int posX = 8 + x * 18;
                int posY = 84 + y * 18;
                this.addSlot(new Slot(playerInventory, x + y * 9 + 9, posX, posY));
            }
        }

        // Player hotbar
        for (int x = 0; x < 9; ++x) {
            this.addSlot(new Slot(playerInventory, x, 8 + x * 18, 142));
        }

        inventory.setItem(0, activatingItem);
        playerInventory.removeItem(activatingItem);
    }

    @Override
    public boolean stillValid(PlayerEntity player) {
        return this.inventory.stillValid(player);
    }

    @Override
    public ItemStack quickMoveStack(PlayerEntity player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();

            final int inventorySize = 3;
            final int playerInventoryEnd = inventorySize + 27;
            final int playerHotbarEnd = playerInventoryEnd + 9;

            if (index == 1) {
                if (!this.moveItemStackTo(itemstack1, inventorySize, playerHotbarEnd, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickCraft(itemstack1, itemstack);
            } else if (index != 0) {
                if (!this.moveItemStackTo(itemstack1, inventorySize, playerHotbarEnd, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemstack1, inventorySize, playerHotbarEnd, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, itemstack1);
        }

        return itemstack;
    }

    @Override
    public void slotsChanged(IInventory inventory) {
        System.out.println("Slots changed");
        this.access.execute((world, pos) -> {
            updateCoinOutput(this.containerId, world, player);
        });
    }
    private void updateCoinOutput(int id, World world, PlayerEntity player) {
        if(!world.isClientSide) {

            ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;
            ItemStack lowerItemStack = ItemStack.EMPTY;
            ItemStack higherItemStack = ItemStack.EMPTY;

            if (inventory.getItem(0).getItem() instanceof CurrencyItem) {
                CurrencyItem inputCurrency = (CurrencyItem)inventory.getItem(0).getItem();
                int inputQuantity = inventory.getItem(0).getCount();
                CurrencyItem higherCurrency = inputCurrency.getMoreValuable();
                CurrencyItem lowerCurrency = inputCurrency.getLessValuable();

                int higherQuantity = higherCurrency.getQuantity(inputCurrency, inputQuantity);
                int lowerQuantity = inputCurrency.getValue() / lowerCurrency.getValue();

                lowerItemStack = lowerCurrency != null ? new ItemStack(lowerCurrency, lowerQuantity) : ItemStack.EMPTY;
                higherItemStack = higherCurrency != null ? new ItemStack(higherCurrency, higherQuantity) : ItemStack.EMPTY;
            }

            inventory.setItem(1, lowerItemStack);
            inventory.setItem(2, higherItemStack);
            serverPlayer.connection.send(new SSetSlotPacket(id, 1, lowerItemStack));
            serverPlayer.connection.send(new SSetSlotPacket(id, 2, higherItemStack));
        }
    }

    public void removed(PlayerEntity player) {
        super.removed(player);
        this.access.execute((world, pos) -> {
            this.inventory.setItem(1, ItemStack.EMPTY);
            this.inventory.setItem(2, ItemStack.EMPTY);
            this.clearContainer(player, world, inventory);
        });
    }
}
