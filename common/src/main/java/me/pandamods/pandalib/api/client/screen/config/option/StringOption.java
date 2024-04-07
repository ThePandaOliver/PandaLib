package me.pandamods.pandalib.api.client.screen.config.option;

import me.pandamods.pandalib.api.config.ConfigData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class StringOption extends ConfigOption<String> {
	private EditBox inputField = null;

	public StringOption(Component name, Supplier<String> load, Consumer<String> save, Supplier<String> loadDefault) {
		super(name, load, save, loadDefault);
	}

	@Override
	protected void init() {
		int x = this.getWidth() - 100 - 4;
		int y = (this.getHeight() - 16) / 2;
		EditBox inputField = new EditBox(this.minecraft.font, x, y, 100, 16, this.inputField, Component.empty());
		inputField.setValue(this.load.get());
		this.addRenderableWidget(inputField);

		super.init();
	}
}
