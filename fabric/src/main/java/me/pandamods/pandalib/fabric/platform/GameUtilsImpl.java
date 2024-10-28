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

import me.pandamods.pandalib.platform.GameUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Path;

public class GameUtilsImpl implements GameUtils {
	@Override
	public Path getGamePath() {
		return FabricLoader.getInstance().getGameDir();
	}

	@Override
	public Path getConfigPath() {
		return FabricLoader.getInstance().getConfigDir();
	}

	@Override
	public Path getModsPath() {
		return getGamePath().resolve("mods");
	}

	@Override
	public Env getEnvironment() {
		return FabricLoader.getInstance().getEnvironmentType().equals(EnvType.CLIENT) ? Env.CLIENT : Env.SERVER;
	}
}
