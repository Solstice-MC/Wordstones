package org.solstice.wordstones.client.content.screen;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import org.solstice.wordstones.content.Word;
import org.solstice.wordstones.content.block.entity.WordstoneEntity;
import org.solstice.wordstones.content.packet.UpdateWordstonePacket;

public class WordstoneEditScreen extends AbstractWordstoneScreen {

	public WordstoneEditScreen(PlayerEntity player, WordstoneEntity wordstone) {
		super(Text.translatable("screen.wordstones.edit_wordstone"), player, wordstone);
	}

	@Override
	public void onDone() {
		if (this.word.length() != 4) return;

		ClientPlayNetworking.send(new UpdateWordstonePacket(this.wordstone.getPos(), new Word(this.word)));
		this.close();
	}

}
