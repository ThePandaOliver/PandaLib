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

package me.pandamods.pandalib.forge.platform;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import net.minecraftforge.eventbus.api.IEventBus;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * Borrowed from Architectury API
 */
public class EventBuses {
	private static final Map<String, IEventBus> EVENT_BUS_MAP = Collections.synchronizedMap(new HashMap<>());
	private static final Multimap<String, Consumer<IEventBus>> ON_REGISTERED = Multimaps.synchronizedMultimap(LinkedListMultimap.create());

	public static void registerModEventBus(String modId, IEventBus bus) {
		if (EVENT_BUS_MAP.putIfAbsent(modId, bus) != null) {
			throw new IllegalStateException("Can't register event bus for mod '" + modId + "' because it was previously registered!");
		}

		for (Consumer<IEventBus> consumer : ON_REGISTERED.get(modId)) {
			consumer.accept(bus);
		}
	}

	public static void onRegistered(String modId, Consumer<IEventBus> busConsumer) {
		if (EVENT_BUS_MAP.containsKey(modId)) {
			busConsumer.accept(EVENT_BUS_MAP.get(modId));
		} else {
			ON_REGISTERED.put(modId, busConsumer);
		}
	}

	public static Optional<IEventBus> getModEventBus(String modId) {
		return Optional.ofNullable(EVENT_BUS_MAP.get(modId));
	}
}
