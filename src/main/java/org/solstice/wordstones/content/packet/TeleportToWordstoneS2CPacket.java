package org.solstice.wordstones.content.packet;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.solstice.wordstones.Wordstones;
import org.solstice.wordstones.client.content.screen.WordstoneEditScreen;
import org.solstice.wordstones.client.content.screen.WordstoneTeleportScreen;
import org.solstice.wordstones.content.Word;
import org.solstice.wordstones.content.block.entity.WordstoneEntity;

public record TeleportToWordstoneS2CPacket(BlockPos pos, Word word) implements CustomPayload {

	public static final Id<TeleportToWordstoneS2CPacket> ID = new Id<>(Wordstones.of("teleport_to_wordstone_s2c"));

	public static final PacketCodec<RegistryByteBuf, TeleportToWordstoneS2CPacket> CODEC = PacketCodec.tuple(
		BlockPos.PACKET_CODEC,
		TeleportToWordstoneS2CPacket::pos,
		Word.PACKET_CODEC,
		TeleportToWordstoneS2CPacket::word,
		TeleportToWordstoneS2CPacket::new
	);

	@Override
	public Id<? extends CustomPayload> getId() {
		return ID;
	}

	@Environment(EnvType.CLIENT)
	public static void handle(TeleportToWordstoneS2CPacket packet, ClientPlayNetworking.Context context) {
		World world = context.player().getWorld();
		if (world.getBlockEntity(packet.pos) instanceof WordstoneEntity wordstone)
			context.client().setScreen(new WordstoneTeleportScreen(wordstone));
	}

}
