package me.pandamods.pandalib.api.client.screen.config.option;

import me.pandamods.pandalib.api.client.screen.Element;
import me.pandamods.pandalib.api.client.screen.ElementHolder;
import me.pandamods.pandalib.api.config.ConfigData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class ConfigOption<T> extends ElementHolder {
	public final Component name;
	public final Supplier<T> load;
	public final Consumer<T> save;
	public final Supplier<T> loadDefault;

	public ConfigOption(Component name, Supplier<T> load, Consumer<T> save, Supplier<T> loadDefault) {
		this.name = name;
		this.load = load;
		this.save = save;
		this.loadDefault = loadDefault;
	}

	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
		Font font = Minecraft.getInstance().font;
		guiGraphics.drawString(font, name, this.getX() + 2, this.getY() + (this.getHeight() - font.lineHeight) / 2, 0xFFFFFF);

		super.render(guiGraphics, mouseX, mouseY, partialTick);
	}

	public void setY(int y) {
		this.y = y;
	}

	@Override
	public int getWidth() {
		return this.getParent().map(Element::getWidth).orElse(0);
	}

	@Override
	public int getHeight() {
		return 20;
	}
}
