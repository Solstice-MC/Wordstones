package org.solstice.wordstones.content.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.solstice.wordstones.content.Word;
import org.solstice.wordstones.content.block.entity.WordstoneEntity;

public class WordstoneBlock extends AbstractWordstoneBlock {

	@Override
	protected MapCodec<? extends BlockWithEntity> getCodec() {
		return createCodec(WordstoneBlock::new);
	}

	@Override
	@Nullable
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		if (state.get(HALF) == DoubleBlockHalf.UPPER) return null;
		return new WordstoneEntity(pos, state);
	}

	public WordstoneBlock(Settings settings) {
		super(settings);
	}

	@Override
	public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
		BlockPos abovePos = pos.up();
		world.setBlockState(abovePos, withWaterloggedState(world, abovePos, this.getDefaultState().with(HALF, DoubleBlockHalf.UPPER)));

		if (!world.isClient && placer instanceof ServerPlayerEntity player) {
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if (blockEntity instanceof WordstoneEntity wordstoneEntity && !wordstoneEntity.hasWord()) {
//				player.openEditSignScreen();
				player.sendMessage(Text.literal("Enter a unique 4-letter combo for this wordstone:"), false);
			}
		}
	}

	@Override
	protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
		if (world.isClient) return ActionResult.PASS	;

		BlockPos entityPos = state.get(HALF) == DoubleBlockHalf.UPPER ? pos.down() : pos;

		if (!(world.getBlockEntity(entityPos) instanceof WordstoneEntity wordstone)) return ActionResult.PASS;
		if (!wordstone.hasWord()) return ActionResult.PASS;

		if (player.isSneaking()) {
			Word word = wordstone.getWord();
			if (WordstoneEntity.wordExists(world, word)) {
				player.sendMessage(Text.literal("Teleporting to wordstone: " + word), false);
				WordstoneEntity.teleportPlayerToWord(player, word);
			} else {
				player.sendMessage(Text.literal("No wordstone with that combo exists."), false);
			}
		}

		return ActionResult.SUCCESS;
	}

}
