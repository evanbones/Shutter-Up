package com.evandev.shutterup;

import net.fabricmc.api.ModInitializer;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;

public class ShutterUp implements ModInitializer {

    @Override
    public void onInitialize() {
        // Register Blocks
        ModRegistry.BLOCKS.forEach((name, blockSupplier) -> {
            Registry.register(BuiltInRegistries.BLOCK, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, name), blockSupplier.get());
        });

        // Register Items
        ModRegistry.ITEMS.forEach((name, itemSupplier) -> {
            Registry.register(BuiltInRegistries.ITEM, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, name), itemSupplier.get());
        });

        CommonClass.init();
    }
}