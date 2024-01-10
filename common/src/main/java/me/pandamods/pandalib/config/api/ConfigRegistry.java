package me.pandamods.pandalib.config.api;

import me.pandamods.pandalib.config.ClientConfig;
import me.pandamods.pandalib.config.api.holders.ClientConfigHolder;
import me.pandamods.pandalib.config.api.holders.CommonConfigHolder;
import me.pandamods.pandalib.config.api.holders.ConfigHolder;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class ConfigRegistry {
	public static Map<Class<?>, ConfigHolder<?>> registeredConfigs = new HashMap<>();

	@SuppressWarnings("unchecked")
	public static <T, E extends ConfigHolder<T>> E register(Class<T> configClass) {
		if (registeredConfigs.containsKey(configClass))
			throw new RuntimeException(String.format("Config %s is already registered", configClass));

		Config config = configClass.getAnnotation(Config.class);
		if (config == null)
			throw new RuntimeException(String.format("The annotation @Config is not present on %s", configClass));

		E holder = config.type().equals(ConfigType.CLIENT) ?
				(E) new ClientConfigHolder<>(configClass, config) :
				(E) new CommonConfigHolder<>(configClass, config);
		registeredConfigs.put(configClass, holder);
		return holder;
	}

	@SuppressWarnings("unchecked")
	public static <T> ConfigHolder<T> getConfig(Class<T> configClass) {
		return (ConfigHolder<T>) registeredConfigs.get(configClass);
	}

	public static Optional<ConfigHolder<?>> getConfig(ResourceLocation resourceLocation) {
		return registeredConfigs.values().stream()
				.filter(configHolder -> configHolder.resourceLocation().equals(resourceLocation)).findFirst();
	}

	public static Map<Class<?>, ConfigHolder<?>> getConfigs() {
		return registeredConfigs;
	}

	public static Map<Class<?>, ConfigHolder<?>> getConfigs(String modId) {
		return registeredConfigs.entrySet().stream()
				.filter(entry -> entry.getValue().getDefinition().modId().equals(modId))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
	}
}
