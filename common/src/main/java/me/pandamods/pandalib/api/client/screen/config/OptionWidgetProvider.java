package me.pandamods.pandalib.api.client.screen.config;

import me.pandamods.pandalib.api.client.screen.config.option.ConfigOption;
import net.minecraft.network.chat.Component;

import java.util.function.Consumer;
import java.util.function.Supplier;

public interface OptionWidgetProvider<T> {
	ConfigOption<T> create(Component name, Supplier<T> load, Consumer<T> save, Supplier<T> loadDefault);
}