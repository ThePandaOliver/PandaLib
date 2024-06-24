/*
 * Copyright (C) 2024 Oliver Froberg (The Panda Oliver)
 *
 * This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 * You should have received a copy of the GNU General Public License
 *  along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package me.pandamods.pandalib.core.event;

import dev.architectury.event.events.client.ClientPlayerEvent;
import dev.architectury.event.events.common.PlayerEvent;
import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import me.pandamods.pandalib.core.network.ConfigNetworking;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.server.level.ServerPlayer;

public class EventHandler {
	public static void Register() {
		PlayerEvent.PLAYER_JOIN.register(EventHandler::onServerPlayerJoin);

		if (Platform.getEnvironment().equals(Env.CLIENT)) {
			ClientPlayerEvent.CLIENT_PLAYER_JOIN.register(EventHandler::onClientPlayerJoin);
		}
	}

	private static void onServerPlayerJoin(ServerPlayer serverPlayer) {
		ConfigNetworking.SyncCommonConfigs(serverPlayer);
	}

	private static void onClientPlayerJoin(LocalPlayer localPlayer) {
		ConfigNetworking.SyncClientConfigs();
	}
}
