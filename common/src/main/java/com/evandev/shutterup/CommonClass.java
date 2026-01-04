package com.evandev.shutterup;

import com.evandev.shutterup.compat.ShutterUpEveryCompat;
import com.evandev.shutterup.platform.Services;
import net.mehvahdjukaar.every_compat.api.EveryCompatAPI;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import com.evandev.shutterup.block.ShutterBlock;

public class CommonClass {

    public static void init() {
        if (Services.PLATFORM.isModLoaded("everycomp")) {
            EveryCompatAPI.registerModule(new ShutterUpEveryCompat());
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
            if (frontState.getValue(ShutterBlock.OPEN) && frontState.getValue(ShutterBlock.FACING) == clickedFace) {
                if (!level.isClientSide) {
                    BlockState newState = frontState.setValue(ShutterBlock.OPEN, false);
                    level.setBlock(frontPos, newState, 3);
                    shutterBlock.updateDiagonalNeighbors(level, frontPos, newState);
                    level.playSound(null, frontPos, shutterBlock.type.doorClose(), net.minecraft.sounds.SoundSource.BLOCKS, 1.0f, 1.0f);
                }
                return InteractionResult.SUCCESS;
            }
        }

        if (!clickedState.isSolidRender(level, clickedPos)) {
            BlockPos behindPos = clickedPos.relative(clickedFace.getOpposite());
            BlockState behindState = level.getBlockState(behindPos);

            if (behindState.getBlock() instanceof ShutterBlock shutterBlock) {
                if (behindState.getValue(ShutterBlock.FACING) == clickedFace.getOpposite()) {
                    if (!level.isClientSide) {
                        boolean isOpen = behindState.getValue(ShutterBlock.OPEN);
                        BlockState newState = behindState.setValue(ShutterBlock.OPEN, !isOpen);

                        level.setBlock(behindPos, newState, 3);
                        shutterBlock.updateDiagonalNeighbors(level, behindPos, newState);

                        level.playSound(null, behindPos,
                                isOpen ? shutterBlock.type.doorClose() : shutterBlock.type.doorOpen(),
                                net.minecraft.sounds.SoundSource.BLOCKS, 1.0f, 1.0f);
                    }
                    return InteractionResult.SUCCESS;
                }
            }
        }

        return InteractionResult.PASS;
    }
}