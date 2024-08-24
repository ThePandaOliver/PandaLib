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

package me.pandamods.pandalib.platform;

import me.pandamods.pandalib.utils.Env;

import java.nio.file.Path;

public interface GameUtils {
	Path getGamePath();
	Path getConfigPath();
	Path getModsPath();

	/**
	 * Retrieves the current environment.
	 *
	 * @return the current environment, either CLIENT or SERVER
	 */
	Env getEnvironment();

	default boolean isClient() {
		return getEnvironment() == Env.CLIENT;
	}
	default boolean isServer() {
		return getEnvironment() == Env.SERVER;
	}
}
