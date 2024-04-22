package me.pandamods.pandalib.api.client.screen.config;

import me.pandamods.pandalib.api.annotation.Category;
import me.pandamods.pandalib.api.config.ConfigData;
import me.pandamods.pandalib.api.config.holders.ConfigHolder;
import me.pandamods.pandalib.core.utils.ClassUtils;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class ConfigScreenProvider<T extends ConfigData> implements Supplier<Screen> {
	private final Screen parent;
	private final ConfigHolder<T> configHolder;
	private final Map<String, ConfigCategory.Builder> categories = new HashMap<>();

	public ConfigScreenProvider(Screen parent, ConfigHolder<T> configHolder) {
		this.parent = parent;
		this.configHolder = configHolder;
	}

	public ConfigMenu.Builder<T> getBuilder() {
		T config = configHolder.get();
		T defaultConfig = configHolder.getNewDefault();
		ConfigMenu.Builder<T> builder = ConfigMenu.builder(configHolder.getConfigClass()).setTitle(
				Component.translatable(configHolder.getTranslatableName() + ".title"));

		addOptions(config, defaultConfig, configHolder.getTranslatableName() + ".option");
		return builder.registerCategories(categories.values().stream().map(ConfigCategory.Builder::build).toList()).setParent(parent);
	}

	private void addOptions(Object config, Object defaultConfig, String baseName) {
		for (Field field : config.getClass().getFields()) {
			ConfigCategory.Builder categoryBuilder = getOrCreateCategory(field, configHolder.getTranslatableName());

			String name = String.format("%s.%s", baseName, field.getName());
			if (ConfigWidgetRegistry.getGui(field).isPresent())
				categoryBuilder.addOption(ConfigWidgetRegistry.getGui(field).get()
						.create(Component.translatable(name),
								() -> ClassUtils.getFieldUnsafely(config, field),
								object -> ClassUtils.setFieldUnsafely(config, field, object),
								() -> ClassUtils.getFieldUnsafely(defaultConfig, field)));
		}
	}

	@Override
	public Screen get() {
		return getBuilder().Build();
	}

	private ConfigCategory.Builder getOrCreateCategory(Field field, String baseName) {
		String categoryKey = "default";
		if (field.isAnnotationPresent(Category.class))
			categoryKey = field.getAnnotation(Category.class).value();

		Component categoryName = Component.translatable(baseName + ".category." + categoryKey);
		ConfigCategory.Builder categoryBuilder = categories.get(categoryKey);
		if (categoryBuilder == null)
			categories.put(categoryKey, categoryBuilder = ConfigCategory.builder(categoryName));

		return categoryBuilder;
	}
}