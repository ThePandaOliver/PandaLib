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

package me.pandamods.pandalib.neoforge.event.events.client;

import me.pandamods.pandalib.event.events.common.ClientPlayerEvents;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.common.NeoForge;

@OnlyIn(Dist.CLIENT)
public class ClientPlayerEventsImpl {
	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void onPlayerJoin(ClientPlayerNetworkEvent.LoggingIn event) {
		ClientPlayerEvents.PLAYER_JOIN.invoker().join(event.getPlayer());
	}

	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void onPlayerQuit(ClientPlayerNetworkEvent.LoggingOut event) {
		ClientPlayerEvents.PLAYER_QUIT.invoker().quit(event.getPlayer());
	}
}
