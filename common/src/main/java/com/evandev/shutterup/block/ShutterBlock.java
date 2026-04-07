package com.evandev.shutterup.block;

import com.evandev.shutterup.ModSounds;
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
import net.minecraft.world.level.block.state.properties.*;
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
    public static final EnumProperty<AttachFace> FACE = BlockStateProperties.ATTACH_FACE;
    public static final BooleanProperty OPEN = BlockStateProperties.OPEN;
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public static final BooleanProperty BLOCKED_LEFT = BooleanProperty.create("blocked_left");
    public static final BooleanProperty BLOCKED_RIGHT = BooleanProperty.create("blocked_right");

    protected static final VoxelShape NORTH_SHAPE = Block.box(0.0D, 0.0D, 14.0D, 16.0D, 16.0D, 16.0D);
    protected static final VoxelShape SOUTH_SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 2.0D);
    protected static final VoxelShape EAST_SHAPE = Block.box(0.0D, 0.0D, 0.0D, 2.0D, 16.0D, 16.0D);
    protected static final VoxelShape WEST_SHAPE = Block.box(14.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);

    protected static final VoxelShape FLOOR_SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D);
    protected static final VoxelShape CEILING_SHAPE = Block.box(0.0D, 14.0D, 0.0D, 16.0D, 16.0D, 16.0D);

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

    protected static final VoxelShape FLOOR_Z_OPEN_SHAPE = Shapes.or(Block.box(-6.0D, 0.0D, 0.0D, 2.0D, 2.0D, 16.0D), Block.box(14.0D, 0.0D, 0.0D, 22.0D, 2.0D, 16.0D));
    protected static final VoxelShape FLOOR_Z_OPEN_NEG_BLOCKED = Shapes.or(Block.box(0.0D, 0.0D, 0.0D, 2.0D, 8.0D, 16.0D), Block.box(14.0D, 0.0D, 0.0D, 22.0D, 2.0D, 16.0D));
    protected static final VoxelShape FLOOR_Z_OPEN_POS_BLOCKED = Shapes.or(Block.box(-6.0D, 0.0D, 0.0D, 2.0D, 2.0D, 16.0D), Block.box(14.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D));
    protected static final VoxelShape FLOOR_Z_OPEN_BOTH_BLOCKED = Shapes.or(Block.box(0.0D, 0.0D, 0.0D, 2.0D, 8.0D, 16.0D), Block.box(14.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D));

    protected static final VoxelShape FLOOR_X_OPEN_SHAPE = Shapes.or(Block.box(0.0D, 0.0D, -6.0D, 16.0D, 2.0D, 2.0D), Block.box(0.0D, 0.0D, 14.0D, 16.0D, 2.0D, 22.0D));
    protected static final VoxelShape FLOOR_X_OPEN_NEG_BLOCKED = Shapes.or(Block.box(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 2.0D), Block.box(0.0D, 0.0D, 14.0D, 16.0D, 2.0D, 22.0D));
    protected static final VoxelShape FLOOR_X_OPEN_POS_BLOCKED = Shapes.or(Block.box(0.0D, 0.0D, -6.0D, 16.0D, 2.0D, 2.0D), Block.box(0.0D, 0.0D, 14.0D, 16.0D, 8.0D, 16.0D));
    protected static final VoxelShape FLOOR_X_OPEN_BOTH_BLOCKED = Shapes.or(Block.box(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 2.0D), Block.box(0.0D, 0.0D, 14.0D, 16.0D, 8.0D, 16.0D));

    protected static final VoxelShape CEILING_Z_OPEN_SHAPE = Shapes.or(Block.box(-6.0D, 14.0D, 0.0D, 2.0D, 16.0D, 16.0D), Block.box(14.0D, 14.0D, 0.0D, 22.0D, 16.0D, 16.0D));
    protected static final VoxelShape CEILING_Z_OPEN_NEG_BLOCKED = Shapes.or(Block.box(0.0D, 8.0D, 0.0D, 2.0D, 16.0D, 16.0D), Block.box(14.0D, 14.0D, 0.0D, 22.0D, 16.0D, 16.0D));
    protected static final VoxelShape CEILING_Z_OPEN_POS_BLOCKED = Shapes.or(Block.box(-6.0D, 14.0D, 0.0D, 2.0D, 16.0D, 16.0D), Block.box(14.0D, 8.0D, 0.0D, 16.0D, 16.0D, 16.0D));
    protected static final VoxelShape CEILING_Z_OPEN_BOTH_BLOCKED = Shapes.or(Block.box(0.0D, 8.0D, 0.0D, 2.0D, 16.0D, 16.0D), Block.box(14.0D, 8.0D, 0.0D, 16.0D, 16.0D, 16.0D));

    protected static final VoxelShape CEILING_X_OPEN_SHAPE = Shapes.or(Block.box(0.0D, 14.0D, -6.0D, 16.0D, 16.0D, 2.0D), Block.box(0.0D, 14.0D, 14.0D, 16.0D, 16.0D, 22.0D));
    protected static final VoxelShape CEILING_X_OPEN_NEG_BLOCKED = Shapes.or(Block.box(0.0D, 8.0D, 0.0D, 16.0D, 16.0D, 2.0D), Block.box(0.0D, 14.0D, 14.0D, 16.0D, 16.0D, 22.0D));
    protected static final VoxelShape CEILING_X_OPEN_POS_BLOCKED = Shapes.or(Block.box(0.0D, 14.0D, -6.0D, 16.0D, 16.0D, 2.0D), Block.box(0.0D, 8.0D, 14.0D, 16.0D, 16.0D, 16.0D));
    protected static final VoxelShape CEILING_X_OPEN_BOTH_BLOCKED = Shapes.or(Block.box(0.0D, 8.0D, 0.0D, 16.0D, 16.0D, 2.0D), Block.box(0.0D, 8.0D, 14.0D, 16.0D, 16.0D, 16.0D));

    public final BlockSetType type;

    public ShutterBlock(BlockSetType type, Properties properties) {
        super(properties);
        this.type = type;
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(FACE, AttachFace.WALL)
                .setValue(OPEN, false)
                .setValue(POWERED, false)
                .setValue(WATERLOGGED, false)
                .setValue(BLOCKED_LEFT, false)
                .setValue(BLOCKED_RIGHT, false));
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        AttachFace face = state.getValue(FACE);
        Direction facing = state.getValue(FACING);
        boolean open = state.getValue(OPEN);
        boolean left = state.getValue(BLOCKED_LEFT);
        boolean right = state.getValue(BLOCKED_RIGHT);

        if (face == AttachFace.FLOOR || face == AttachFace.CEILING) {
            if (open) {
                boolean isZAxis = facing.getAxis() == Direction.Axis.Z;
                boolean isCeiling = face == AttachFace.CEILING;

                if (left && right) {
                    return isCeiling ? (isZAxis ? CEILING_Z_OPEN_BOTH_BLOCKED : CEILING_X_OPEN_BOTH_BLOCKED)
                            : (isZAxis ? FLOOR_Z_OPEN_BOTH_BLOCKED : FLOOR_X_OPEN_BOTH_BLOCKED);
                }

                boolean blockNeg = (facing == Direction.NORTH && left) || (facing == Direction.SOUTH && right) || (facing == Direction.EAST && left) || (facing == Direction.WEST && right);
                boolean blockPos = (facing == Direction.NORTH && right) || (facing == Direction.SOUTH && left) || (facing == Direction.EAST && right) || (facing == Direction.WEST && left);

                if (blockNeg) {
                    return isCeiling ? (isZAxis ? CEILING_Z_OPEN_NEG_BLOCKED : CEILING_X_OPEN_NEG_BLOCKED)
                            : (isZAxis ? FLOOR_Z_OPEN_NEG_BLOCKED : FLOOR_X_OPEN_NEG_BLOCKED);
                } else if (blockPos) {
                    return isCeiling ? (isZAxis ? CEILING_Z_OPEN_POS_BLOCKED : CEILING_X_OPEN_POS_BLOCKED)
                            : (isZAxis ? FLOOR_Z_OPEN_POS_BLOCKED : FLOOR_X_OPEN_POS_BLOCKED);
                }

                return isCeiling ? (isZAxis ? CEILING_Z_OPEN_SHAPE : CEILING_X_OPEN_SHAPE)
                        : (isZAxis ? FLOOR_Z_OPEN_SHAPE : FLOOR_X_OPEN_SHAPE);
            }
            return face == AttachFace.FLOOR ? FLOOR_SHAPE : CEILING_SHAPE;
        }

        if (open) {
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

        return switch (facing) {
            case SOUTH -> SOUTH_SHAPE;
            case EAST -> EAST_SHAPE;
            case WEST -> WEST_SHAPE;
            default -> NORTH_SHAPE;
        };
    }

    @Override
    protected @NotNull InteractionResult useWithoutItem(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull BlockHitResult hit) {
        if (!this.type.canOpenByHand()) {
            return InteractionResult.PASS;
        }
        return toggleShutter(state, level, pos, player);
    }

    public InteractionResult toggleShutter(BlockState state, Level level, BlockPos pos, @Nullable Player player) {
        boolean willOpen = !state.getValue(OPEN);
        toggleWithConnected(state, level, pos, willOpen, player);
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    public void toggleWithConnected(BlockState state, Level level, BlockPos pos, boolean open, @Nullable Player player) {
        BlockState newState = calculateBlockedState(state.setValue(OPEN, open), level, pos);
        level.setBlock(pos, newState, 10);
        updateDiagonalNeighbors(level, pos, newState);
        this.playOpenCloseSound(level, pos, open, player);
        level.gameEvent(player, open ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE, pos);

        Direction.Axis propagationAxis = state.getValue(FACE) == AttachFace.WALL ? Direction.Axis.Y : state.getValue(FACING).getClockWise().getAxis();

        for (Direction dir : Direction.values()) {
            if (dir.getAxis() == propagationAxis) {
                BlockPos current = pos.relative(dir);
                while (level.getBlockState(current).getBlock() instanceof ShutterBlock
                        && level.getBlockState(current).getValue(FACING) == state.getValue(FACING)
                        && level.getBlockState(current).getValue(FACE) == state.getValue(FACE)
                        && level.getBlockState(current).getValue(OPEN) != open) {

                    BlockState nextState = level.getBlockState(current);
                    nextState = calculateBlockedState(nextState.setValue(OPEN, open), level, current);
                    level.setBlock(current, nextState, 10);
                    updateDiagonalNeighbors(level, current, nextState);
                    current = current.relative(dir);
                }
            }
        }
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        FluidState fluidState = context.getLevel().getFluidState(context.getClickedPos());
        Direction clickedFace = context.getClickedFace();
        AttachFace face = clickedFace == Direction.UP ? AttachFace.FLOOR : (clickedFace == Direction.DOWN ? AttachFace.CEILING : AttachFace.WALL);

        BlockState state = this.defaultBlockState()
                .setValue(FACING, context.getHorizontalDirection().getOpposite())
                .setValue(FACE, face)
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
                    toggleWithConnected(state, level, pos, hasSignal, null);
                } else {
                    level.setBlock(pos, state.setValue(POWERED, hasSignal), 2);
                }
            }
        }
    }

    private void playOpenCloseSound(Level level, BlockPos pos, boolean open, @Nullable Player player) {
        level.playSound(player, pos, open ? ModSounds.SHUTTER_OPEN : ModSounds.SHUTTER_CLOSE, SoundSource.BLOCKS, 1.0F, level.getRandom().nextFloat() * 0.1F + 0.9F);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, FACE, OPEN, POWERED, WATERLOGGED, BLOCKED_LEFT, BLOCKED_RIGHT);
    }

    @Override
    public @NotNull FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }
}