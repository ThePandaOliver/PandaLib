package me.pandamods.pandalib.event;

import dev.architectury.event.EventResult;
import dev.architectury.event.events.client.ClientPlayerEvent;
import dev.architectury.event.events.common.PlayerEvent;
import me.pandamods.pandalib.PandaLib;
import me.pandamods.pandalib.network.ConfigPacket;
import me.pandamods.pandalib.network.ConfigPacketClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.player.LocalPlayer;

@Environment(EnvType.CLIENT)
public class EventHandlerClient {
	public static void init() {
		ClientPlayerEvent.CLIENT_PLAYER_JOIN.register(EventHandlerClient::onPlayerJoin);
	}

	private static void onPlayerJoin(LocalPlayer localPlayer) {
		ConfigPacketClient.sendToServer();
	}
}
