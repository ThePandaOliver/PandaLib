package me.pandamods.pandalib.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.architectury.platform.Platform;
import me.pandamods.pandalib.PandaLib;
import me.pandamods.pandalib.utils.ClassUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class ConfigHolder<T> {
	public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

	private final Class<T> configClass;
	private final Config definition;

	private T config;

	public ConfigHolder(Class<T> configClass, Config config) {
		this.configClass = configClass;
		this.definition = config;

		if (this.load()) {
			this.save();
		}
	}

	public Class<T> getConfigClass() {
		return configClass;
	}

	public Config getDefinition() {
		return definition;
	}

	public T get() {
		return config;
	}

	public Path getConfigPath() {
		Path path = Platform.getConfigFolder();
		if (!definition.parentDirectory().isBlank()) path = path.resolve(definition.parentDirectory());
		return path.resolve(definition.name() + ".json");
	}

	public void save() {
		Path configPath = getConfigPath();
		try {
			Files.createDirectories(configPath.getParent());
			BufferedWriter writer = Files.newBufferedWriter(configPath);
			GSON.toJson(config, writer);
			writer.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public boolean load() {
		Path configPath = getConfigPath();
		if (Files.exists(configPath)) {
			try (BufferedReader reader = Files.newBufferedReader(configPath)) {
				this.config = GSON.fromJson(reader, configClass);
			} catch (IOException e) {
				PandaLib.LOGGER.error("Failed to load config '{}', using default!", configClass, e);
				resetToDefault();
				return false;
			}
		} else {
			resetToDefault();
		}
		return true;
	}

	public void resetToDefault() {
		this.config = getDefault();
	}

	public T getDefault() {
		return ClassUtils.constructUnsafely(configClass);
	}
}
