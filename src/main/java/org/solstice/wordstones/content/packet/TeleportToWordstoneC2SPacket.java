package org.solstice.wordstones.content.packet;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.math.BlockPos;
import org.solstice.wordstones.Wordstones;
import org.solstice.wordstones.content.Word;

public record TeleportToWordstoneC2SPacket(BlockPos pos, Word word) implements CustomPayload {

	public static final Id<TeleportToWordstoneC2SPacket> ID = new Id<>(Wordstones.of("teleport_to_wordstone_c2s"));

	public static final PacketCodec<RegistryByteBuf, TeleportToWordstoneC2SPacket> CODEC = PacketCodec.tuple(
		BlockPos.PACKET_CODEC,
		TeleportToWordstoneC2SPacket::pos,
		Word.PACKET_CODEC,
		TeleportToWordstoneC2SPacket::word,
		TeleportToWordstoneC2SPacket::new
	);

	@Override
	public Id<? extends CustomPayload> getId() {
		return ID;
	}

	public static void handle(TeleportToWordstoneC2SPacket packet, ServerPlayNetworking.Context context) {
//		if (context.player().getWorld() instanceof ServerWorld world && world.getBlockEntity(packet.pos) instanceof WordstoneEntity wordstone) {
//			context.player().teleport(
//				world
//			);
//		}
	}

}
