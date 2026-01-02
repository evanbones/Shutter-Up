package com.evandev.shutterup;

import com.evandev.shutterup.compat.ShutterUpEveryCompat;
import com.evandev.shutterup.platform.Services;
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
        }
    }

    public static InteractionResult onRightClickBlock(Player player, Level level, InteractionHand hand, BlockHitResult hitResult) {
        if (player.isSpectator()) return InteractionResult.PASS;

        BlockPos clickedPos = hitResult.getBlockPos();
        Direction clickedFace = hitResult.getDirection();

        BlockPos potentialShutterPos = clickedPos.relative(clickedFace);
        BlockState shutterState = level.getBlockState(potentialShutterPos);

        if (shutterState.getBlock() instanceof ShutterBlock) {
            if (shutterState.getValue(ShutterBlock.OPEN)) {
                if (shutterState.getValue(ShutterBlock.FACING) == clickedFace) {
                    if (!level.isClientSide) {
                        level.setBlock(potentialShutterPos, shutterState.setValue(ShutterBlock.OPEN, false), 3);
                        shutterState.getBlock().setPlacedBy(level, potentialShutterPos, shutterState, null, null);
                        level.playSound(null, potentialShutterPos, ((ShutterBlock)shutterState.getBlock()).type.doorClose(), net.minecraft.sounds.SoundSource.BLOCKS, 1.0f, 1.0f);
                    }
                    return InteractionResult.SUCCESS;
                }
            }
        }
        return InteractionResult.PASS;
    }
}