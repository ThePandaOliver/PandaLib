package me.pandamods.pandalib.api.client.screen.config;

import me.pandamods.pandalib.api.client.screen.config.option.ConfigOption;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.chat.Component;

import java.util.function.Consumer;
import java.util.function.Supplier;

@Environment(EnvType.CLIENT)
public interface OptionGuiProvider<T> {
	ConfigOption<T> create(Component name, Supplier<T> load, Consumer<T> save, Supplier<T> loadDefault);
}