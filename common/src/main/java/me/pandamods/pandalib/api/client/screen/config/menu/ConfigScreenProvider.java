package me.pandamods.pandalib.api.client.screen.config.menu;

import me.pandamods.pandalib.api.annotation.Category;
import me.pandamods.pandalib.api.annotation.ConfigGui;
import me.pandamods.pandalib.api.client.screen.config.ConfigCategory;
import me.pandamods.pandalib.api.client.screen.config.ConfigGuiRegistry;
import me.pandamods.pandalib.api.client.screen.config.option.AbstractConfigOption;
import me.pandamods.pandalib.api.client.screen.config.option.CollapsableObjectWidget;
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
			if (field.isAnnotationPresent(ConfigGui.Excluded.class))
				continue;
			ConfigCategory.Builder categoryBuilder = getOrCreateCategory(field, configHolder.getTranslatableName());

			boolean isCollapsableObject = field.isAnnotationPresent(ConfigGui.CollapsableObject.class);
			boolean isTransitiveObject = field.isAnnotationPresent(ConfigGui.TransitiveObject.class);

			String name = String.format("%s.%s", baseName, field.getName());
			if (isCollapsableObject || isTransitiveObject) {
//				if (isCollapsableObject)
//					categoryBuilder.addOption(new CollapsableObjectWidget(Component.translatable(name)));
				try {
					addOptions(field.get(config), field.get(defaultConfig), name);
				} catch (IllegalAccessException e) {
					throw new RuntimeException(e);
				}
				continue;
			}

			if (ConfigGuiRegistry.getGui(field).isPresent()) {
				AbstractConfigOption<?> option = ConfigGuiRegistry.getGui(field).get().create(Component.translatable(name));
				option.onSaveEvent.register(object -> ClassUtils.setFieldUnsafely(config, field, object));
				option.onLoadEvent.register(() -> ClassUtils.getFieldUnsafely(config, field));
				option.onResetEvent.register(() -> ClassUtils.getFieldUnsafely(defaultConfig, field));
				categoryBuilder.addOption(option);
			}
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