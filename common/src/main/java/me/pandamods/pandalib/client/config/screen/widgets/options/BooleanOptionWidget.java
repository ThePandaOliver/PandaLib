package me.pandamods.pandalib.client.config.screen.widgets.options;

import me.pandamods.pandalib.client.config.screen.ConfigScreen;
import me.pandamods.pandalib.client.config.screen.widgets.ConfigEntryList;
import me.pandamods.pandalib.client.config.screen.widgets.GenericConfigEntry;
import me.pandamods.pandalib.client.screen.widgets.WidgetImpl;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

public class BooleanOptionWidget extends GenericConfigEntry {
	private final ToggleButton button;

	public BooleanOptionWidget(WidgetImpl parent, Data data) {
		super(parent, data);

		int height = 20;
		int width = 100;
		this.button = new ToggleButton(0, 0, width, height, CommonComponents.GUI_YES, CommonComponents.GUI_NO);
	}


	@Override
	public void initWidget() {
		this.addElement(this.button);

		super.initWidget();
	}

	@Override
	public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
//		button.setPosition(getMaxX() - button.getWidth() - 10 - getRightPadding(), getY() + (getHeight() - button.getHeight()) / 2);
		super.renderWidget(guiGraphics, mouseX, mouseY, partialTick);
	}

	@Override
	public boolean canReset() {
//		return getOption().getDefaultAsBoolean() != button.getValue();
		return false;
	}

	@Override
	public boolean canUndo() {
		return getOption().getAsBoolean() != button.getValue();
	}

	@Override
	public void save() {
		this.option.set(this.button.getValue());
	}

	@Override
	public void load() {
		this.button.setValue(option.getAsBoolean());
	}

	@Override
	public void reset() {
//		this.button.setValue(option.getDefaultAsBoolean());
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

		public void setValue(boolean value) {
			this.value = value;
		}

		public boolean getValue() {
			return this.value;
		}
	}
}