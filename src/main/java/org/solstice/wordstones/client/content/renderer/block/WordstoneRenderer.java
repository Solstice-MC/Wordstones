package org.solstice.wordstones.client.content.renderer.block;

import net.minecraft.client.render.*;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.block.entity.EnchantingTableBlockEntityRenderer;
import net.minecraft.client.render.entity.model.BookModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.util.math.MatrixStack;
import org.solstice.wordstones.content.block.entity.WordstoneEntity;

public class WordstoneRenderer implements BlockEntityRenderer<WordstoneEntity> {

	protected final BlockRenderManager manager;
	protected final BookModel book;

	public WordstoneRenderer(BlockEntityRendererFactory.Context context) {
		this.manager = context.getRenderManager();
		this.book = new BookModel(context.getLayerModelPart(EntityModelLayers.BOOK));
	}

	@Override
	public void render(WordstoneEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		matrices.push();
		VertexConsumer vertexConsumer = EnchantingTableBlockEntityRenderer.BOOK_TEXTURE.getVertexConsumer(vertexConsumers, RenderLayer::getEntitySolid);
		this.book.renderBook(matrices, vertexConsumer, light, overlay, -1);
		matrices.pop();
	}

//	private void renderModel(BlockPos pos, BlockState state, MatrixStack matrices, VertexConsumerProvider vertexConsumers, World world, int overlay) {
//		RenderLayer renderLayer = RenderLayers.getMovingBlockLayer(state);
//		VertexConsumer vertexConsumer = vertexConsumers.getBuffer(renderLayer);
//		BakedModel model = this.manager.getModel(state);
//		this.manager.getModelRenderer().render(
//			world,
//			model,
//			state,
//			pos,
//			matrices,
//			vertexConsumer,
//			false,
//			world.getRandom(),
//			state.getRenderingSeed(pos),
//			overlay
//		);
//	}

}
