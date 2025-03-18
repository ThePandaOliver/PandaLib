package me.pandamods.pandalib.core.client.event;

import me.pandamods.pandalib.core.network.ConfigNetworking;
import net.minecraft.client.player.LocalPlayer;

public class EventHandlerClient {
	public static void init() {
		// TODO: Implement player join event
//		ClientPlayerEvent.CLIENT_PLAYER_JOIN.register(EventHandlerClient::onClientPlayerJoin);
	}

	private static void onClientPlayerJoin(LocalPlayer localPlayer) {
		ConfigNetworking.SyncClientConfigs();
	}
}
