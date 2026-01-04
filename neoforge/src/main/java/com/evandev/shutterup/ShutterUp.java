package com.evandev.shutterup;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

@Mod(Constants.MOD_ID)
public class ShutterUp {

    public ShutterUp(IEventBus eventBus) {
        DeferredRegister<Block> BLOCK_REGISTER = DeferredRegister.create(BuiltInRegistries.BLOCK, Constants.MOD_ID);
        DeferredRegister<Item> ITEM_REGISTER = DeferredRegister.create(BuiltInRegistries.ITEM, Constants.MOD_ID);

        DeferredRegister<SoundEvent> SOUND_REGISTER = DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, Constants.MOD_ID);

        ModRegistry.BLOCKS.forEach(BLOCK_REGISTER::register);
        ModRegistry.ITEMS.forEach(ITEM_REGISTER::register);
        ModSounds.SOUNDS.forEach((name, sound) -> SOUND_REGISTER.register(name, () -> sound));

        BLOCK_REGISTER.register(eventBus);
        ITEM_REGISTER.register(eventBus);
        SOUND_REGISTER.register(eventBus);

        eventBus.addListener(this::addCreative);
        NeoForge.EVENT_BUS.register(this);

        CommonClass.init();
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) {
            ModRegistry.ITEMS.forEach((name, itemSupplier) -> event.accept(itemSupplier.get()));
        }
    }

    @SubscribeEvent
    public void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        InteractionResult result = CommonClass.onRightClickBlock(event.getEntity(), event.getLevel(), event.getHand(), event.getHitVec());
        if (result == InteractionResult.SUCCESS) {
            event.setCanceled(true);
            event.setCancellationResult(InteractionResult.SUCCESS);
        }
    }
}