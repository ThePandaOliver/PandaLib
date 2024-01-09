package me.pandamods.pandalib.config;

import me.pandamods.pandalib.PandaLib;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class ConfigRegistry {
	public static Map<Class<?>, ConfigHolder<?>> registeredConfigs = new HashMap<>();

	public static <T> ConfigHolder<T> register(Class<T> configClass) {
		if (registeredConfigs.containsKey(configClass))
			throw new RuntimeException(String.format("Config %s is already registered", configClass));

		Config config = configClass.getAnnotation(Config.class);
		if (config == null)
			throw new RuntimeException(String.format("The annotation @Config is not present on %s", configClass));

		ConfigHolder<T> holder = new ConfigHolder<>(configClass, config);
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
