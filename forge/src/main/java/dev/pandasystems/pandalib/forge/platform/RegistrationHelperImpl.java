/*
 * Copyright (C) 2025 Oliver Froberg (The Panda Oliver)
 *
 * This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 * You should have received a copy of the GNU Lesser General Public License
 *  along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package dev.pandasystems.pandalib.forge.platform;

import dev.pandasystems.pandalib.platform.services.RegistrationHelper;
import dev.pandasystems.pandalib.registry.DeferredObject;
import dev.pandasystems.pandalib.registry.IdentifiableResourceReloadListener;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.RegistrationInfo;
import net.minecraft.core.Registry;
import net.minecraft.core.WritableRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.packs.PackType;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.registries.RegisterEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class RegistrationHelperImpl implements RegistrationHelper {
	private final Map<ResourceKey<? extends Registry<?>>, PendingRegistries<?>> pendingRegistries = new HashMap<>();
	private final List<Registry<?>> pendingRegistryTypes = new ArrayList<>();
	private final List<IdentifiableResourceReloadListener> serverDataReloadListeners = new ArrayList<>();
	private final List<IdentifiableResourceReloadListener> clientDataReloadListeners = new ArrayList<>();

	@Override
	@SuppressWarnings("unchecked")
	public <T> void register(DeferredObject<? extends T> deferredObject, Supplier<? extends T> supplier) {
		PendingRegistries<T> pending = (PendingRegistries<T>) pendingRegistries
				.computeIfAbsent(deferredObject.getRegistryKey(), k ->
						new PendingRegistries<>((ResourceKey<? extends Registry<T>>) deferredObject.getRegistryKey()));
		pending.add(deferredObject, supplier);
	}

	@Override
	public <T> void registerNewRegistry(Registry<T> registry) {
		pendingRegistryTypes.add(registry);
	}

	@Override
	public void registerReloadListener(PackType packType, IdentifiableResourceReloadListener listener) {
		if (packType == PackType.SERVER_DATA) {
			serverDataReloadListeners.add(listener);
		} else {
			clientDataReloadListeners.add(listener);
		}
	}

	public void registerEvent(RegisterEvent event) {
		pendingRegistries.values().forEach(pending -> pending.register(event));
	}

	@SuppressWarnings({"deprecation", "unchecked", "rawtypes"})
	public void registerNewRegistries() {
		if (BuiltInRegistries.REGISTRY instanceof MappedRegistry<?> rootRegistry)
			rootRegistry.unfreeze();

		for (Registry<?> registry : pendingRegistryTypes) {
			((WritableRegistry) BuiltInRegistries.REGISTRY).register(registry.key(), registry, RegistrationInfo.BUILT_IN);
		}

		if (BuiltInRegistries.REGISTRY instanceof MappedRegistry<?> rootRegistry)
			rootRegistry.freeze();
	}

	public void addClientReloadListenerEvent(RegisterClientReloadListenersEvent event) {
		for (IdentifiableResourceReloadListener listener : clientDataReloadListeners) {
			event.registerReloadListener(listener);
		}
	}

	public void addServerReloadListenerEvent(AddReloadListenerEvent event) {
		for (IdentifiableResourceReloadListener listener : serverDataReloadListeners) {
			event.addListener(listener);
		}
	}

	private static class PendingRegistries<T> {
		private final ResourceKey<? extends Registry<T>> registryKey;

		private final Map<DeferredObject<? extends T>, Supplier<? extends T>> entries = new HashMap<>();

		public PendingRegistries(ResourceKey<? extends Registry<T>> registryKey) {
			this.registryKey = registryKey;
		}

		public void add(DeferredObject<? extends T> deferredObject, Supplier<? extends T> objectSupplier) {
			entries.put(deferredObject, objectSupplier);
		}

		public void register(RegisterEvent event) {
			entries.forEach((deferredObject, supplier) -> {
				event.register(registryKey, deferredObject.getId(), supplier::get);
				deferredObject.bind(false);
			});
		}
	}
}
