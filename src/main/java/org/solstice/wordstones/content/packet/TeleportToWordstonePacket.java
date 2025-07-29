package org.solstice.wordstones.content.packet;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.solstice.wordstones.Wordstones;
import org.solstice.wordstones.content.Word;
import org.solstice.wordstones.content.block.entity.WordstoneEntity;

public record TeleportToWordstonePacket(BlockPos pos, Word word) implements CustomPayload {

	public static final Id<TeleportToWordstonePacket> ID = new Id<>(Wordstones.of("update_wordstone"));

	public static final PacketCodec<RegistryByteBuf, TeleportToWordstonePacket> CODEC = PacketCodec.tuple(
		BlockPos.PACKET_CODEC,
		TeleportToWordstonePacket::pos,
		Word.PACKET_CODEC,
		TeleportToWordstonePacket::word,
		TeleportToWordstonePacket::new
	);

	@Override
	public Id<? extends CustomPayload> getId() {
		return ID;
	}

	public static void handle(TeleportToWordstonePacket packet, ServerPlayNetworking.Context context) {
//		if (context.player().getWorld() instanceof ServerWorld world && world.getBlockEntity(packet.pos) instanceof WordstoneEntity wordstone) {
//			context.player().teleport(
//				world
//			);
//		}
	}

}
