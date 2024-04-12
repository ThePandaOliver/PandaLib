package me.pandamods.pandalib.api.client.screen.config;

import me.pandamods.pandalib.api.client.screen.config.option.StringOption;
import me.pandamods.pandalib.core.utils.PriorityMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@Environment(EnvType.CLIENT)
public class ConfigWidgetRegistry {
	private static final PriorityMap<Function<Field, Boolean>, OptionWidgetProvider<?>> widgetProviders = new PriorityMap<>();

	static {
		registerGuiByType(0, StringOption::new, String.class);
	}

	public static <Y> void registerGui(int priority, OptionWidgetProvider<Y> provider, Function<Field, Boolean> prediction) {
		widgetProviders.put(priority, prediction, provider);
	}

	public static <Y> void registerGuiByType(int priority, OptionWidgetProvider<Y> provider, Class<?>... types) {
		for (Class<?> type : types) {
			registerGui(priority, provider, field -> field.getType().equals(type));
		}
	}

	public static <U extends Annotation, Y> void registerGuiByAnnotation(int priority, OptionWidgetProvider<Y> provider, Class<U> annotation) {
		registerGui(priority, provider, field -> field.getAnnotation(annotation) != null);
	}

	public static Optional<OptionWidgetProvider<?>> getGui(Field field) {
		for (Map.Entry<Function<Field, Boolean>, OptionWidgetProvider<?>> entry : widgetProviders.entrySet()) {
			if (entry.getKey().apply(field)) return Optional.of(entry.getValue());
		}
		return Optional.empty();
	}
}
