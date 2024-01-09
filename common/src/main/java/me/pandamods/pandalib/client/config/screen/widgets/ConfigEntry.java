package me.pandamods.pandalib.client.config.screen.widgets;

import me.pandamods.pandalib.client.config.Option;
import me.pandamods.pandalib.client.config.screen.ConfigScreen;
import me.pandamods.pandalib.client.screen.widgets.Widget;
import me.pandamods.pandalib.client.screen.widgets.WidgetImpl;
import me.pandamods.pandalib.config.Config;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

import java.awt.*;
import java.lang.reflect.Field;

public abstract class ConfigEntry extends Widget {
	protected final Option option;
	protected final Component title;

	public ConfigEntry(WidgetImpl parent, Data data) {
		super(parent);
		this.option = new Option(data.field, data.origin());

		Config definition = data.config();
		this.title = Component.translatable("config." + definition.modId() + "." +
				definition.name() + ".option." + option.getName());
	}

	@Override
	public void initWidget() {
		super.initWidget();
	}

	@Override
	public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
		super.renderWidget(guiGraphics, mouseX, mouseY, partialTick);
		renderName(guiGraphics);
	}

	protected void renderName(GuiGraphics guiGraphics) {
		guiGraphics.drawString(font, this.title, getNameX(), getNameY(), Color.WHITE.getRGB());
	}

	protected int getNameX() {
		return this.getX() + 4;
	}

	protected int getNameY() {
		return this.getY() + ((this.getHeight() - font.lineHeight) / 2);
	}

	public abstract void save();
	public abstract void load();
	public abstract void reset();

	public Option getOption() {
		return option;
	}

	public record Data(Field field, Config config, Object origin) {}
}
