package me.pandamods.pandalib.client.screen.api.widgets.buttons;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

public class AbstractToggleButton extends PLAbstractButton {
	public boolean state;

	public AbstractToggleButton(int x, int y, int width, int height, boolean state, Component component) {
		super(x, y, width, height, component);
		this.state = state;
	}

	@Override
	public void onPress() {
		this.state = !this.state;
	}

	@Override
	protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
		narrationElementOutput.add(NarratedElementType.TITLE, this.getMessage());
		narrationElementOutput.add(NarratedElementType.TITLE, state ? CommonComponents.OPTION_ON : CommonComponents.OPTION_OFF);
	}

	@Override
	public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
		super.renderWidget(guiGraphics, mouseX, mouseY, partialTick);
	}

	@Override
	protected void renderScrollingString(GuiGraphics guiGraphics, Font font, int width, int color) {
		Component message = this.getMessage().copy().append(": ").append(state ? CommonComponents.OPTION_ON : CommonComponents.OPTION_OFF);
		renderScrollingString(guiGraphics, font, message, this.minX() + width, this.minY(), this.maxX() - width, this.maxY(), color);
	}

	public enum Type {
		BUTTON,
		SWITCH,
		CHECK
	}
}
