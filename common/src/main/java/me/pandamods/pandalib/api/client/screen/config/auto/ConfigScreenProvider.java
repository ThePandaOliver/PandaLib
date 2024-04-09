package me.pandamods.pandalib.api.client.screen.config.auto;

import me.pandamods.pandalib.api.config.ConfigData;
import net.minecraft.client.gui.screens.Screen;

import java.util.function.Supplier;

public class ConfigScreenProvider<T extends ConfigData> implements Supplier<Screen> {
	@Override
	public Screen get() {
		return null;
	}

	private ConfigCategory getOrCreateCategoryForField(
			Field field,
			ConfigBuilder screenBuilder,
			Map<String, ResourceLocation> backgroundMap,
			String baseI13n
	) {
		String categoryName = "default";

		if (field.isAnnotationPresent(ConfigEntry.Category.class))
			categoryName = field.getAnnotation(ConfigEntry.Category.class).value();

		Component categoryKey = Component.translatable(categoryFunction.apply(baseI13n, categoryName));

		if (!screenBuilder.hasCategory(categoryKey)) {
			ConfigCategory category = screenBuilder.getOrCreateCategory(categoryKey);
			if (backgroundMap.containsKey(categoryName)) {
				category.setCategoryBackground(backgroundMap.get(categoryName));
			}
			return category;
		}

		return screenBuilder.getOrCreateCategory(categoryKey);
	}
}
