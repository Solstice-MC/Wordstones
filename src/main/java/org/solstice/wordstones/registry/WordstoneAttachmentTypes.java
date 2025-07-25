package org.solstice.wordstones.registry;

import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import org.solstice.wordstones.Wordstones;
import org.solstice.wordstones.content.Word;
import org.solstice.euclidsElements.util.Location;

import java.util.Map;

public class WordstoneAttachmentTypes {

	public static void init() {}

    public static final Codec<Map<Word, Location>> GLOBAL_WORDSTONES_CODEC = Codec.unboundedMap(
		Word.CODEC,
        Location.CODEC
    );

	public static final AttachmentType<Map<Word, Location>> GLOBAL_WORDSTONES = AttachmentRegistry.create(Wordstones.of("global_wordstones"),
		builder -> builder
			.persistent(GLOBAL_WORDSTONES_CODEC)
			.initializer(Map::of)
	);

}
