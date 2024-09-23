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

package me.pandamods.pandalib.fabric.platform;

import me.pandamods.pandalib.fabric.registry.ReloadListenerRegistryImpl;
import me.pandamods.pandalib.platform.NetworkHandler;
import me.pandamods.pandalib.platform.GameUtils;
import me.pandamods.pandalib.platform.ModLoader;
import me.pandamods.pandalib.platform.Platform;
import me.pandamods.pandalib.registry.ReloadListenerRegistry;

public class PlatformImpl implements Platform {
	private final ModLoader modLoader = new ModLoaderImpl();
	private final GameUtils gameUtils = new GameUtilsImpl();
	private final NetworkHandler networkHandler = new NetworkHandlerImpl();
	private final ReloadListenerRegistry reloadListenerRegistry = new ReloadListenerRegistryImpl();

	@Override
	public ModLoader getModLoader() {
		return this.modLoader;
	}

	@Override
	public GameUtils getGame() {
		return this.gameUtils;
	}

	@Override
	public NetworkHandler getNetwork() {
		return this.networkHandler;
	}

	@Override
	public ReloadListenerRegistry getReloadListenerRegistry() {
		return this.reloadListenerRegistry;
	}
}
