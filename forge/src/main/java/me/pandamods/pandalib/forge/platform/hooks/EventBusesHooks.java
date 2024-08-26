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

package me.pandamods.pandalib.forge.platform.hooks;

import me.pandamods.pandalib.forge.platform.EventBuses;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * Borrowed from Architectury API
 */
public class EventBusesHooks {
	public static void whenAvailable(String modId, Consumer<IEventBus> busConsumer) {
		EventBuses.onRegistered(modId, busConsumer);
	}

	public static Optional<IEventBus> getModEventBus(String modId) {
		return EventBuses.getModEventBus(modId);
	}
}
