package com.evandev.shutterup;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.registry.OxidizableBlocksRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTabs;

public class ShutterUp implements ModInitializer {

    @Override
    public void onInitialize() {
        // Register Blocks
        ModRegistry.BLOCKS.forEach((name, blockSupplier) ->
                Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation(Constants.MOD_ID, name), blockSupplier.get()));

        // Register Items
        ModRegistry.ITEMS.forEach((name, itemSupplier) ->
                Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(Constants.MOD_ID, name), itemSupplier.get()));

        // Register Sounds
        ModSounds.SOUNDS.forEach((name, sound) -> Registry.register(BuiltInRegistries.SOUND_EVENT, new ResourceLocation(Constants.MOD_ID, name), sound));
        ModSounds.init();

        UseBlockCallback.EVENT.register(CommonClass::onRightClickBlock);

        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.BUILDING_BLOCKS).register(content ->
                ModRegistry.ITEMS.forEach((name, itemSupplier) -> content.accept(itemSupplier.get())));

        registerWeathering();

        CommonClass.init();
    }

    public void registerWeathering() {
        OxidizableBlocksRegistry.registerOxidizableBlockPair(ModRegistry.COPPER_SHUTTER.get(), ModRegistry.EXPOSED_COPPER_SHUTTER.get());
        OxidizableBlocksRegistry.registerOxidizableBlockPair(ModRegistry.EXPOSED_COPPER_SHUTTER.get(), ModRegistry.WEATHERED_COPPER_SHUTTER.get());
        OxidizableBlocksRegistry.registerOxidizableBlockPair(ModRegistry.WEATHERED_COPPER_SHUTTER.get(), ModRegistry.OXIDIZED_COPPER_SHUTTER.get());

        OxidizableBlocksRegistry.registerWaxableBlockPair(ModRegistry.COPPER_SHUTTER.get(), ModRegistry.WAXED_COPPER_SHUTTER.get());
        OxidizableBlocksRegistry.registerWaxableBlockPair(ModRegistry.EXPOSED_COPPER_SHUTTER.get(), ModRegistry.WAXED_EXPOSED_COPPER_SHUTTER.get());
        OxidizableBlocksRegistry.registerWaxableBlockPair(ModRegistry.WEATHERED_COPPER_SHUTTER.get(), ModRegistry.WAXED_WEATHERED_COPPER_SHUTTER.get());
        OxidizableBlocksRegistry.registerWaxableBlockPair(ModRegistry.OXIDIZED_COPPER_SHUTTER.get(), ModRegistry.WAXED_OXIDIZED_COPPER_SHUTTER.get());
    }
}