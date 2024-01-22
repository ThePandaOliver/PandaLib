package me.pandamods.pandalib.config.api;

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

	public static <T extends ConfigData, E extends ConfigHolder<T>> E register(Class<T> configClass, ConfigHolderProvider<T, E> provider) {
		if (registeredConfigs.containsKey(configClass))
			throw new RuntimeException(String.format("Config %s is already registered", configClass));

		Config config = configClass.getAnnotation(Config.class);
		if (config == null)
			throw new RuntimeException(String.format("The annotation @Config is not present on %s", configClass));

		E holder = provider.provide(configClass, config);
		registeredConfigs.put(configClass, holder);
		return holder;
	}

	public static <T extends ConfigData> ClientConfigHolder<T> registerClient(Class<T> configClass) {
		return ConfigRegistry.<T, ClientConfigHolder<T>>register(configClass, ClientConfigHolder::new);
	}


	public static <T extends ConfigData> CommonConfigHolder<T> registerCommon(Class<T> configClass) {
		return ConfigRegistry.<T, CommonConfigHolder<T>>register(configClass, CommonConfigHolder::new);
	}


	@SuppressWarnings("unchecked")
	public static <T extends ConfigData> ConfigHolder<T> getConfig(Class<T> configClass) {
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

	public interface ConfigHolderProvider<T extends ConfigData, E extends ConfigHolder<T>> {
		E provide(Class<T> configClass, Config config);
	}
}
