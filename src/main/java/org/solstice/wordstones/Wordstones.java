package org.solstice.wordstones;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.solstice.wordstones.content.item.LastWillItem;
import org.solstice.wordstones.registry.*;

public class Wordstones implements ModInitializer {

	public static final String MOD_ID = "wordstones";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static Identifier of(String path) {
		return Identifier.of(MOD_ID, path);
	}

	@Override
	public void onInitialize() {
		WordstoneComponentTypes.init();
		WordstoneAttachmentTypes.init();

		WordstoneBlocks.init();
		WordstoneItems.init();
		WordstoneBlockEntityTypes.init();

		WordstoneItemGroups.init();
		WordstonesSoundEvents.init();
	}

}
