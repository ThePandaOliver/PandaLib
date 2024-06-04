package me.pandamods.pandalib.api.client.screen.config.option;

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
		button = new ToggleButton(0, 0, 20, 20);
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
		PLGridLayout grid = new PLGridLayout().spacing(2);
		grid.defaultCellSetting().alignVerticallyMiddle();

		grid.addChild(button, 0, 0);
		addActionButtons(grid, 2);

		grid.quickArrange(this::addElement, getX(), getY(), this.getWidth() - 5, this.getHeight(), 1f, 0.5f);
		super.init();
	}

	public static class ToggleButton extends AbstractButton {
		private boolean state = true;

		public ToggleButton(int x, int y, int width, int height) {
			super(x, y, width, height, Component.empty());
		}

		@Override
		public void onPress() {
			this.state = !state;
		}

		@Override
		public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
			int minX = this.getX();
			int minY = this.getY();
			int maxX = minX + this.getWidth();
			int maxY = minY + this.getHeight();

			guiGraphics.fill(minX, minY, maxX, maxY, Color.white.getRGB());
			guiGraphics.fill(minX + 1, minY + 1, maxX - 1, maxY - 1, Color.black.getRGB());
			if (state)
				guiGraphics.fill(minX + 3, minY + 3, maxX - 3, maxY - 3, Color.white.getRGB());
		}

		@Override
		protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
			this.defaultButtonNarrationText(narrationElementOutput);
		}

		public void setState(boolean state) {
			this.state = state;
		}

		public boolean getState() {
			return state;
		}
	}
}