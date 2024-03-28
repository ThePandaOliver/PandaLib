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
