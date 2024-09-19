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

import me.pandamods.pandalib.platform.ModLoader;
import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Path;

public class ModLoaderImpl implements ModLoader {
	@Override
	public boolean isModLoaded(String modId) {
		return FabricLoader.getInstance().isModLoaded(modId);
	}

	@Override
	public boolean isFabric() {
		return true;
	}

	@Override
	public boolean isMinecraftForge() {
		return false;
	}

	@Override
	public boolean isNeoForge() {
		return false;
	}

	@Override
	public boolean isDevelopmentEnvironment() {
		return FabricLoader.getInstance().isDevelopmentEnvironment();
	}
}
