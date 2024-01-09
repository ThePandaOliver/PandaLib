package me.pandamods.pandalib.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import me.pandamods.pandalib.PandaLib;
import me.pandamods.pandalib.utils.ClassUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ConfigHolder<T> {
	public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

	private final Map<UUID, T> clientConfigs = new HashMap<>();
	private T serverConfig = null;

	private final Class<T> configClass;
	private final Config definition;
	private final ResourceLocation resourceLocation;
	private final ResourceLocation configPacketId;
	private final boolean synchronize;
	private T config;

	public ConfigHolder(Class<T> configClass, Config config) {
		this.configClass = configClass;
		this.definition = config;

		this.resourceLocation = new ResourceLocation(config.modId(), config.name());
		this.synchronize = config.synchronize();
		this.configPacketId = new ResourceLocation(config.modId(), "config_packet");

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

	public T getLocal() {
		return config;
	}

	public Path getConfigPath() {
		Path path = Platform.getConfigFolder();
		if (!definition.parentDirectory().isBlank()) path = path.resolve(definition.parentDirectory());
		return path.resolve(definition.name() + ".json");
	}

	public void save() {
		if (definition.type().equals(ConfigType.CLIENT) && Platform.getEnvironment().equals(Env.SERVER)) {
			return;
		}
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
		if (definition.type().equals(ConfigType.CLIENT) && Platform.getEnvironment().equals(Env.SERVER)) {
			return false;
		}
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
		this.config = getNewDefault();
	}

	public T getNewDefault() {
		return ClassUtils.constructUnsafely(configClass);
	}

	public ResourceLocation resourceLocation() {
		return resourceLocation;
	}

	public void setPlayersConfig(Player player, JsonObject jsonObject) {
		clientConfigs.put(player.getUUID(), ConfigHolder.GSON.fromJson(jsonObject, this.configClass));
	}

	public void setServerConfig(JsonObject jsonObject) {
		serverConfig = ConfigHolder.GSON.fromJson(jsonObject, this.configClass);
	}
}
