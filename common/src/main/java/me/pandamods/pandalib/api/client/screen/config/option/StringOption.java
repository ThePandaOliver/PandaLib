package me.pandamods.pandalib.api.client.screen.config.option;

import me.pandamods.pandalib.api.client.screen.layouts.PLGridLayout;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;

import java.lang.reflect.Field;

public class StringOption extends AbstractConfigOption<String> {
	private EditBox inputField;

	public StringOption(Component name, Field field) {
		super(name, field);
		inputField = new EditBox(Minecraft.getInstance().font, 0, 0, 100, 16, name);
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
		PLGridLayout grid = new PLGridLayout().spacing(2);
		grid.defaultCellSetting().alignVerticallyMiddle();

		grid.addChild(inputField = new EditBox(Minecraft.getInstance().font, 0, 0, 150, 16, this.inputField, this.name), 0, 0);
		this.inputField.setMaxLength(Integer.MAX_VALUE);

		addActionButtons(grid, 2);

		grid.quickArrange(this::addElement, 0, 0, this.getWidth() - 5, this.getHeight(), 1f, 0.5f);
	}
}