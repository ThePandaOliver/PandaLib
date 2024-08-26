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

package me.pandamods.pandalib.neoforge.event;

import me.pandamods.pandalib.neoforge.event.events.common.BlockEventsImpl;
import me.pandamods.pandalib.neoforge.event.events.common.PlayerEventsImpl;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.NeoForge;

public class EventHandlerImpl {
	public static void register() {
		IEventBus bus = NeoForge.EVENT_BUS;

		bus.register(PlayerEventsImpl.class);
		bus.register(BlockEventsImpl.class);
	}
}
