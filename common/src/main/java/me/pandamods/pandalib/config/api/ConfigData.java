package me.pandamods.pandalib.config.api;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import me.pandamods.pandalib.config.api.holders.ConfigHolder;

public interface ConfigData {
	default <T extends ConfigData> JsonObject onLoad(ConfigHolder<T> configHolder, JsonObject configJson) {
		return configJson;
	}
	default <T extends ConfigData> JsonObject onSave(ConfigHolder<T> configHolder, JsonObject configJson) {
		return configJson;
	}

	default GsonBuilder buildGson(GsonBuilder builder) {
		return builder;
	}
}
