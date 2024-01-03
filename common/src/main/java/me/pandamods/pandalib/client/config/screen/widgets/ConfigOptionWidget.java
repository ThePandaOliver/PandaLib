package me.pandamods.pandalib.client.config.screen.widgets;

import me.pandamods.pandalib.client.config.Option;
import me.pandamods.pandalib.client.screen.PandaLibScreen;
import me.pandamods.pandalib.client.screen.widgets.Widget;
import me.pandamods.pandalib.config.Config;
import me.pandamods.pandalib.config.ConfigHolder;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

import java.awt.*;
import java.lang.reflect.Field;

public abstract class ConfigOptionWidget extends Widget {
	protected final Option option;
	protected final ConfigHolder<?> configHolder;
	protected final Component title;

	public ConfigOptionWidget(PandaLibScreen screen, Widget parent, Data data) {
		super(screen, parent);
		this.option = new Option(data.field, data.configHolder.get());
		this.configHolder = data.configHolder;

		Config definition = configHolder.getDefinition();
		this.title = Component.translatable("config." + definition.modId() + "." +
				definition.name() + ".option." + option.getName());
	}

	@Override
	public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
		super.renderWidget(guiGraphics, mouseX, mouseY, partialTick);
		renderName(guiGraphics);
	}

	@Override
	public void init() {
		super.init();
		load();
	}

	public void renderName(GuiGraphics guiGraphics) {
		int y = this.getY() + ((this.getHeight() - font.lineHeight) / 2);
		guiGraphics.drawString(font, this.title, this.getX() + 4, y, Color.WHITE.getRGB());
	}

	@Override
	public int getHeight() {
		return 60;
	}

	public abstract void save();
	public abstract void load();

	public record Data(Field field, ConfigHolder<?> configHolder) {}
}
