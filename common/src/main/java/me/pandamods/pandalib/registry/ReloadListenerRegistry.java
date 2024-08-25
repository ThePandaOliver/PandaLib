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

package me.pandamods.pandalib.registry;

import me.pandamods.pandalib.platform.Services;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;

public interface ReloadListenerRegistry {
	static void register(PackType type, PreparableReloadListener listener) {
		register(type, listener, null);
	}

	static void register(PackType type, PreparableReloadListener listener, @Nullable ResourceLocation listenerId) {
		register(type, listener, listenerId, List.of());
	}
	
	static void register(PackType type, PreparableReloadListener listener, @Nullable ResourceLocation listenerId, Collection<ResourceLocation> dependencies) {
		Services.PLATFORM.getReloadListenerRegistry().registerListener(type, listener, listenerId, dependencies);
	}

	void registerListener(PackType type, PreparableReloadListener listener, @Nullable ResourceLocation listenerId, Collection<ResourceLocation> dependencies);
}
