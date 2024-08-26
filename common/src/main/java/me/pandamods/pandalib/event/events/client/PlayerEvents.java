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

package me.pandamods.pandalib.event.events.client;

import me.pandamods.pandalib.event.Event;
import me.pandamods.pandalib.event.EventFactory;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.server.level.ServerPlayer;

@Environment(EnvType.CLIENT)
public interface PlayerEvents {
	Event<PlayerJoin> PLAYER_JOIN = EventFactory.createEvent();
	Event<PlayerQuit> PLAYER_QUIT = EventFactory.createEvent();

	interface PlayerJoin {
		void join(ServerPlayer player);
	}

	interface PlayerQuit {
		void quit(ServerPlayer player);
	}
}
