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

package me.pandamods.pandalib.core.extensions;

import com.google.gson.JsonObject;
import me.pandamods.pandalib.api.config.ConfigData;
import me.pandamods.pandalib.api.config.PandaLibConfig;
import me.pandamods.pandalib.api.config.holders.ClientConfigHolder;
import me.pandamods.pandalib.api.config.holders.ConfigHolder;
import net.minecraft.resources.ResourceLocation;

public interface PlayerExtension {
	default <T extends ConfigData> void pandaLib$setConfig(T config) {}
	default <T extends ConfigData> T pandaLib$getConfig(Class<T> configClass) {
		return PandaLibConfig.getConfig(configClass).get();
	}

	@Deprecated(forRemoval = true, since = "0.2.1")
	default <T extends ConfigData> void pandaLib$setConfig(ResourceLocation resourceLocation, T config) {
		pandaLib$setConfig(config);
	}
	@Deprecated(forRemoval = true, since = "0.2.1")
	default <T extends ConfigData> T pandaLib$getConfig(ConfigHolder<T> configClass) {
		return pandaLib$getConfig(configClass.getConfigClass());
	}
}
