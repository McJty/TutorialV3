package com.example.tutorialv3.blocks;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;

public class PowergenBlock extends Block implements EntityBlock {

	public static final String MESSAGE_POWERGEN = "message.powergen";
	public static final String SCREEN_TUTORIAL_POWERGEN = "screen.tutorial.powergen";

	private static final VoxelShape RENDER_SHAPE = Shapes.box(0.1, 0.1, 0.1, 0.9, 0.9, 0.9);

	public PowergenBlock() {
		super(Properties.of(Material.METAL).sound(SoundType.METAL).strength(2.0f)
				.lightLevel(state -> state.getValue(BlockStateProperties.POWERED) ? 14 : 0)
				.requiresCorrectToolForDrops());
	}

	@Override
	public VoxelShape getOcclusionShape(BlockState state, BlockGetter reader, BlockPos pos) {
		return RENDER_SHAPE;
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable BlockGetter reader, List<Component> list,
			TooltipFlag flags) {
		list.add(Component.translatable(MESSAGE_POWERGEN, Integer.toString(PowergenConfig.POWERGEN_GENERATE.get()))
				.withStyle(ChatFormatting.BLUE));
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
		return new PowergenBE(blockPos, blockState);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		return level.isClientSide() ? null : (lvl, pos, blockState, t) -> ((PowergenBE)t).tickServer();
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(BlockStateProperties.POWERED);
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return super.getStateForPlacement(context).setValue(BlockStateProperties.POWERED, false);
	}

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand,
			BlockHitResult trace) {
		if (!level.isClientSide && level.getBlockEntity(pos) instanceof PowergenBE be) {
			NetworkHooks.openGui((ServerPlayer) player, be, be.getBlockPos());
		} else {
			throw new IllegalStateException("Our named container provider is missing!");
		}
		return InteractionResult.SUCCESS;
	}
}
