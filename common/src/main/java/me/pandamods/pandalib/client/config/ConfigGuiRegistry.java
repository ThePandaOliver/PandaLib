package me.pandamods.pandalib.client.config;

import me.pandamods.pandalib.client.config.screen.widgets.ConfigOptionWidget;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Environment(EnvType.CLIENT)
public class ConfigGuiRegistry {
	private static final Map<Class<?>, ConfigOptionWidgetProvider> registeredWidgetByClass = new HashMap<>();

	public static void registerByClass(Class<?> type, ConfigOptionWidgetProvider widgetProvider) {
		registeredWidgetByClass.put(type, widgetProvider);
	}

	public static ConfigOptionWidgetProvider getByClass(Class<?> type) {
		return registeredWidgetByClass.get(type);
	}
}