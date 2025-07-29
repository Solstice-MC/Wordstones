package org.solstice.wordstones.client.content.screen;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.BookModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;
import org.solstice.wordstones.content.block.entity.WordstoneEntity;

public abstract class AbstractWordstoneScreen extends Screen {

	private static final Identifier BOOK_TEXTURE = Identifier.ofVanilla("textures/entity/enchanting_table_book.png");
	@Nullable private BookModel bookModel;

	protected final PlayerEntity player;
	protected final WordstoneEntity wordstone;
	protected String word;

	protected int cursorPos = 0;
	protected boolean cursorVisible = true;
	protected int tickCount = 0;

	protected ButtonWidget doneButton;

	public AbstractWordstoneScreen(Text title, PlayerEntity player, WordstoneEntity wordstone) {
		super(title);
		this.player = player;
		this.wordstone = wordstone;
		this.word = wordstone.getWord() != null ? wordstone.getWord().value() : "";
	}

	@Override
	protected void init() {
		super.init();

		this.doneButton = ButtonWidget.builder(ScreenTexts.DONE, this::onDone)
			.dimensions(this.width / 2 - 100, this.height / 4 + 144, 200, 20)
			.build();

		this.addDrawableChild(this.doneButton);
		this.updateButtons();

		MinecraftClient client = this.client;
		if (client != null) this.bookModel = new BookModel(client.getEntityModelLoader().getModelPart(EntityModelLayers.BOOK));
		else this.bookModel = null;
	}

	public boolean isPlayerTooFar() {
		return !this.player.canInteractWithBlockAt(this.wordstone.getPos(), 4);
	}

	protected boolean canUse() {
		return this.client != null && this.client.player != null && !this.wordstone.isRemoved() && !this.isPlayerTooFar();
	}

	@Override
	public void tick() {
		super.tick();
		this.tickCount++;

		if (this.tickCount % 10 == 0) this.cursorVisible = !this.cursorVisible;

		if (!this.canUse()) this.close();
	}

	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		super.render(context, mouseX, mouseY, delta);

		int centerX = this.width / 2;
		int centerY = this.height / 2;

		DiffuseLighting.disableGuiDepthLighting();
		context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 40, 0xFFFFFF);

		MatrixStack matrices = context.getMatrices();
		VertexConsumerProvider vertexConsumers = context.getVertexConsumers();

		this.drawBook(matrices, vertexConsumers, context, centerX, centerY);
		this.drawText(matrices, context, centerX, centerY);

		DiffuseLighting.enableGuiDepthLighting();
	}

	private void drawBook(MatrixStack matrices, VertexConsumerProvider vertexConsumers, DrawContext context, int x, int y) {
		if (this.bookModel == null) return;

		matrices.push();
		matrices.translate((float)x, (float)y, -1024);

		float scale = 128F;
		matrices.scale(-scale, scale, scale);

		matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(45));
		matrices.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees(90));
		float leftFlipAmount = MathHelper.clamp(MathHelper.fractionalPart(1 + 0.25F) * 1.6F - 0.3F, 0.0F, 1.0F);
		float rightFlipAmount = MathHelper.clamp(MathHelper.fractionalPart(1 + 0.75F) * 1.6F - 0.3F, 0.0F, 1.0F);
		this.bookModel.setPageAngles(0, leftFlipAmount, rightFlipAmount, 1);
		VertexConsumer vertexConsumer = vertexConsumers.getBuffer(this.bookModel.getLayer(BOOK_TEXTURE));
		this.bookModel.render(matrices, vertexConsumer, 0xF000F0, OverlayTexture.DEFAULT_UV);
		context.draw();
		matrices.pop();
	}

	protected void drawText(MatrixStack matrices, DrawContext context, int centerX, int centerY) {
		matrices.push();
		matrices.translate(-centerX, -centerY, 0);
		matrices.scale(2, 2, 1);

		String word = this.word;
		int missing = Math.max(0, 4 - word.length());
		word += "_".repeat(missing);

		int textX = centerX - 10;
		int textY = centerY - 10;

		for (int i = 0; i < 4; i++) {
			String letter = String.valueOf(i < word.length() ? word.charAt(i) : '_');

			int charX = textX + i * 7 - (this.textRenderer.getWidth(letter) / 2);
			context.drawText(this.textRenderer, letter, charX, textY, 0x000000, false);

			if (i == this.cursorPos && this.cursorVisible && this.isFocused())
				context.drawTextWithShadow(this.textRenderer, "_", charX, textY, 0x000000);
		}

		matrices.pop();
	}

	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		return switch (keyCode) {
			case GLFW.GLFW_KEY_ESCAPE -> {
				this.close();
				yield true;
			}
			case GLFW.GLFW_KEY_ENTER, GLFW.GLFW_KEY_KP_ENTER -> {
				this.onDone();
				yield true;
			}
			case GLFW.GLFW_KEY_BACKSPACE -> {
				if (this.cursorPos > 0) {
					this.word = this.word.substring(0, this.cursorPos - 1) +
						this.word.substring(this.cursorPos);
					this.cursorPos--;
					this.updateButtons();
				}
				yield true;
			}
			case GLFW.GLFW_KEY_DELETE -> {
				if (this.cursorPos < this.word.length()) {
					this.word = this.word.substring(0, this.cursorPos) +
						this.word.substring(this.cursorPos + 1);
					this.updateButtons();
				}
				yield true;
			}
			case GLFW.GLFW_KEY_LEFT -> {
				if (this.cursorPos > 0) {
					this.cursorPos--;
				}
				yield true;
			}
			case GLFW.GLFW_KEY_RIGHT -> {
				if (this.cursorPos < Math.min(this.word.length(), 3)) {
					this.cursorPos++;
				}
				yield true;
			}
			case GLFW.GLFW_KEY_HOME -> {
				this.cursorPos = 0;
				yield true;
			}
			case GLFW.GLFW_KEY_END -> {
				this.cursorPos = this.word.length();
				yield true;
			}
			default -> super.keyPressed(keyCode, scanCode, modifiers);
		};

	}

	@Override
	public boolean charTyped(char chr, int modifiers) {
		// Check if we've reached the maximum length
		if (this.word.length() >= 4) {
			return false;
		}

		// Only allow letters (both upper and lower case)
		if (Character.isLetter(chr)) {
			// Convert to uppercase for consistency
			chr = Character.toUpperCase(chr);

			// Insert character at cursor position
			this.word = this.word.substring(0, this.cursorPos) +
				chr +
				this.word.substring(this.cursorPos);
			this.cursorPos++;
			this.updateButtons();
			return true;
		}

		return super.charTyped(chr, modifiers);
	}

	protected void updateButtons() {
		if (this.doneButton != null) {
			this.doneButton.active = this.word.length() == 4;
		}
	}


	protected void onDone(ButtonWidget button) {
		this.onDone();
	}

	abstract public void onDone();

	@Override
	public void close() {
		if (this.client != null) this.client.setScreen(null);
	}

	@Override
	public boolean shouldPause() {
		return false;
	}

	@Override
	public boolean shouldCloseOnEsc() {
		return true;
	}

}
