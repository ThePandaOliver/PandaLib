package me.pandamods.pandalib.api.client.screen.config.option;

import me.pandamods.pandalib.api.client.screen.elements.widgets.PLEditBox;
import me.pandamods.pandalib.api.client.screen.elements.widgets.inputs.Slider;
import me.pandamods.pandalib.api.client.screen.elements.widgets.inputs.SliderInput;
import me.pandamods.pandalib.api.client.screen.layouts.PLGrid;
import me.pandamods.pandalib.api.client.screen.layouts.PLGridLayout;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;

import java.lang.reflect.Field;
import java.util.regex.Pattern;

public class FloatOption extends AbstractConfigOption<Float> {
//	private PLEditBox inputField;
	private final SliderInput slider;

	public FloatOption(Component name, Field field) {
		super(name, field);
//		inputField = new PLEditBox(Minecraft.getInstance().font, name);
//		inputField.setPosition(0, 0);
//		inputField.setSize(100, 16);

		slider = new SliderInput(0, 10, 0.25f);
		slider.setPosition(0, 0);
		slider.setSize(150, 16);
	}

	@Override
	protected void setValue(Float value) {
		slider.setValue(value);
	}

	@Override
	protected Float getValue() {
		return (float) slider.getValue();
	}

	@Override
	public void init() {
		PLGrid grid = new PLGrid().spacing(2);
		grid.defaultCellSetting().alignVerticallyMiddle();

//		grid.addChild(inputField = new PLEditBox(Minecraft.getInstance().font, this.inputField, this.name), 0, 0);
//		this.inputField.setMaxLength(Integer.MAX_VALUE);
//		this.inputField.setFilter(s -> Pattern.compile("-?[0-9]*.?[0-9]*").matcher(s).matches());

		grid.addChild(slider, 0, 0);

		addActionButtons(grid, 2);

		grid.quickArrange(this::addElement, getX(), getY(), this.getWidth() - 5, this.getHeight(), 1f, 0.5f);
		super.init();
	}
}