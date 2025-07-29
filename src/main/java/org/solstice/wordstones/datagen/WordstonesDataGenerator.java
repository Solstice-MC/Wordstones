package org.solstice.wordstones.datagen;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import org.solstice.euclidsElements.autoDatagen.api.generator.AutoLanguageGenerator;
import org.solstice.euclidsElements.autoDatagen.api.generator.AutoLootTableGenerator;
import org.solstice.euclidsElements.autoDatagen.api.generator.AutoModelGenerator;

public class WordstonesDataGenerator implements DataGeneratorEntrypoint {

	@Override
	public void onInitializeDataGenerator(FabricDataGenerator generator) {
		FabricDataGenerator.Pack pack = generator.createPack();
		pack.addProvider(AutoLanguageGenerator::new);
		pack.addProvider(AutoModelGenerator::new);
		pack.addProvider(AutoLootTableGenerator::new);
	}

}
