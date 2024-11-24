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

public class DeferredRegistry<T> {
	private final String namespace;
	private final ResourceKey<? extends Registry<T>> registryKey;

	public static <T> DeferredRegistry<T> create(String namespace, ResourceLocation registryLocation) {
		return create(namespace, ResourceKey.createRegistryKey(registryLocation))
	}

	public static <T> DeferredRegistry<T> create(String namespace, Registry<T> registry) {
		return create(namespace, registry.key());
	}

	public static <T> DeferredRegistry<T> create(String namespace, ResourceKey<? extends Registry<T>> registryKey) {
		return new DeferredRegistry<>(namespace, registryKey);
	}

	private DeferredRegistry(String namespace, ResourceKey<? extends Registry<T>> registryKey) {
		this.namespace = namespace;
		this.registryKey = registryKey;
	}
}
