package org.solstice.wordstones.registry;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import org.solstice.wordstones.content.packet.EditWordstonePacket;
import org.solstice.wordstones.content.packet.UpdateWordstonePacket;

public class WordstonePackets {

	public static void init() {
		PayloadTypeRegistry.playS2C().register(EditWordstonePacket.ID, EditWordstonePacket.CODEC);
		PayloadTypeRegistry.playC2S().register(UpdateWordstonePacket.ID, UpdateWordstonePacket.CODEC);
	}

	public static void clientInit() {
		ClientPlayNetworking.registerGlobalReceiver(EditWordstonePacket.ID, EditWordstonePacket::handle);
		ServerPlayNetworking.registerGlobalReceiver(UpdateWordstonePacket.ID, UpdateWordstonePacket::handle);
	}

}
