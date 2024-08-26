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

package me.pandamods.pandalib.forge.event;

import me.pandamods.pandalib.forge.event.events.common.BlockEventsImpl;
import me.pandamods.pandalib.forge.event.events.common.PlayerEventsImpl;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;

public class EventHandlerImpl {
	public static void register() {
		IEventBus bus = MinecraftForge.EVENT_BUS;

		bus.register(PlayerEventsImpl.class);
		bus.register(BlockEventsImpl.class);
	}
}
