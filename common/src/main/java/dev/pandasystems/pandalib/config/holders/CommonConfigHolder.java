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

package dev.pandasystems.pandalib.config.holders;

import dev.pandasystems.pandalib.config.Config;
import dev.pandasystems.pandalib.config.ConfigData;
import dev.pandasystems.pandalib.platform.Services;
import dev.pandasystems.pandalib.utils.Env;

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
