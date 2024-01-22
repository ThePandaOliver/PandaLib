package me.pandamods.pandalib.config.api;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import me.pandamods.pandalib.config.api.holders.ConfigHolder;
import me.pandamods.pandalib.utils.gsonadapter.QuaternionfTypeAdapter;
import me.pandamods.pandalib.utils.gsonadapter.Vector2fTypeAdapter;
import me.pandamods.pandalib.utils.gsonadapter.Vector3fTypeAdapter;
import me.pandamods.pandalib.utils.gsonadapter.Vector4fTypeAdapter;
import org.joml.Quaternionf;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public interface ConfigData {
	default <T extends ConfigData> void onLoad(ConfigHolder<T> configHolder, JsonObject configJson) {}
	default <T extends ConfigData> void onSave(ConfigHolder<T> configHolder) {}

	default GsonBuilder buildGson(GsonBuilder builder) {
		return builder
				.registerTypeAdapter(Vector2f.class, new Vector2fTypeAdapter())
				.registerTypeAdapter(Vector3f.class, new Vector3fTypeAdapter())
				.registerTypeAdapter(Vector4f.class, new Vector4fTypeAdapter())
				.registerTypeAdapter(Quaternionf.class, new QuaternionfTypeAdapter());
	}
}
