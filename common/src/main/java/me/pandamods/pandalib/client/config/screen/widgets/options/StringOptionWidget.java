package me.pandamods.pandalib.client.config.screen.widgets.options;

import me.pandamods.pandalib.client.config.screen.ConfigScreen;
import me.pandamods.pandalib.client.config.screen.widgets.ConfigEntryList;
import me.pandamods.pandalib.client.config.screen.widgets.GenericConfigEntry;
import me.pandamods.pandalib.client.screen.widgets.WidgetImpl;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;

import java.awt.*;
import java.util.Objects;

public class StringOptionWidget extends GenericConfigEntry {
	private EditBox textField;

	public StringOptionWidget(WidgetImpl parent, Data data) {
		super(parent, data);

		int height = font.lineHeight + 5;
		int width = 100;
		this.textField = new EditBox(this.font, 0, 0, width, height, null, Component.empty());
		this.textField.setMaxLength(50);
		this.textField.setVisible(true);
		this.textField.setTextColor(Color.white.getRGB());
	}

	@Override
	public void initWidget() {
		this.addElement(this.textField);

		super.initWidget();
	}

	@Override
	public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
//		textField.setPosition(getMaxX() - textField.getWidth() - 10 - getRightPadding(), getY() + (getHeight() - textField.getHeight()) / 2);
		super.renderWidget(guiGraphics, mouseX, mouseY, partialTick);
	}

	@Override
	public boolean canReset() {
//		return !Objects.equals(getOption().getDefaultAsString(), textField.getValue());
		return false;
	}

	@Override
	public boolean canUndo() {
		return !Objects.equals(getOption().getAsString(), textField.getValue());
	}

	@Override
	public void save() {
		this.option.set(this.textField.getValue());
	}

	@Override
	public void load() {
		this.textField.setValue(option.getAsString());
	}

	@Override
	public void reset() {
//		this.textField.setValue(option.getDefaultAsString());
	}
}