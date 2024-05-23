package me.pandamods.pandalib.api.client.screen.config.menu;

import me.pandamods.pandalib.api.annotation.ConfigGui;
import me.pandamods.pandalib.api.client.screen.config.ConfigCategory;
import me.pandamods.pandalib.api.client.screen.config.ConfigGuiRegistry;
import me.pandamods.pandalib.api.client.screen.config.option.AbstractConfigOption;
import me.pandamods.pandalib.api.config.ConfigData;
import me.pandamods.pandalib.api.config.holders.ConfigHolder;
import me.pandamods.pandalib.core.utils.ClassUtils;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.lang.reflect.Field;
import java.util.function.Supplier;

public class ConfigScreenProvider<T extends ConfigData> implements Supplier<Screen> {
	private final Screen parent;
	private final ConfigHolder<T> configHolder;

	public ConfigScreenProvider(Screen parent, ConfigHolder<T> configHolder) {
		this.parent = parent;
		this.configHolder = configHolder;
	}

	@Override
	public Screen get() {
		T config = configHolder.get();
		T defaultConfig = configHolder.getNewDefault();

		return new ConfigMenu<>(parent, config.getClass(),
				createCategory(config, defaultConfig, configHolder.getLangName(), configHolder.getLangName())
		);
	}

	private ConfigCategory createCategory(Object config, Object defaultConfig, String baseName, String langName) {
		ConfigCategory.Builder categoryBuilder = ConfigCategory.builder(Component.translatable(langName));
		for (Field field : config.getClass().getFields()) {
			if (field.isAnnotationPresent(ConfigGui.Excluded.class)) continue;

			String name = langName = String.format("%s.%s", baseName, field.getName());
			ConfigGui.LangName langNameAnno = field.getAnnotation(ConfigGui.LangName.class);
			if (langNameAnno != null) langName = langNameAnno.value();
			if (field.isAnnotationPresent(ConfigGui.Category.class)) {
				try {
					categoryBuilder.addCategory(createCategory(field.get(config), field.get(defaultConfig), name, langName));
				} catch (IllegalAccessException e) {
					throw new RuntimeException(e);
				}
				continue;
			}

			if (ConfigGuiRegistry.getGui(field).isPresent()) {
				AbstractConfigOption<?> option = ConfigGuiRegistry.getGui(field).get().create(Component.translatable(langName));
				option.onSaveEvent.register(object -> ClassUtils.setFieldUnsafely(config, field, object));
				option.onLoadEvent.register(() -> ClassUtils.getFieldUnsafely(config, field));
				option.onResetEvent.register(() -> ClassUtils.getFieldUnsafely(defaultConfig, field));
				categoryBuilder.addOption(option);
			}
		}
		return categoryBuilder.build();
	}
}