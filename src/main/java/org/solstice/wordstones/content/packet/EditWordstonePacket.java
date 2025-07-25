package org.solstice.wordstones.content.packet;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.math.BlockPos;
import org.solstice.wordstones.Wordstones;

public record EditWordstonePacket(BlockPos pos) implements CustomPayload {

	public static final CustomPayload.Id<EditWordstonePacket> ID = new CustomPayload.Id<>(Wordstones.of("edit_wordstone"));

	public static final PacketCodec<RegistryByteBuf, EditWordstonePacket> CODEC = PacketCodec.tuple(
		BlockPos.PACKET_CODEC,
		EditWordstonePacket::pos,
		EditWordstonePacket::new
	);

	@Override
	public Id<? extends CustomPayload> getId() {
		return ID;
	}

}
