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

package me.pandamods.pandalib.neoforge.event.events.common;

import me.pandamods.pandalib.event.events.client.PlayerEvents;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

public class PlayerEventsImpl {
	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
		PlayerEvents.PLAYER_JOIN.invoker().join((ServerPlayer) event.getEntity());
	}

	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void onPlayerQuit(PlayerEvent.PlayerLoggedOutEvent event) {
		PlayerEvents.PLAYER_QUIT.invoker().quit((ServerPlayer) event.getEntity());
	}
}
