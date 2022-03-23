package com.greenflameblade.gfb_essentials.core.init;

import com.greenflameblade.gfb_essentials.GFBEssentials;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class MainInit {

    public static void register(IEventBus eventBus) {
        ItemInit.ITEMS.register(eventBus);
        BlockInit.BLOCKS.register(eventBus);
        ContainerTypeInit.CONTAINERS.register(eventBus);
    }

    @Mod.EventBusSubscriber(modid = GFBEssentials.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class Client {

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            ContainerTypeInit.registerScreens(event);
            RenderTypeLookup.setRenderLayer(BlockInit.ENCHANTED_GLASS.get(), RenderType.translucent());
            RenderTypeLookup.setRenderLayer(BlockInit.MOLLYCODDLE_VINE.get(), RenderType.cutout());
        }
    }
}
