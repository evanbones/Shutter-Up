package com.evandev.shutterup;

import com.evandev.shutterup.block.ShutterBlock;
import com.evandev.shutterup.compat.ShutterUpEveryCompat;
import com.evandev.shutterup.platform.Services;
import net.mehvahdjukaar.every_compat.api.EveryCompatAPI;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class CommonClass {

    public static void init() {
        if (Services.PLATFORM.isModLoaded("everycomp")) {
            EveryCompatRegistry.register();
        }
    }

    public static InteractionResult onRightClickBlock(Player player, Level level, InteractionHand hand, BlockHitResult hitResult) {
        if (player.isSpectator()) return InteractionResult.PASS;

        BlockPos clickedPos = hitResult.getBlockPos();
        Direction clickedFace = hitResult.getDirection();
        BlockState clickedState = level.getBlockState(clickedPos);

        BlockPos frontPos = clickedPos.relative(clickedFace);
        BlockState frontState = level.getBlockState(frontPos);

        if (frontState.getBlock() instanceof ShutterBlock shutterBlock) {
            if (!shutterBlock.type.canOpenByHand()) return InteractionResult.PASS;
            if (frontState.getValue(ShutterBlock.OPEN) && frontState.getValue(ShutterBlock.FACING) == clickedFace) {
                if (!level.isClientSide) {
                    shutterBlock.toggleWithConnected(frontState, level, frontPos, false, player, !player.isShiftKeyDown());
                }
                return InteractionResult.SUCCESS;
            }
        }

        if (!clickedState.isSolidRender(level, clickedPos)) {
            BlockPos behindPos = clickedPos.relative(clickedFace.getOpposite());
            BlockState behindState = level.getBlockState(behindPos);

            if (behindState.getBlock() instanceof ShutterBlock shutterBlock) {
                if (!shutterBlock.type.canOpenByHand()) return InteractionResult.PASS;
                if (behindState.getValue(ShutterBlock.FACING) == clickedFace.getOpposite()) {
                    if (!level.isClientSide) {
                        boolean wasOpen = behindState.getValue(ShutterBlock.OPEN);
                        shutterBlock.toggleWithConnected(behindState, level, behindPos, !wasOpen, player, !player.isShiftKeyDown());
                    }
                    return InteractionResult.SUCCESS;
                }
            }
        }

        return InteractionResult.PASS;
    }

    private static class EveryCompatRegistry {
        static void register() {
            EveryCompatAPI.registerModule(new ShutterUpEveryCompat());
        }
    }
}