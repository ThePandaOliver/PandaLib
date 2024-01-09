package me.pandamods.pandalib.client.config;

import me.pandamods.pandalib.client.config.screen.widgets.ConfigEntry;
import net.minecraft.network.chat.Component;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ConfigCategory implements ConfigCategoryImpl {
	private final Map<String, ConfigCategory> optionCategories = new LinkedHashMap<>();
	private final Map<Field, ConfigEntry> optionEntries = new LinkedHashMap<>();
	private final ConfigCategoryImpl parent;
	private final String name;

	public ConfigCategory(ConfigCategoryImpl parent, String name) {
		this.parent = parent;
		this.name = name;
	}

	@Override
	public Map<String, ConfigCategory> optionCategories() {
		return optionCategories;
	}

	@Override
	public Map<Field, ConfigEntry> optionEntries() {
		return optionEntries;
	}

	@Override
	public ConfigCategoryImpl getParent() {
		return parent;
	}

	@Override
	public String getName() {
		return name;
	}
}