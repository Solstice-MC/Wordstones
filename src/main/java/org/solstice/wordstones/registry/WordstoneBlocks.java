package org.solstice.wordstones.registry;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import org.solstice.wordstones.Wordstones;
import org.solstice.wordstones.content.block.DropBoxBlock;
import org.solstice.wordstones.content.block.WordstoneBlock;

import java.util.function.Function;

public class WordstoneBlocks {

	public static void init() {}

	public static final Block WORDSTONE = register("wordstone",
		WordstoneBlock::new,
		AbstractBlock.Settings.create()
			.requiresTool()
			.strength(2.0F, 6.0F)
			.mapColor(MapColor.IRON_GRAY)
			.sounds(BlockSoundGroup.STONE)
			.pistonBehavior(PistonBehavior.BLOCK)
	);
	public static final Block DROP_BOX = register("drop_box",
		DropBoxBlock::new,
		AbstractBlock.Settings.create()
			.requiresTool()
			.strength(2.0F, 6.0F)
			.mapColor(MapColor.IRON_GRAY)
			.sounds(BlockSoundGroup.STONE)
			.pistonBehavior(PistonBehavior.BLOCK)
	);

	public static Block register(String name) {
		return register(name, Block::new, AbstractBlock.Settings.create());
	}

	public static Block register(String name, AbstractBlock.Settings settings) {
		return register(name, Block::new, settings);
	}

	public static Block register(String name, Function<AbstractBlock.Settings, Block> function, AbstractBlock.Settings settings) {
		return register(name, function, settings, new Item.Settings());
	}

	public static Block register(String name, Function<AbstractBlock.Settings, Block> function, AbstractBlock.Settings blockSettings, Item.Settings itemSettings) {
		Identifier id = Wordstones.of(name);
		RegistryKey<Block> blockKey = RegistryKey.of(RegistryKeys.BLOCK, id);
		Block block = function.apply(blockSettings);
		Registry.register(Registries.BLOCK, blockKey, block);
		RegistryKey<Item> itemKey = RegistryKey.of(RegistryKeys.ITEM, id);
		Item item = new BlockItem(block, itemSettings);
		Registry.register(Registries.ITEM, itemKey, item);
		return block;
	}

}
