package me.pandamods.pandalib.core.network;

import dev.architectury.networking.NetworkManager;
import me.pandamods.pandalib.PandaLib;
import net.minecraft.resources.ResourceLocation;

public class PacketHandler {
	public static final ResourceLocation CONFIG_PACKET = new ResourceLocation(PandaLib.MOD_ID, "config");

	public static void init() {
		NetworkManager.registerReceiver(NetworkManager.clientToServer(), CONFIG_PACKET, ConfigPacket::configReceiver);
	}
}
