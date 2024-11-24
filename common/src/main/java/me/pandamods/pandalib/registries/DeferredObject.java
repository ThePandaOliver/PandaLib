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

package me.pandamods.pandalib.registries;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

public class DeferredObject<T, V extends T> implements Supplier<V> {
	private final V object;
	private final ResourceKey<T> resourceKey;

	public DeferredObject(V object, ResourceKey<T> resourceKey) {
		this.object = object;
		this.resourceKey = resourceKey;
	}

	@Override
	public V get() {
		return object;
	}

	public ResourceLocation getLocation() {
		return resourceKey.location();
	}

	public ResourceKey<T> getKey() {
		return resourceKey;
	}

	public ResourceKey<? extends Registry<T>> getRegistryKey() {
		return resourceKey.registryKey();
	}
}
