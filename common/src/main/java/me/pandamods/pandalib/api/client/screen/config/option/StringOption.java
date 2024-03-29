package me.pandamods.pandalib.api.client.screen.config.option;

import me.pandamods.pandalib.api.config.ConfigData;
import net.minecraft.network.chat.Component;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class StringOption<T extends ConfigData> extends ConfigOption<String, T> {
	public StringOption(Component name, Function<T, String> load, BiConsumer<T, String> save, Function<T, String> loadDefault) {
		super(name, load, save, loadDefault);
	}
}
