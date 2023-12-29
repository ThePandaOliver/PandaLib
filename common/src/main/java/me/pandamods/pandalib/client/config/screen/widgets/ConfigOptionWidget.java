package me.pandamods.pandalib.client.config.screen.widgets;

import me.pandamods.pandalib.client.screen.widgets.Widget;
import me.pandamods.pandalib.config.Config;
import me.pandamods.pandalib.config.ConfigHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.lang.reflect.Field;

public abstract class ConfigOptionWidget extends Widget {
	private final Field field;
	public final Component title;

	public ConfigOptionWidget(@Nullable Widget parentWidget, Screen screen, Field field, ConfigHolder<?> configHolder) {
		super(parentWidget, screen);

		this.field = field;
		Config definition = configHolder.getDefinition();
		this.title = Component.translatable("config." + definition.modId() + "." +
				definition.name() + ".option." + field.getName());
	}

	@Override
	public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
		super.renderWidget(guiGraphics, mouseX, mouseY, partialTick);
		renderName(guiGraphics);
	}

	public void renderName(GuiGraphics guiGraphics) {
		Font font = Minecraft.getInstance().font;
		int y = this.getY() + ((this.getHeight() - font.lineHeight) / 2);
		guiGraphics.drawString(font, this.title, this.getX() + 4, y, Color.WHITE.getRGB());
	}

	public boolean isMouseOver(double mouseX, double mouseY) {
		return getMinX() <= mouseX && getMaxX() >= mouseX && getMinY() <= mouseY && getMaxY() >= mouseY;
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		return super.mouseClicked(mouseX, mouseY, button);
	}
}
