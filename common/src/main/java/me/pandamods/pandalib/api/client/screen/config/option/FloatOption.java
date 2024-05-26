package me.pandamods.pandalib.api.client.screen.config.option;

import me.pandamods.pandalib.api.client.screen.layouts.PLGridLayout;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;

import java.lang.reflect.Field;
import java.util.regex.Pattern;

public class FloatOption extends AbstractConfigOption<Float> {
	private EditBox inputField;

	public FloatOption(Component name, Field field) {
		super(name, field);
		inputField = new EditBox(Minecraft.getInstance().font, 0, 0, 100, 16, name);
	}

	@Override
	protected void setValue(Float value) {
		inputField.setValue(String.valueOf(value));
	}

	@Override
	protected Float getValue() {
		return Float.valueOf(inputField.getValue());
	}

	@Override
	public void init() {
		PLGridLayout grid = new PLGridLayout().spacing(2);
		grid.defaultCellSetting().alignVerticallyMiddle();

		grid.addChild(inputField = new EditBox(Minecraft.getInstance().font, 0, 0, 150, 16, this.inputField, this.name), 0, 0);
		this.inputField.setMaxLength(Integer.MAX_VALUE);
		this.inputField.setFilter(s -> Pattern.compile("[0-9]*.?[0-9]*").matcher(s).matches());

		addActionButtons(grid, 2);

		grid.quickArrange(this::addElement, 0, 0, this.getWidth() - 5, this.getHeight(), 1f, 0.5f);
		super.init();
	}
}