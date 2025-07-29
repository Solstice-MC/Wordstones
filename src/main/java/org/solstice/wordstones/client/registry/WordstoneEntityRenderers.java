package org.solstice.wordstones.client.registry;

import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import org.solstice.wordstones.client.content.renderer.block.WordstoneRenderer;
import org.solstice.wordstones.registry.WordstoneBlockEntityTypes;

public class WordstoneEntityRenderers {

	public static void init() {
		BlockEntityRendererFactories.register(WordstoneBlockEntityTypes.WORDSTONE, WordstoneRenderer::new);
	}

}
