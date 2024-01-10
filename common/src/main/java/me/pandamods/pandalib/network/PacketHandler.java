package me.pandamods.pandalib.network;

import dev.architectury.networking.NetworkManager;
import me.pandamods.pandalib.PandaLib;
import net.minecraft.resources.ResourceLocation;

public class PacketHandler {
	public static final ResourceLocation CONFIG_REQUEST_PACKET = new ResourceLocation(PandaLib.MOD_ID, "config_request");
	public static final ResourceLocation CONFIG_PACKET = new ResourceLocation(PandaLib.MOD_ID, "config");

	public static void init() {
//		NetworkManager.registerReceiver(NetworkManager.clientToServer(), CONFIG_REQUEST_PACKET, ConfigPacket::requestReceiver);
		NetworkManager.registerReceiver(NetworkManager.clientToServer(), CONFIG_PACKET, ConfigPacket::configReceiver);
	}
}
