package org.solstice.wordstones.client.renderer.block;

import net.minecraft.block.BlockState;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.solstice.wordstones.content.block.entity.WordstoneEntity;

public class WordstoneRenderer implements BlockEntityRenderer<WordstoneEntity> {

	protected final BlockRenderManager manager;

	public WordstoneRenderer(BlockEntityRendererFactory.Context context) {
		this.manager = context.getRenderManager();
	}

	@Override
	public void render(WordstoneEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		this.renderModel(entity.getPos(), entity.getCachedState(), matrices, vertexConsumers, entity.getWorld(), overlay);
	}

	private void renderModel(BlockPos pos, BlockState state, MatrixStack matrices, VertexConsumerProvider vertexConsumers, World world, int overlay) {
		RenderLayer renderLayer = RenderLayers.getMovingBlockLayer(state);
		VertexConsumer vertexConsumer = vertexConsumers.getBuffer(renderLayer);
		BakedModel model = this.manager.getModel(state);
		this.manager.getModelRenderer().render(
			world,
			model,
			state,
			pos,
			matrices,
			vertexConsumer,
			false,
			world.getRandom(),
			state.getRenderingSeed(pos),
			overlay
		);
	}

}
