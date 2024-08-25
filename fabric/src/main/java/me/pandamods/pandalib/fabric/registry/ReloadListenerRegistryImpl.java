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

package me.pandamods.pandalib.fabric.registry;

import com.google.common.primitives.Longs;
import me.pandamods.pandalib.PandaLib;
import me.pandamods.pandalib.registry.ReloadListenerRegistry;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

import java.security.SecureRandom;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class ReloadListenerRegistryImpl implements ReloadListenerRegistry {
	private static final SecureRandom RANDOM = new SecureRandom();

	@Override
	public void registerListener(PackType type, PreparableReloadListener listener, @Nullable ResourceLocation listenerId, Collection<ResourceLocation> dependencies) {
		byte[] bytes = new byte[8];
		RANDOM.nextBytes(bytes);
		ResourceLocation id = listenerId != null ? listenerId :
				PandaLib.location("reload_" + StringUtils.leftPad(Math.abs(Longs.fromByteArray(bytes)) + "", 19, '0'));

		ResourceManagerHelper.get(type).registerReloadListener(new IdentifiableResourceReloadListener() {
			@Override
			public ResourceLocation getFabricId() {
				return id;
			}

			@Override
			public String getName() {
				return listener.getName();
			}

			@Override
			public Collection<ResourceLocation> getFabricDependencies() {
				return dependencies;
			}

			@Override
			public CompletableFuture<Void> reload(PreparationBarrier preparationBarrier, ResourceManager resourceManager,
												  ProfilerFiller profilerFiller, ProfilerFiller profilerFiller2, Executor executor, Executor executor2) {
				return listener.reload(preparationBarrier, resourceManager, profilerFiller, profilerFiller2, executor, executor2);
			}
		});
	}
}
