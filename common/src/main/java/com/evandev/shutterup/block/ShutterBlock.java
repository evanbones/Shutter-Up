package com.evandev.shutterup.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ShutterBlock extends Block implements SimpleWaterloggedBlock {
    public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty OPEN = BlockStateProperties.OPEN;
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public static final BooleanProperty BLOCKED_LEFT = BooleanProperty.create("blocked_left");
    public static final BooleanProperty BLOCKED_RIGHT = BooleanProperty.create("blocked_right");

    protected static final VoxelShape NORTH_SHAPE = Block.box(0.0D, 0.0D, 14.0D, 16.0D, 16.0D, 16.0D);
    protected static final VoxelShape SOUTH_SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 2.0D);
    protected static final VoxelShape EAST_SHAPE = Block.box(0.0D, 0.0D, 0.0D, 2.0D, 16.0D, 16.0D);
    protected static final VoxelShape WEST_SHAPE = Block.box(14.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);

    protected static final VoxelShape NORTH_OPEN_SHAPE = Shapes.or(Block.box(-6.0D, 0.0D, 14.0D, 2.0D, 16.0D, 16.0D), Block.box(14.0D, 0.0D, 14.0D, 22.0D, 16.0D, 16.0D));
    protected static final VoxelShape SOUTH_OPEN_SHAPE = Shapes.or(Block.box(-6.0D, 0.0D, 0.0D, 2.0D, 16.0D, 2.0D), Block.box(14.0D, 0.0D, 0.0D, 22.0D, 16.0D, 2.0D));
    protected static final VoxelShape EAST_OPEN_SHAPE = Shapes.or(Block.box(0.0D, 0.0D, -6.0D, 2.0D, 16.0D, 2.0D), Block.box(0.0D, 0.0D, 14.0D, 2.0D, 16.0D, 22.0D));
    protected static final VoxelShape WEST_OPEN_SHAPE = Shapes.or(Block.box(14.0D, 0.0D, -6.0D, 16.0D, 16.0D, 2.0D), Block.box(14.0D, 0.0D, 14.0D, 16.0D, 16.0D, 22.0D));

    protected static final VoxelShape NORTH_OPEN_LEFT_BLOCKED_SHAPE = Shapes.or(Block.box(0.0D, 0.0D, 8.0D, 2.0D, 16.0D, 16.0D), Block.box(14.0D, 0.0D, 14.0D, 22.0D, 16.0D, 16.0D));
    protected static final VoxelShape NORTH_OPEN_RIGHT_BLOCKED_SHAPE = Shapes.or(Block.box(-6.0D, 0.0D, 14.0D, 2.0D, 16.0D, 16.0D), Block.box(14.0D, 0.0D, 8.0D, 16.0D, 16.0D, 16.0D));
    protected static final VoxelShape NORTH_OPEN_BOTH_BLOCKED_SHAPE = Shapes.or(Block.box(0.0D, 0.0D, 8.0D, 2.0D, 16.0D, 16.0D), Block.box(14.0D, 0.0D, 8.0D, 16.0D, 16.0D, 16.0D));

    protected static final VoxelShape SOUTH_OPEN_LEFT_BLOCKED_SHAPE = Shapes.or(Block.box(14.0D, 0.0D, 0.0D, 16.0D, 16.0D, 8.0D), Block.box(-6.0D, 0.0D, 0.0D, 2.0D, 16.0D, 2.0D));
    protected static final VoxelShape SOUTH_OPEN_RIGHT_BLOCKED_SHAPE = Shapes.or(Block.box(14.0D, 0.0D, 0.0D, 22.0D, 16.0D, 2.0D), Block.box(0.0D, 0.0D, 0.0D, 2.0D, 16.0D, 8.0D));
    protected static final VoxelShape SOUTH_OPEN_BOTH_BLOCKED_SHAPE = Shapes.or(Block.box(14.0D, 0.0D, 0.0D, 16.0D, 16.0D, 8.0D), Block.box(0.0D, 0.0D, 0.0D, 2.0D, 16.0D, 8.0D));

    protected static final VoxelShape EAST_OPEN_LEFT_BLOCKED_SHAPE = Shapes.or(Block.box(0.0D, 0.0D, 0.0D, 8.0D, 16.0D, 2.0D), Block.box(0.0D, 0.0D, 14.0D, 2.0D, 16.0D, 22.0D));
    protected static final VoxelShape EAST_OPEN_RIGHT_BLOCKED_SHAPE = Shapes.or(Block.box(0.0D, 0.0D, -6.0D, 2.0D, 16.0D, 2.0D), Block.box(0.0D, 0.0D, 14.0D, 8.0D, 16.0D, 16.0D));
    protected static final VoxelShape EAST_OPEN_BOTH_BLOCKED_SHAPE = Shapes.or(Block.box(0.0D, 0.0D, 0.0D, 8.0D, 16.0D, 2.0D), Block.box(0.0D, 0.0D, 14.0D, 8.0D, 16.0D, 16.0D));

    protected static final VoxelShape WEST_OPEN_LEFT_BLOCKED_SHAPE = Shapes.or(Block.box(8.0D, 0.0D, 14.0D, 16.0D, 16.0D, 16.0D), Block.box(14.0D, 0.0D, -6.0D, 16.0D, 16.0D, 2.0D));
    protected static final VoxelShape WEST_OPEN_RIGHT_BLOCKED_SHAPE = Shapes.or(Block.box(14.0D, 0.0D, 14.0D, 16.0D, 16.0D, 22.0D), Block.box(8.0D, 0.0D, 0.0D, 16.0D, 16.0D, 2.0D));
    protected static final VoxelShape WEST_OPEN_BOTH_BLOCKED_SHAPE = Shapes.or(Block.box(8.0D, 0.0D, 14.0D, 16.0D, 16.0D, 16.0D), Block.box(8.0D, 0.0D, 0.0D, 16.0D, 16.0D, 2.0D));

    public final BlockSetType type;

    public ShutterBlock(BlockSetType type, Properties properties) {
        super(properties);
        this.type = type;
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(OPEN, false)
                .setValue(POWERED, false)
                .setValue(WATERLOGGED, false)
                .setValue(BLOCKED_LEFT, false)
                .setValue(BLOCKED_RIGHT, false));
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        if (state.getValue(OPEN)) {
            Direction facing = state.getValue(FACING);
            boolean left = state.getValue(BLOCKED_LEFT);
            boolean right = state.getValue(BLOCKED_RIGHT);

            if (left && right) {
                return switch (facing) {
                    case SOUTH -> SOUTH_OPEN_BOTH_BLOCKED_SHAPE;
                    case EAST -> EAST_OPEN_BOTH_BLOCKED_SHAPE;
                    case WEST -> WEST_OPEN_BOTH_BLOCKED_SHAPE;
                    default -> NORTH_OPEN_BOTH_BLOCKED_SHAPE;
                };
            } else if (left) {
                return switch (facing) {
                    case SOUTH -> SOUTH_OPEN_LEFT_BLOCKED_SHAPE;
                    case EAST -> EAST_OPEN_LEFT_BLOCKED_SHAPE;
                    case WEST -> WEST_OPEN_LEFT_BLOCKED_SHAPE;
                    default -> NORTH_OPEN_LEFT_BLOCKED_SHAPE;
                };
            } else if (right) {
                return switch (facing) {
                    case SOUTH -> SOUTH_OPEN_RIGHT_BLOCKED_SHAPE;
                    case EAST -> EAST_OPEN_RIGHT_BLOCKED_SHAPE;
                    case WEST -> WEST_OPEN_RIGHT_BLOCKED_SHAPE;
                    default -> NORTH_OPEN_RIGHT_BLOCKED_SHAPE;
                };
            }

            return switch (facing) {
                case SOUTH -> SOUTH_OPEN_SHAPE;
                case EAST -> EAST_OPEN_SHAPE;
                case WEST -> WEST_OPEN_SHAPE;
                default -> NORTH_OPEN_SHAPE;
            };
        }

        return switch (state.getValue(FACING)) {
            case SOUTH -> SOUTH_SHAPE;
            case EAST -> EAST_SHAPE;
            case WEST -> WEST_SHAPE;
            default -> NORTH_SHAPE;
        };
    }

    @Override
    protected @NotNull InteractionResult useWithoutItem(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull BlockHitResult hit) {
        return toggleShutter(state, level, pos, player);
    }

    public InteractionResult toggleShutter(BlockState state, Level level, BlockPos pos, Player player) {
        state = state.cycle(OPEN);
        state = calculateBlockedState(state, level, pos);
        level.setBlock(pos, state, 10);
        updateDiagonalNeighbors(level, pos, state);
        level.playSound(player, pos, state.getValue(OPEN) ? this.type.doorOpen() : this.type.doorClose(), SoundSource.BLOCKS, 1.0F, level.getRandom().nextFloat() * 0.1F + 0.9F);
        level.gameEvent(player, state.getValue(OPEN) ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE, pos);
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        FluidState fluidState = context.getLevel().getFluidState(context.getClickedPos());
        BlockState state = this.defaultBlockState()
                .setValue(FACING, context.getHorizontalDirection().getOpposite())
                .setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER)
                .setValue(POWERED, context.getLevel().hasNeighborSignal(context.getClickedPos()));

        return calculateBlockedState(state, context.getLevel(), context.getClickedPos());
    }

    @Override
    public @NotNull BlockState updateShape(BlockState state, @NotNull Direction direction, @NotNull BlockState neighborState, @NotNull LevelAccessor level, @NotNull BlockPos currentPos, @NotNull BlockPos neighborPos) {
        if (state.getValue(WATERLOGGED)) {
            level.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }
        return calculateBlockedState(state, level, currentPos);
    }

    private BlockState calculateBlockedState(BlockState state, LevelAccessor level, BlockPos pos) {
        Direction facing = state.getValue(FACING);
        Direction leftDir = facing.getCounterClockWise();
        Direction rightDir = facing.getClockWise();

        boolean blockedLeft = !level.getBlockState(pos.relative(leftDir)).getCollisionShape(level, pos.relative(leftDir)).isEmpty();
        boolean blockedRight = !level.getBlockState(pos.relative(rightDir)).getCollisionShape(level, pos.relative(rightDir)).isEmpty();

        if (!blockedLeft) {
            BlockPos leftDiagPos = pos.relative(leftDir).relative(facing.getOpposite());
            BlockState leftDiagState = level.getBlockState(leftDiagPos);
            if (leftDiagState.getBlock() instanceof ShutterBlock
                    && leftDiagState.getValue(FACING) == leftDir
                    && leftDiagState.getValue(OPEN)) {
                blockedLeft = true;
            }
        }

        if (!blockedRight) {
            BlockPos rightDiagPos = pos.relative(rightDir).relative(facing.getOpposite());
            BlockState rightDiagState = level.getBlockState(rightDiagPos);
            if (rightDiagState.getBlock() instanceof ShutterBlock
                    && rightDiagState.getValue(FACING) == rightDir
                    && rightDiagState.getValue(OPEN)) {
                blockedRight = true;
            }
        }

        return state.setValue(BLOCKED_LEFT, blockedLeft).setValue(BLOCKED_RIGHT, blockedRight);
    }

    public void updateDiagonalNeighbors(Level level, BlockPos pos, BlockState state) {
        Direction facing = state.getValue(FACING);
        BlockPos leftDiagPos = pos.relative(facing.getCounterClockWise()).relative(facing.getOpposite());
        BlockPos rightDiagPos = pos.relative(facing.getClockWise()).relative(facing.getOpposite());

        updateDiagonalNeighbor(level, leftDiagPos, pos);
        updateDiagonalNeighbor(level, rightDiagPos, pos);
    }

    private void updateDiagonalNeighbor(Level level, BlockPos targetPos, BlockPos sourcePos) {
        BlockState targetState = level.getBlockState(targetPos);
        if (targetState.getBlock() instanceof ShutterBlock) {
            BlockState newState = targetState.updateShape(Direction.UP, level.getBlockState(sourcePos), level, targetPos, sourcePos);
            if (newState != targetState) {
                level.setBlock(targetPos, newState, 2 | 16);
            }
        }
    }

    @Override
    public void onPlace(BlockState state, @NotNull Level level, @NotNull BlockPos pos, BlockState oldState, boolean isMoving) {
        if (!oldState.is(state.getBlock())) {
            updateDiagonalNeighbors(level, pos, state);
        }
        super.onPlace(state, level, pos, oldState, isMoving);
    }

    @Override
    public void onRemove(BlockState state, @NotNull Level level, @NotNull BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            updateDiagonalNeighbors(level, pos, state);
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }

    @Override
    public void neighborChanged(@NotNull BlockState state, Level level, @NotNull BlockPos pos, @NotNull Block neighborBlock, @NotNull BlockPos neighborPos, boolean movedByPiston) {
        if (!level.isClientSide) {
            boolean hasSignal = level.hasNeighborSignal(pos);
            if (hasSignal != state.getValue(POWERED)) {
                if (hasSignal != state.getValue(OPEN)) {
                    this.playOpenCloseSound(level, pos, hasSignal);
                    level.gameEvent(null, hasSignal ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE, pos);
                }
                BlockState newState = state.setValue(POWERED, hasSignal).setValue(OPEN, hasSignal);
                newState = calculateBlockedState(newState, level, pos);
                level.setBlock(pos, newState, 2);
                updateDiagonalNeighbors(level, pos, newState);
            }
        }
    }

    private void playOpenCloseSound(Level level, BlockPos pos, boolean open) {
        level.playSound(null, pos, open ? this.type.doorOpen() : this.type.doorClose(), SoundSource.BLOCKS, 1.0F, level.getRandom().nextFloat() * 0.1F + 0.9F);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, OPEN, POWERED, WATERLOGGED, BLOCKED_LEFT, BLOCKED_RIGHT);
    }

    @Override
    public @NotNull FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }
}