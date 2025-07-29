package org.solstice.wordstones.content.block;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractWordstoneBlock extends BlockWithEntity {

	public static final VoxelShape
		BOTTOM_MODEL = Block.createCuboidShape(0, 0, 0, 16, 4, 16),
		BASE_MODEL = Block.createCuboidShape(2, 4, 2, 14, 26, 14),
		TOP_MODEL = Block.createCuboidShape(0, 26, 0, 16, 30, 16),
		CAP_MODEL = Block.createCuboidShape(2, 30, 2, 14, 32, 14),
		MODEL = VoxelShapes.union(BOTTOM_MODEL, BASE_MODEL, TOP_MODEL, CAP_MODEL);

	public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;
	public static final EnumProperty<DoubleBlockHalf> HALF = Properties.DOUBLE_BLOCK_HALF;

	public AbstractWordstoneBlock(Settings settings) {
		super(settings);
		this.setDefaultState(this.stateManager.getDefaultState()
			.with(FACING, Direction.NORTH)
			.with(HALF, DoubleBlockHalf.LOWER)
		);
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(FACING, HALF);
	}

	@Override
	protected BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}

	@Override
	protected BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
		DoubleBlockHalf doubleBlockHalf = state.get(HALF);
		if (direction.getAxis() != Direction.Axis.Y || doubleBlockHalf == DoubleBlockHalf.LOWER != (direction == Direction.UP) || neighborState.isOf(this) && neighborState.get(HALF) != doubleBlockHalf) {
			return doubleBlockHalf == DoubleBlockHalf.LOWER && direction == Direction.DOWN && !state.canPlaceAt(world, pos) ? Blocks.AIR.getDefaultState() : super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
		} else {
			return Blocks.AIR.getDefaultState();
		}
	}

	@Nullable
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		BlockPos blockPos = ctx.getBlockPos();
		World world = ctx.getWorld();
		return blockPos.getY() < world.getTopY() - 1 && world.getBlockState(blockPos.up()).canReplace(ctx) ? super.getPlacementState(ctx) : null;
	}

	@Override
	protected boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
		if (state.get(HALF) != DoubleBlockHalf.UPPER) return super.canPlaceAt(state, world, pos);

		BlockState belowState = world.getBlockState(pos.down());
		return belowState.isOf(this) && belowState.get(HALF) == DoubleBlockHalf.LOWER;
	}

	public static void placeAt(WorldAccess world, BlockState state, BlockPos pos, int flags) {
		BlockPos blockPos = pos.up();
		world.setBlockState(pos, withWaterloggedState(world, pos, state.with(HALF, DoubleBlockHalf.LOWER)), flags);
		world.setBlockState(blockPos, withWaterloggedState(world, blockPos, state.with(HALF, DoubleBlockHalf.UPPER)), flags);
	}

	public static BlockState withWaterloggedState(WorldView world, BlockPos pos, BlockState state) {
		return state.contains(Properties.WATERLOGGED) ? state.with(Properties.WATERLOGGED, world.isWater(pos)) : state;
	}

	@Override
	public BlockState onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		if (!world.isClient) {
			if (player.isCreative()) {
				onBreakInCreative(world, pos, state, player);
			} else {
				dropStacks(state, world, pos, null, player, player.getMainHandStack());
			}
		}

		return super.onBreak(world, pos, state, player);
	}

	@Override
	public void afterBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack tool) {
		super.afterBreak(world, player, pos, Blocks.AIR.getDefaultState(), blockEntity, tool);
	}

	protected static void onBreakInCreative(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		DoubleBlockHalf doubleBlockHalf = state.get(HALF);
		if (doubleBlockHalf == DoubleBlockHalf.LOWER) return;

		BlockPos belowPos = pos.down();
		BlockState belowState = world.getBlockState(belowPos);
		if (belowState.isOf(state.getBlock()) && belowState.get(HALF) == DoubleBlockHalf.LOWER) {
			BlockState defaultState = belowState.getFluidState().isOf(Fluids.WATER) ? Blocks.WATER.getDefaultState() : Blocks.AIR.getDefaultState();
			world.setBlockState(belowPos, defaultState);
//			world.syncWorldEvent(player, 2001, belowPos, Block.getRawIdFromState(belowState));
		}
	}

	@Override
	protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		VoxelShape result = MODEL;
		if (state.get(HALF) == DoubleBlockHalf.UPPER)
			result = result.offset(0, -1, 0);
		return result;
	}

}
