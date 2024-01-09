package me.pandamods.pandalib.client.config.screen.widgets;

import me.pandamods.pandalib.client.config.ConfigCategoryImpl;
import me.pandamods.pandalib.client.screen.widgets.tab.ObjectTab;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public class ConfigTabButton extends ObjectTab<ConfigCategoryImpl> {
	public ConfigTabButton(ConfigCategoryImpl value) {
		super(value);
	}

	@Override
	public @NotNull Component getMessage() {
		return Component.translatable(getValue().getFullName());
	}
}
