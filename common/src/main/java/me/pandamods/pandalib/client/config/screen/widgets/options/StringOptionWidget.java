package me.pandamods.pandalib.client.config.screen.widgets.options;

import me.pandamods.pandalib.client.config.screen.widgets.ConfigOptionWidget;
import me.pandamods.pandalib.client.screen.widgets.Widget;
import me.pandamods.pandalib.config.ConfigHolder;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.lang.reflect.Field;
import java.util.function.Consumer;

public class StringOptionWidget extends ConfigOptionWidget {
	private EditBox textField = null;

	public StringOptionWidget(@Nullable Widget parentWidget, Screen screen, Field field, ConfigHolder<?> configHolder) {
		super(parentWidget, screen, field, configHolder);
	}

	@Override
	public void init() {
		int height = font.lineHeight + 3;
		int width = 100;
		this.textField = new EditBox(this.font, 0, 0, width, height, this.textField, Component.empty());
		this.textField.setMaxLength(50);
		this.textField.setVisible(true);
		this.textField.setTextColor(Color.white.getRGB());
		this.addRenderableWidget(textField);

		super.init();
	}

	@Override
	public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
		textField.setPosition(getMaxX() - textField.getWidth() - 10, getY() + (getHeight() - textField.getHeight()) / 2);
		super.renderWidget(guiGraphics, mouseX, mouseY, partialTick);
	}

	@Override
	public int getHeight() {
		return 40;
	}
}
