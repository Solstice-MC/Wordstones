package org.solstice.wordstones.registry;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.solstice.wordstones.Wordstones;
import org.solstice.wordstones.content.block.entity.WordstoneEntity;

public class WordstoneBlockEntityTypes {

	public static void init() {}

	public static final BlockEntityType<WordstoneEntity> WORDSTONE = register("wordstone",
		BlockEntityType.Builder.create(WordstoneEntity::new, WordstoneBlocks.WORDSTONE)
	);

	public static <T extends BlockEntity> BlockEntityType<T> register(String name, BlockEntityType.Builder<T> builder) {
		Identifier id = Wordstones.of(name);
		return Registry.register(Registries.BLOCK_ENTITY_TYPE, id, builder.build());
	}

}
