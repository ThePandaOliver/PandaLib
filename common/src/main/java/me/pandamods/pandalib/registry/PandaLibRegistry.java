/*
 * Copyright (C) 2024 Oliver Froberg (The Panda Oliver)
 *
 * This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 * You should have received a copy of the GNU Lesser General Public License
 *  along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package me.pandamods.pandalib.registry;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class PandaLibRegistry<T> {
	private final Map<ResourceLocation, T> entries = new HashMap<>();

	private final Registry<T> registry;
	private final String modId;

	public PandaLibRegistry(Registry<T> registry, String modId) {
		this.registry = registry;
		this.modId = modId;
	}

	public T register(String name, RegistryHandler<T> handler) {
		return register(ResourceLocation.fromNamespaceAndPath(modId, name), handler);
	}

	public T register(ResourceLocation resourceLocation, RegistryHandler<T> handler) {
		ResourceKey<T> key = ResourceKey.create(registry.key(), resourceLocation);
		T value = handler.register(key);
		entries.put(resourceLocation, value);
		return value;
	}

	public void register() {
		entries.forEach((resourceLocation, t) -> Registry.register(registry, resourceLocation, t));
	}
}
