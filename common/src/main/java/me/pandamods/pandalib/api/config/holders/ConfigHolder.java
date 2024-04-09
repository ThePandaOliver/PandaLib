package me.pandamods.pandalib.api.config.holders;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import dev.architectury.platform.Platform;
import me.pandamods.pandalib.api.client.screen.config.*;
import me.pandamods.pandalib.api.client.screen.config.option.StringOption;
import me.pandamods.pandalib.api.config.Config;
import me.pandamods.pandalib.api.config.ConfigData;
import me.pandamods.pandalib.core.utils.ClassUtils;
import me.pandamods.pandalib.core.utils.PriorityMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Function;

public class ConfigHolder<T extends ConfigData> {
	public final Logger logger;
	private final Gson gson;

	@Environment(EnvType.CLIENT)
	private final PriorityMap<Function<Field, Boolean>, OptionWidgetProvider<?>> widgetProviders = new PriorityMap<>();

	private final Class<T> configClass;
	private final Config definition;
	private final ResourceLocation resourceLocation;
	private final boolean synchronize;
	private T config;

	public ConfigHolder(Class<T> configClass, Config config) {
		this.configClass = configClass;
		this.definition = config;
		this.logger = LoggerFactory.getLogger(config.modId() + " | Config");
		this.gson = getNewDefault().buildGson(new GsonBuilder()).setPrettyPrinting().create();

		this.resourceLocation = new ResourceLocation(config.modId(), config.name());
		this.synchronize = config.synchronize();

		if (this.load()) {
			save();
		}

		registerGuiByType(0, StringOption::new, String.class);
	}

	public Gson getGson() {
		return gson;
	}

	public Class<T> getConfigClass() {
		return configClass;
	}

	public Config getDefinition() {
		return definition;
	}

	public boolean shouldSynchronize() {
		return synchronize;
	}

	public Path getConfigPath() {
		Path path = Platform.getConfigFolder();
		if (!definition.parentDirectory().isBlank()) path = path.resolve(definition.parentDirectory());
		return path.resolve(definition.name() + ".json");
	}

	public void save() {
		JsonObject jsonObject = this.getGson().toJsonTree(this.config).getAsJsonObject();
		this.config.onSave(this, jsonObject);
		Path configPath = getConfigPath();
		try {
			Files.createDirectories(configPath.getParent());
			BufferedWriter writer = Files.newBufferedWriter(configPath);
			this.getGson().toJson(jsonObject, writer);
			writer.close();
			this.logger.info("successfully saved config '{}'", definition.name());
		} catch (IOException e) {
			this.logger.info("Failed to save config '{}'", definition.name());
			throw new RuntimeException(e);
		}
	}

	public boolean load() {
		Path configPath = getConfigPath();
		if (Files.exists(configPath)) {
			try (BufferedReader reader = Files.newBufferedReader(configPath)) {
				JsonObject jsonObject = this.getGson().fromJson(reader, JsonObject.class);
				this.config = this.getGson().fromJson(jsonObject, configClass);
				this.config.onLoad(this, jsonObject);
			} catch (IOException e) {
				this.logger.error("Failed to load config '{}', using default", definition.name(), e);
				resetToDefault();
				return false;
			}
		} else {
			resetToDefault();
			save();
		}
		this.logger.info("successfully loaded config '{}'", definition.name());
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

	public MutableComponent getName() {
		return Component.translatable(String.format("config.%s.%s", resourceLocation.getNamespace(), resourceLocation.getPath()));
	}

	public String modID() {
		return getDefinition().modId();
	}

	public T get() {
		return config;
	}

	@Environment(EnvType.CLIENT)
	public <Y> void registerGui(int priority, OptionWidgetProvider<Y> provider, Function<Field, Boolean> prediction) {
		this.widgetProviders.put(priority, prediction, provider);
	}

	@Environment(EnvType.CLIENT)
	public <Y> void registerGuiByType(int priority, OptionWidgetProvider<Y> provider, Class<?>... types) {
		for (Class<?> type : types) {
			this.registerGui(priority, provider, field -> field.getType().equals(type));
		}
	}

	@Environment(EnvType.CLIENT)
	public <U extends Annotation, Y> void registerGuiByAnnotation(int priority, OptionWidgetProvider<Y> provider, Class<U> annotation) {
		this.registerGui(priority, provider, field -> field.getAnnotation(annotation) != null);
	}

	@Environment(EnvType.CLIENT)
	public Optional<OptionWidgetProvider<?>> getGui(Field field) {
		for (Map.Entry<Function<Field, Boolean>, OptionWidgetProvider<?>> entry : this.widgetProviders.entrySet()) {
			if (entry.getKey().apply(field)) return Optional.of(entry.getValue());
		}
		return Optional.empty();
	}

	@Environment(EnvType.CLIENT)
	public ConfigMenu.Builder<T> buildScreen() {
		ConfigMenu.Builder<T> builder = ConfigMenu.builder(configClass);
		return builder.registerCategories(registerClassedOption(this.get()).values().stream().map(ConfigCategory.Builder::build).toList());
	}
}
