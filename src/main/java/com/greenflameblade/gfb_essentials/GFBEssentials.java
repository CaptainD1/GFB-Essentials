package com.greenflameblade.gfb_essentials;

import com.greenflameblade.gfb_essentials.core.init.ItemInit;
import com.greenflameblade.gfb_essentials.core.init.MainInit;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(GFBEssentials.MODID)
public class GFBEssentials {
    
    public static final String MODID = "gfb_essentials";

    public static final ItemGroup GFB_TAB = new GFBTab("gfbtab");

    public GFBEssentials() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        
        MainInit.register(eventBus);
        eventBus.addListener(this::setup);

        MinecraftForge.EVENT_BUS.register(this);
    }

    public void setup(FMLCommonSetupEvent event) {

    }
    
    @SubscribeEvent
    public void clientLoad(FMLClientSetupEvent event) {
        
    }

    public static class GFBTab extends ItemGroup {

        public GFBTab(String label) {
            super(label);
        }

        @Override
        public ItemStack makeIcon() {
            return ItemInit.GOLD_COIN.get().getDefaultInstance();
        }

        @Override
        public void fillItemList(NonNullList<ItemStack> items) {
            items.addAll(0, ItemInit.getTabItems());
            super.fillItemList(items);
        }
    }
}
