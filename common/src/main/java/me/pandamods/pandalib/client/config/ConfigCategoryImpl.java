package me.pandamods.pandalib.client.config;

import me.pandamods.pandalib.client.config.screen.widgets.ConfigEntry;
import me.pandamods.pandalib.client.screen.widgets.WidgetImpl;
import me.pandamods.pandalib.config.Config;
import net.minecraft.network.chat.Component;

import java.lang.reflect.Field;
import java.util.*;

public interface ConfigCategoryImpl {
	Map<String, ConfigCategory> optionCategories();
	Map<Field, ConfigEntry> optionEntries();
	default String getFullName() {
		return getParent().getFullName() + "." + getName();
	}
	String getName();

	default List<ConfigCategoryImpl> history() {
		List<ConfigCategoryImpl> history = this.getParent().history();
		history.add(this.getParent());
		return history;
	}

	default Component getTitle() {
		return Component.translatable(getFullName());
	}

	default void createEntriesAndCategories(WidgetImpl widget, Object configObject, Config definition) {
		for (Field field : configObject.getClass().getDeclaredFields()) {
			if (field.canAccess(configObject)) {
				add(widget, field, configObject, definition);
			}
		}
	}

	default void add(WidgetImpl widget, Field field, Object configObject, Config definition) {
		Config.Gui.Category category = field.getAnnotation(Config.Gui.Category.class);

		if (category != null) {
			addToCategory(widget, category.value(), category.isObject(), field, configObject, definition);
		} else {
			addEntry(widget, field, configObject, definition);
		}
	}

	default void addToCategory(WidgetImpl widget, String name, boolean isObject, Field field, Object configObject, Config definition) {
		optionCategories().putIfAbsent(name, new ConfigCategory(this, name));
		if (isObject) {
			try {
				optionCategories().get(name).createEntriesAndCategories(widget, field.get(configObject), definition);
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		} else {
			optionCategories().get(name).addEntry(widget, field, configObject, definition);
		}
	}

	default void addEntry(WidgetImpl widget, Field field, Object configObject, Config definition) {
		Optional<ConfigOptionWidgetProvider> widgetProvider = ConfigGuiRegistry.get(field);
		widgetProvider.ifPresent(provider -> {
			ConfigEntry entry = provider.create(widget,
					new ConfigEntry.Data(field, definition, configObject));

			optionEntries().put(field, entry);
		});
	}

	default void load() {
		optionEntries().values().forEach(ConfigEntry::load);
		optionCategories().values().forEach(ConfigCategory::load);
	}

	default ConfigCategoryImpl getParent() {
		return null;
	}
}