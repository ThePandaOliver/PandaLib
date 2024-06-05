package me.pandamods.pandalib.api.client.screen.config.option;

import me.pandamods.pandalib.api.client.screen.elements.widgets.PLEditBox;
import me.pandamods.pandalib.api.client.screen.layouts.PLGridLayout;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;

import java.lang.reflect.Field;
import java.util.regex.Pattern;

public class IntegerOption extends AbstractConfigOption<Integer> {
	private PLEditBox inputField;

	public IntegerOption(Component name, Field field) {
		super(name, field);
		inputField = new PLEditBox(Minecraft.getInstance().font, name);
		inputField.setPosition(0, 0);
		inputField.setSize(100, 16);
	}

	@Override
	protected void setValue(Integer value) {
		inputField.setValue(String.valueOf(value));
	}

	@Override
	protected Integer getValue() {
		return Integer.valueOf(inputField.getValue());
	}

	@Override
	public void init() {
		PLGridLayout grid = new PLGridLayout().spacing(2);
		grid.defaultCellSetting().alignVerticallyMiddle();

		grid.addChild(inputField = new PLEditBox(Minecraft.getInstance().font, this.inputField, this.name), 0, 0);
		this.inputField.setMaxLength(Integer.MAX_VALUE);
		this.inputField.setFilter(s -> Pattern.compile("-?[0-9]*").matcher(s).matches());

		addActionButtons(grid, 2);

		grid.quickArrange(this::addElement, getX(), getY(), this.getWidth() - 5, this.getHeight(), 1f, 0.5f);
		super.init();
	}
}