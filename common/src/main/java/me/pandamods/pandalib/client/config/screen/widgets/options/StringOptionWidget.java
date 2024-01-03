package me.pandamods.pandalib.client.config.screen.widgets.options;

import me.pandamods.pandalib.client.config.screen.widgets.ConfigOptionWidget;
import me.pandamods.pandalib.client.screen.PandaLibScreen;
import me.pandamods.pandalib.client.screen.widgets.Widget;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;

import java.awt.*;

public class StringOptionWidget extends ConfigOptionWidget {
	private EditBox textField = null;

	public StringOptionWidget(PandaLibScreen screen, Widget parent, Data data) {
		super(screen, parent, data);
	}

	@Override
	public void init() {
		int height = font.lineHeight + 5;
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

	@Override
	public void save() {
		this.option.set(this.textField.getValue());
	}

	@Override
	public void load() {
		this.textField.setValue(option.getAsString());
	}
}