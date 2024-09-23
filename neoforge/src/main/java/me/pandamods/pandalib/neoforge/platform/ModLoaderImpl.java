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

package me.pandamods.pandalib.neoforge.platform;

import me.pandamods.pandalib.platform.ModLoader;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLLoader;

import java.nio.file.Path;

public class ModLoaderImpl implements ModLoader {
	@Override
	public boolean isModLoaded(String modId) {
		return ModList.get().isLoaded(modId);
	}

	@Override
	public boolean isFabric() {
		return false;
	}

	@Override
	public boolean isMinecraftForge() {
		return false;
	}

	@Override
	public boolean isNeoForge() {
		return true;
	}

	@Override
	public boolean isDevelopmentEnvironment() {
		return !FMLLoader.isProduction();
	}
}
