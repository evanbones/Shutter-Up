package com.evandev.shutterup;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@Mod(Constants.MOD_ID)
public class ShutterUp {

    public ShutterUp() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        DeferredRegister<Block> BLOCK_REGISTER = DeferredRegister.create(ForgeRegistries.BLOCKS, Constants.MOD_ID);
        DeferredRegister<Item> ITEM_REGISTER = DeferredRegister.create(ForgeRegistries.ITEMS, Constants.MOD_ID);
        DeferredRegister<SoundEvent> SOUND_REGISTER = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Constants.MOD_ID);

        ModRegistry.BLOCKS.forEach(BLOCK_REGISTER::register);
        ModRegistry.ITEMS.forEach(ITEM_REGISTER::register);
        ModSounds.SOUNDS.forEach((name, sound) -> SOUND_REGISTER.register(name, () -> sound));

        BLOCK_REGISTER.register(modEventBus);
        ITEM_REGISTER.register(modEventBus);
        SOUND_REGISTER.register(modEventBus);

        modEventBus.addListener(this::addCreative);

        MinecraftForge.EVENT_BUS.register(this);

        CommonClass.init();
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) {
            ModRegistry.ITEMS.forEach((name, itemSupplier) -> event.accept(itemSupplier.get()));
        }
    }

    @SubscribeEvent
    public void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        ItemStack itemInHand = event.getItemStack();

        if (itemInHand.is(Items.HONEYCOMB)) {
            Block block = event.getLevel().getBlockState(event.getPos()).getBlock();
            Block waxedBlock = null;

            if (block == ModRegistry.COPPER_SHUTTER.get()) waxedBlock = ModRegistry.WAXED_COPPER_SHUTTER.get();
            else if (block == ModRegistry.EXPOSED_COPPER_SHUTTER.get())
                waxedBlock = ModRegistry.WAXED_EXPOSED_COPPER_SHUTTER.get();
            else if (block == ModRegistry.WEATHERED_COPPER_SHUTTER.get())
                waxedBlock = ModRegistry.WAXED_WEATHERED_COPPER_SHUTTER.get();
            else if (block == ModRegistry.OXIDIZED_COPPER_SHUTTER.get())
                waxedBlock = ModRegistry.WAXED_OXIDIZED_COPPER_SHUTTER.get();

            if (waxedBlock != null) {
                Player player = event.getEntity();
                Level level = event.getLevel();
                BlockPos pos = event.getPos();

                if (player instanceof ServerPlayer serverPlayer) {
                    CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger(serverPlayer, pos, itemInHand);
                }

                player.swing(event.getHand());
                if (!player.isCreative()) itemInHand.shrink(1);

                level.setBlockAndUpdate(pos, waxedBlock.withPropertiesOf(level.getBlockState(pos)));

                level.levelEvent(player, 3003, pos, 0);

                event.setCanceled(true);
                event.setCancellationResult(InteractionResult.sidedSuccess(level.isClientSide));
                return;
            }
        }

        InteractionResult result = CommonClass.onRightClickBlock(event.getEntity(), event.getLevel(), event.getHand(), event.getHitVec());
        if (result == InteractionResult.SUCCESS) {
            event.setCanceled(true);
            event.setCancellationResult(InteractionResult.SUCCESS);
        }
    }

    @SubscribeEvent
    public void onAxeUse(BlockEvent.BlockToolModificationEvent event) {
        if (event.isSimulated()) return;

        BlockState state = event.getState();
        Block block = state.getBlock();

        if (event.getToolAction() == ToolActions.AXE_SCRAPE) {
            Block resultBlock = null;
            if (block == ModRegistry.EXPOSED_COPPER_SHUTTER.get()) resultBlock = ModRegistry.COPPER_SHUTTER.get();
            else if (block == ModRegistry.WEATHERED_COPPER_SHUTTER.get())
                resultBlock = ModRegistry.EXPOSED_COPPER_SHUTTER.get();
            else if (block == ModRegistry.OXIDIZED_COPPER_SHUTTER.get())
                resultBlock = ModRegistry.WEATHERED_COPPER_SHUTTER.get();

            if (resultBlock != null) {
                event.setFinalState(resultBlock.withPropertiesOf(state));
            }
        }

        if (event.getToolAction() == ToolActions.AXE_WAX_OFF) {
            Block resultBlock = null;
            if (block == ModRegistry.WAXED_COPPER_SHUTTER.get()) resultBlock = ModRegistry.COPPER_SHUTTER.get();
            else if (block == ModRegistry.WAXED_EXPOSED_COPPER_SHUTTER.get())
                resultBlock = ModRegistry.EXPOSED_COPPER_SHUTTER.get();
            else if (block == ModRegistry.WAXED_WEATHERED_COPPER_SHUTTER.get())
                resultBlock = ModRegistry.WEATHERED_COPPER_SHUTTER.get();
            else if (block == ModRegistry.WAXED_OXIDIZED_COPPER_SHUTTER.get())
                resultBlock = ModRegistry.OXIDIZED_COPPER_SHUTTER.get();

            if (resultBlock != null) {
                event.setFinalState(resultBlock.withPropertiesOf(state));
            }
        }
    }
}