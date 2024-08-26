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

package me.pandamods.pandalib.forge.registry;

import com.google.common.collect.Lists;
import me.pandamods.pandalib.registry.ReloadListenerRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;

public class ReloadListenerRegistryImpl implements ReloadListenerRegistry {
	private static final List<PreparableReloadListener> serverDataReloadListeners = Lists.newArrayList();

	static {
		MinecraftForge.EVENT_BUS.addListener(ReloadListenerRegistryImpl::addReloadListeners);
	}

	@Override
	public void registerListener(PackType type, PreparableReloadListener listener, @Nullable ResourceLocation listenerId, Collection<ResourceLocation> dependencies) {
		if (type == PackType.SERVER_DATA) {
			serverDataReloadListeners.add(listener);
		} else if (type == PackType.CLIENT_RESOURCES) {
			registerClient(listener);
		}
	}

	@OnlyIn(Dist.CLIENT)
	private static void registerClient(PreparableReloadListener listener) {
		((ReloadableResourceManager) Minecraft.getInstance().getResourceManager()).registerReloadListener(listener);
	}

	public static void addReloadListeners(AddReloadListenerEvent event) {
		for (PreparableReloadListener listener : serverDataReloadListeners) {
			event.addListener(listener);
		}
	}
}
