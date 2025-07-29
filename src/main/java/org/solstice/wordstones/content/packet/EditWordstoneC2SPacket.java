package org.solstice.wordstones.content.packet;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.solstice.wordstones.Wordstones;
import org.solstice.wordstones.content.Word;
import org.solstice.wordstones.content.block.entity.WordstoneEntity;

public record EditWordstoneC2SPacket(BlockPos pos, Word word) implements CustomPayload {

	public static final CustomPayload.Id<EditWordstoneC2SPacket> ID = new CustomPayload.Id<>(Wordstones.of("update_wordstone_c2s"));

	public static final PacketCodec<RegistryByteBuf, EditWordstoneC2SPacket> CODEC = PacketCodec.tuple(
		BlockPos.PACKET_CODEC,
		EditWordstoneC2SPacket::pos,
		Word.PACKET_CODEC,
		EditWordstoneC2SPacket::word,
		EditWordstoneC2SPacket::new
	);

	@Override
	public CustomPayload.Id<? extends CustomPayload> getId() {
		return ID;
	}


	public static void handle(EditWordstoneC2SPacket packet, ServerPlayNetworking.Context context) {
		World world = context.player().getWorld();
		if (world.getBlockEntity(packet.pos) instanceof WordstoneEntity wordstone)
			wordstone.setWord(packet.word);
	}

}
