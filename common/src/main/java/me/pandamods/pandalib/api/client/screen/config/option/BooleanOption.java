package me.pandamods.pandalib.api.client.screen.config.option;

import me.pandamods.pandalib.api.client.screen.elements.widgets.buttons.ToggleButton;
import me.pandamods.pandalib.api.client.screen.layouts.PLGrid;
import me.pandamods.pandalib.api.client.screen.layouts.PLGridLayout;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;

import java.awt.*;
import java.lang.reflect.Field;

public class BooleanOption extends AbstractConfigOption<Boolean> {
	private ToggleButton button;

	public BooleanOption(Component name, Field field) {
		super(name, field);
		button = new ToggleButton(0, 0, 20);
	}

	@Override
	protected void setValue(Boolean value) {
		button.setState(value);
	}

	@Override
	protected Boolean getValue() {
		return button.getState();
	}

	@Override
	public void init() {
		PLGrid grid = new PLGrid().spacing(2);
		grid.defaultCellSetting().alignVerticallyMiddle();

		grid.addChild(button, 0, 0);
		addActionButtons(grid, 2);

		grid.quickArrange(this::addElement, getX(), getY(), this.getWidth() - 5, this.getHeight(), 1f, 0.5f);
		super.init();
	}
}