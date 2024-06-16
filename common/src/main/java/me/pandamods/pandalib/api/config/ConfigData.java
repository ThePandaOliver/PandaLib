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

package me.pandamods.pandalib.api.config;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import me.pandamods.pandalib.api.config.holders.ConfigHolder;

public interface ConfigData {
	default <T extends ConfigData> void onLoad(ConfigHolder<T> configHolder, JsonObject configJson) {}
	default <T extends ConfigData> JsonObject onSave(ConfigHolder<T> configHolder, JsonObject configJson) {
		return configJson;
	}

	default GsonBuilder buildGson(GsonBuilder builder) {
		return builder;
	}
}
