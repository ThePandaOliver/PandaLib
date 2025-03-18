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

package me.pandamods.pandalib.config.holders;

import me.pandamods.pandalib.config.Config;
import me.pandamods.pandalib.config.ConfigData;
import me.pandamods.pandalib.platform.Services;
import me.pandamods.pandalib.utils.Env;

public class CommonConfigHolder<T extends ConfigData> extends ConfigHolder<T> {
	private T commonConfig;

	public CommonConfigHolder(Class<T> configClass, Config config) {
		super(configClass, config);
	}

	@SuppressWarnings("unchecked")
	public <C extends ConfigData> void setCommonConfig(C config) {
		this.commonConfig = (T) config;
	}

	@Override
	public T get() {
		if (Services.GAME.getEnvironment().equals(Env.CLIENT) && this.commonConfig != null)
			return this.commonConfig;
		return super.get();
	}
}
