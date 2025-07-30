package org.solstice.wordstones.datagen;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.block.Block;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.data.client.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import org.solstice.euclidsElements.EuclidsElements;
import org.solstice.euclidsElements.autoDatagen.api.generator.AutoLanguageGenerator;
import org.solstice.euclidsElements.autoDatagen.api.generator.AutoLootTableGenerator;
import org.solstice.euclidsElements.autoDatagen.api.generator.AutoModelGenerator;
import org.solstice.euclidsElements.autoDatagen.api.supplier.BlockModelSupplier;
import org.solstice.wordstones.Wordstones;
import org.solstice.wordstones.content.block.DropBoxBlock;
import org.solstice.wordstones.content.block.WordstoneBlock;

public class WordstonesDataGenerator implements DataGeneratorEntrypoint {

	@Override
	public void onInitializeDataGenerator(FabricDataGenerator generator) {
		FabricDataGenerator.Pack pack = generator.createPack();
		pack.addProvider(WordstoneRecipeGenerator::new);
		pack.addProvider(AutoLanguageGenerator::new);
		pack.addProvider(AutoModelGenerator::new);
		pack.addProvider(AutoLootTableGenerator::new);

		BlockModelSupplier.register(DropBoxBlock.class, WordstonesDataGenerator::registerHorizontalFacing);
		BlockModelSupplier.register(WordstoneBlock.class, WordstonesDataGenerator::registerHorizontalFacingHalved);
	}

	public static void registerHorizontalFacing(BlockStateModelGenerator generator, Block block, Identifier id) {
		generator.registerNorthDefaultHorizontalRotation(block);
		generator.registerParentedItemModel(block, id.withPrefixedPath("block/"));
	}

	public static final Identifier EMPTY = Wordstones.of("block/empty");

	public static void registerHorizontalFacingHalved(BlockStateModelGenerator generator, Block block, Identifier id) {
		BlockStateVariantMap map = BlockStateVariantMap.create(WordstoneBlock.FACING, WordstoneBlock.HALF).register((direction, half) -> {
			VariantSettings.Rotation rotation = switch (direction) {
				case EAST -> VariantSettings.Rotation.R90;
				case SOUTH -> VariantSettings.Rotation.R180;
				case WEST -> VariantSettings.Rotation.R270;
				default -> VariantSettings.Rotation.R0;
			};
			if (half == DoubleBlockHalf.UPPER) return BlockStateVariant.create().put(VariantSettings.MODEL, EMPTY);

			BlockStateVariant variant = BlockStateVariant.create();
			variant.put(VariantSettings.MODEL, id.withPrefixedPath("block/"));
			if (rotation != VariantSettings.Rotation.R0) variant.put(VariantSettings.Y, rotation);
			return variant;
		});

		VariantsBlockStateSupplier supplier = VariantsBlockStateSupplier.create(block).coordinate(map);

		generator.blockStateCollector.accept(supplier);
		generator.registerParentedItemModel(block, id.withPrefixedPath("block/"));
	}

}
