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

package me.pandamods.pandalib.neoforge.platform.hooks;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * Borrowed from Architectury API
 */
public class EventBusesHooks {
	public static void whenAvailable(String modId, Consumer<IEventBus> busConsumer) {
		IEventBus bus = getModEventBus(modId).orElseThrow(() -> new IllegalStateException("Mod '" + modId + "' is not available!"));
		busConsumer.accept(bus);
	}

	public static Optional<IEventBus> getModEventBus(String modId) {
		return ModList.get().getModContainerById(modId)
				.map(ModContainer::getEventBus);
	}
}
