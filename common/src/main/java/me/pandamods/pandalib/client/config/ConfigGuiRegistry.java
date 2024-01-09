package me.pandamods.pandalib.client.config;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Environment(EnvType.CLIENT)
public class ConfigGuiRegistry {
	private static final Map<Class<?>, ConfigOptionWidgetProvider> registeredWidgetByClass = new HashMap<>();
	private static final Map<Class<? extends Annotation>, ConfigOptionWidgetProvider> registeredWidgetByAnnotation = new HashMap<>();

	public static void registerByClass(Class<?> type, ConfigOptionWidgetProvider widgetProvider) {
		registeredWidgetByClass.put(type, widgetProvider);
	}

	public static Optional<ConfigOptionWidgetProvider> getByClass(Class<?> type) {
		return Optional.ofNullable(registeredWidgetByClass.get(type));
	}

	public static void registerByAnnotation(Class<? extends Annotation> annotation, ConfigOptionWidgetProvider widgetProvider) {
		registeredWidgetByAnnotation.put(annotation, widgetProvider);
	}

	public static Optional<ConfigOptionWidgetProvider> getByAnnotation(Annotation[] annotations) {
		return registeredWidgetByAnnotation.entrySet().stream().filter(entry ->
				Arrays.stream(annotations).anyMatch(annotation -> entry.getKey() == annotation.annotationType())).findFirst()
				.map(Map.Entry::getValue);
	}

	public static Optional<ConfigOptionWidgetProvider> get(Field field) {
		Optional<ConfigOptionWidgetProvider> widget = getByAnnotation(field.getAnnotations());
		if (widget.isEmpty()) {
			widget = getByClass(field.getType());
		}
		return widget;
	}
}