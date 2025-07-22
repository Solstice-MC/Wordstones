package org.solstice.wordstones.client.screen;

import net.minecraft.block.BlockState;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.client.util.SelectionManager;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.solstice.wordstones.content.block.Word;
import org.solstice.wordstones.content.block.entity.WordstoneEntity;

public class WordEditScreen extends Screen {

	protected final WordstoneEntity entity;
	protected final Word word;
	private int ticksSinceOpened;

	@Nullable
	private SelectionManager selectionManager;

	public WordEditScreen(WordstoneEntity entity) {
		this(entity, Text.translatable("screen.wordstones.word_edit"));
	}

	public WordEditScreen(WordstoneEntity entity, Text title) {
		super(title);
		this.entity = entity;
		this.word = entity.getWord();
	}

	protected void init() {
		this.addDrawableChild(
			ButtonWidget.builder(ScreenTexts.DONE, (button) -> this.client.setScreen(null))
				.dimensions(this.width / 2 - 100, this.height / 4 + 144, 200, 20)
				.build()
		);
		this.selectionManager = new SelectionManager(
			this.word::toString,
			this::setWord,
			SelectionManager.makeClipboardGetter(this.client),
			SelectionManager.makeClipboardSetter(this.client),
			string -> string.length() <= 4
		);
	}

	public void tick() {
		++this.ticksSinceOpened;
		if (!this.canEdit()) this.client.setScreen(null);
	}

	private boolean canEdit() {
		return this.client != null && this.client.player != null && !this.entity.isRemoved();
	}

	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		return this.selectionManager.handleSpecialKey(keyCode) ? true : super.keyPressed(keyCode, scanCode, modifiers);
	}

	public boolean charTyped(char chr, int modifiers) {
		this.selectionManager.insert(chr);
		return true;
	}

	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		super.render(context, mouseX, mouseY, delta);
		DiffuseLighting.disableGuiDepthLighting();
		context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 40, 16777215);

		context.getMatrices().push();
		this.renderSignText(context);
		context.getMatrices().pop();

		DiffuseLighting.enableGuiDepthLighting();
	}

	@Override
	public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
		this.renderInGameBackground(context);
	}

	@Override
	public void close() {
		this.client.setScreen(null);
	}

	public boolean shouldPause() {
		return false;
	}

	protected void translateForRender(DrawContext context, BlockState state) {
		context.getMatrices().translate((float)this.width / 2.0F, 90.0F, 50.0F);
	}


	private void renderSignText(DrawContext context) {
		context.getMatrices().translate(0.0F, 0.0F, 4.0F);
		context.getMatrices().scale(0.9765628F, 0.9765628F, 0.9765628F);
		int color = -988212;
		int start = this.selectionManager.getSelectionStart();
		int height = 20;

		String string = this.word.toString();
		if (this.textRenderer.isRightToLeft()) {
			string = this.textRenderer.mirror(string);
		}

		int width = -this.textRenderer.getWidth(string) / 2;
		context.drawText(this.textRenderer, string, width, height, color, false);

		if (this.ticksSinceOpened / 6 % 2 == 0) {
			int p = this.textRenderer.getWidth(string.substring(0, Math.max(Math.min(start, string.length()), 0)));
			int q = p - this.textRenderer.getWidth(string) / 2;
			if (start >= string.length()) {
				context.drawText(this.textRenderer, "_", q, height, color, false);
			}
		}
	}

	private void setWord(String message) {
		this.entity.setWord(new Word(message));
	}

}
