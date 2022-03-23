package com.greenflameblade.gfb_essentials.core.init;

import com.greenflameblade.gfb_essentials.GFBEssentials;
import com.greenflameblade.gfb_essentials.client.screen.CurrencyConverterScreen;
import com.greenflameblade.gfb_essentials.common.container.CurrencyConverterContainer;

import net.minecraft.client.gui.ScreenManager;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ContainerTypeInit {

    public static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, GFBEssentials.MODID);

    public static final RegistryObject<ContainerType<CurrencyConverterContainer>> CURRENCY_CONVERTER = register("currency_converter", CurrencyConverterContainer::new);

    @OnlyIn(Dist.CLIENT)
    public static void registerScreens(FMLClientSetupEvent event) {
        ScreenManager.register(CURRENCY_CONVERTER.get(), CurrencyConverterScreen::new);
    }

    private static <T extends Container> RegistryObject<ContainerType<T>> register(String name, IContainerFactory<T> factory) {
        return CONTAINERS.register(name, () -> IForgeContainerType.create(factory));
    }
}
