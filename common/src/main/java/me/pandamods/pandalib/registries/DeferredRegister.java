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

import me.pandamods.pandalib.platform.Services;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class DeferredRegister<T> {
	private final String namespace;
	private final ResourceKey<? extends Registry<T>> registryKey;

	private final List<DeferredObject<T, ? extends T>> entries = new ArrayList<>();

	public static <T> DeferredRegister<T> create(String namespace, ResourceKey<? extends Registry<T>> registryKey) {
		return new DeferredRegister<>(namespace, registryKey);
	}

	public static <T> DeferredRegister<T> create(String namespace, Registry<T> registry) {
		return new DeferredRegister<>(namespace, registry.key());
	}

	public static <T> DeferredRegister<T> create(String namespace, ResourceLocation registryName) {
		return new DeferredRegister<>(namespace, ResourceKey.createRegistryKey(registryName));
	}

	private DeferredRegister(String namespace, ResourceKey<? extends Registry<T>> registryKey) {
		this.namespace = namespace;
		this.registryKey = registryKey;
	}

	public <V extends T> DeferredObject<T, V> register(String name, V object) {
		return register(ResourceLocation.fromNamespaceAndPath(namespace, name), object);
	}

	public <V extends T> DeferredObject<T, V> register(ResourceLocation name, V object) {
		DeferredObject<T, V> entry = new DeferredObject<>(object, ResourceKey.create(registryKey, name));
		entries.add(entry);
		return entry;
	}

	public void register() {
		entries.forEach(Services.REGISTRATION_HELPER::register);
	}
}
