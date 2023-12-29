package me.pandamods.pandalib.client.config.screen.widgets.options;

import me.pandamods.pandalib.client.config.screen.widgets.ConfigOptionWidget;
import me.pandamods.pandalib.client.screen.widgets.Widget;
import me.pandamods.pandalib.config.ConfigHolder;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.lang.reflect.Field;

public class BooleanOptionWidget extends ConfigOptionWidget {
	private final ToggleButton button;

	public BooleanOptionWidget(@Nullable Widget parentWidget, Screen screen, Field field, ConfigHolder<?> configHolder) {
		super(parentWidget, screen, field, configHolder);

		int height = 20;
		int width = 100;
		this.button = new ToggleButton(0, 0, width, height, CommonComponents.GUI_YES, CommonComponents.GUI_NO);
	}

	@Override
	public void init() {
		this.addRenderableWidget(this.button);

		super.init();
	}

	@Override
	public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
		button.setPosition(getMaxX() - button.getWidth() - 10, getY() + (getHeight() - button.getHeight()) / 2);
		super.renderWidget(guiGraphics, mouseX, mouseY, partialTick);
	}

	@Override
	public int getHeight() {
		return 40;
	}

	static class ToggleButton extends AbstractButton {
		private final Component trueComponent;
		private final Component falseComponent;
		private boolean value = false;

		public ToggleButton(int x, int y, int width, int height, Component trueComponent, Component falseComponent) {
			super(x, y, width, height, Component.empty());
			this.trueComponent = trueComponent;
			this.falseComponent = falseComponent;
		}

		@Override
		public void onPress() {
			this.value = !this.value;
		}

		@Override
		protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {

		}

		@Override
		public Component getMessage() {
			return this.value ? this.trueComponent : this.falseComponent;
		}

		@Override
		public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
			super.render(guiGraphics, mouseX, mouseY, partialTick);
		}
	}
}
