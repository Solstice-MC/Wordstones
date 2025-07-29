package org.solstice.wordstones.content.packet;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.solstice.wordstones.Wordstones;
import org.solstice.wordstones.client.content.screen.WordstoneEditScreen;
import org.solstice.wordstones.content.block.entity.WordstoneEntity;

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

	public static void handle(EditWordstonePacket packet, ClientPlayNetworking.Context context) {
		World world = context.player().getWorld();
		if (world.getBlockEntity(packet.pos) instanceof WordstoneEntity wordstone)
			context.client().setScreen(new WordstoneEditScreen(context.player(), wordstone));
	}

}
