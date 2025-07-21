package org.solstice.wordstones.content.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import org.solstice.wordstones.registry.WordstoneBlockEntityTypes;

public class WordstoneEntity extends BlockEntity {

	public WordstoneEntity(BlockPos pos, BlockState state) {
		super(WordstoneBlockEntityTypes.WORDSTONE, pos, state);
	}

}
