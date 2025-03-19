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

package dev.pandasystems.pandalib.fabric.platform;

import dev.pandasystems.pandalib.platform.services.GameHelper;
import dev.pandasystems.pandalib.utils.Env;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Path;

public class GameHelperImpl implements GameHelper {
	@Override
	public boolean isDevelopmentEnvironment() {
		return FabricLoader.getInstance().isDevelopmentEnvironment();
	}

	@Override
	public boolean isProductionEnvironment() {
		return !FabricLoader.getInstance().isDevelopmentEnvironment();
	}

	@Override
	public Env getEnvironment() {
		return switch (FabricLoader.getInstance().getEnvironmentType()) {
			case CLIENT -> Env.CLIENT;
			case SERVER -> Env.SERVER;
		};
	}

	@Override
	public boolean isClient() {
		return FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT;
	}

	@Override
	public boolean isServer() {
		return FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER;
	}

	@Override
	public Path getGameDir() {
		return FabricLoader.getInstance().getGameDir().toAbsolutePath().normalize();
	}

	@Override
	public Path getConfigDir() {
		return FabricLoader.getInstance().getConfigDir().toAbsolutePath().normalize();
	}

	@Override
	public Path getModDir() {
		return getGameDir().resolve("mods");
	}
}
