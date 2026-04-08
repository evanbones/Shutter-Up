package com.evandev.shutterup.block;

import com.evandev.shutterup.ModRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class WeatheringShutterBlock extends ShutterBlock implements WeatheringCopper {
    private final WeatherState weatherState;

    public WeatheringShutterBlock(WeatherState weatherState, BlockSetType type, Properties properties) {
        super(type, properties);
        this.weatherState = weatherState;
    }

    @Override
    public void randomTick(@NotNull BlockState state, @NotNull ServerLevel level, @NotNull BlockPos pos, @NotNull RandomSource random) {
        this.onRandomTick(state, level, pos, random);
    }

    @Override
    public boolean isRandomlyTicking(@NotNull BlockState state) {
        return this.getNext(state).isPresent();
    }

    @Override
    public @NotNull WeatherState getAge() {
        return this.weatherState;
    }

    @Override
    public @NotNull Optional<BlockState> getNext(BlockState state) {
        Block nextBlock = null;
        Block currentBlock = state.getBlock();

        if (currentBlock == ModRegistry.COPPER_SHUTTER.get()) nextBlock = ModRegistry.EXPOSED_COPPER_SHUTTER.get();
        else if (currentBlock == ModRegistry.EXPOSED_COPPER_SHUTTER.get())
            nextBlock = ModRegistry.WEATHERED_COPPER_SHUTTER.get();
        else if (currentBlock == ModRegistry.WEATHERED_COPPER_SHUTTER.get())
            nextBlock = ModRegistry.OXIDIZED_COPPER_SHUTTER.get();

        return Optional.ofNullable(nextBlock).map(block -> block.withPropertiesOf(state));
    }
}