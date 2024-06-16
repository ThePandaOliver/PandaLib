package me.pandamods.pandalib.api.client.screen.config.option;

import me.pandamods.pandalib.api.client.screen.elements.widgets.PLEditBox;
import me.pandamods.pandalib.api.client.screen.layouts.PLGrid;
import me.pandamods.pandalib.api.client.screen.layouts.PLGridLayout;
import me.pandamods.pandalib.api.utils.screen.PLGuiGraphics;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;

import java.lang.reflect.Field;

public class StringOption extends AbstractConfigOption<String> {
	private PLEditBox inputField;

	public StringOption(Component name, Field field) {
		super(name, field);
		inputField = new PLEditBox(Minecraft.getInstance().font, name);
		inputField.setPosition(0, 0);
		inputField.setSize(100, 16);
	}

	@Override
	protected void setValue(String value) {
		inputField.setValue(value);
	}

	@Override
	protected String getValue() {
		return inputField.getValue();
	}

	@Override
	public void init() {
		PLGrid grid = new PLGrid().spacing(2);
		grid.defaultCellSetting().alignVerticallyMiddle();

		grid.addChild(inputField = new PLEditBox(Minecraft.getInstance().font, this.inputField, this.name), 0, 0);
		this.inputField.setMaxLength(Integer.MAX_VALUE);

		addActionButtons(grid, 2);

		grid.quickArrange(this::addElement, getX(), getY(), this.getWidth() - 5, this.getHeight(), 1f, 0.5f);
		super.init();
	}
}