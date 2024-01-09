package me.pandamods.pandalib.event;

import dev.architectury.event.events.common.PlayerEvent;
import me.pandamods.pandalib.network.ConfigPacket;
import net.minecraft.server.level.ServerPlayer;

public class EventHandler {
	public static void init() {
		PlayerEvent.PLAYER_JOIN.register(EventHandler::onPlayerJoin);
	}

	private static void onPlayerJoin(ServerPlayer serverPlayer) {
		ConfigPacket.requestConfigsFromPlayer(serverPlayer);
		ConfigPacket.sendToPlayer(serverPlayer);
	}
}
