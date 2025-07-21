package org.solstice.wordstones.content.block;

import com.mojang.serialization.Codec;
import org.apache.commons.lang3.StringUtils;

public record Word(String value) {

	public static final Codec<Word> CODEC = Codec.STRING.xmap(Word::new, Word::value);

	public Word(String value) {
		this.value = StringUtils.substring(value, 0, 4);
	}

}
