package me.pandamods.pandalib.client.screen.widgets.tab;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class AbstractTab extends AbstractButton {
	public static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation("textures/gui/tab_button.png");
	private AbstractTabWidget<?> tabWidget;

	public AbstractTab() {
		super(0, 0, 0, 0, Component.empty());
	}

	public void prepare(AbstractTabWidget<?> tabWidget, int x, int y, int width, int height) {
		this.tabWidget = tabWidget;
		setX(x);
		setY(y);
		this.width = width;
		this.height = height;
	}

	@Override
	protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {}

	@Override
	public void onPress() {
		tabWidget.onClick(this);
	}

	public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
		guiGraphics.blitNineSliced(TEXTURE_LOCATION, this.getX(), this.getY(), this.width, this.height,
				2, 2, 2, 0, 130, 24, 0, getTextureY());
		Font font = Minecraft.getInstance().font;
		int i = this.active ? -1 : -6250336;
		this.renderString(guiGraphics, font, i);
		if (this.isSelected()) {
			this.renderFocusUnderline(guiGraphics, font, i);
		}
	}

	public void renderString(GuiGraphics guiGraphics, Font font, int color) {
		int i = this.getX() + 1;
		int j = this.getY() + (this.isSelected() ? 0 : 3);
		int k = this.getX() + this.getWidth() - 1;
		int l = this.getY() + this.getHeight();
		renderScrollingString(guiGraphics, font, this.getMessage(), i, j, k, l, color);
	}

	private void renderFocusUnderline(GuiGraphics guiGraphics, Font font, int color) {
		int i = Math.min(font.width(this.getMessage()), this.getWidth() - 4);
		int j = this.getX() + (this.getWidth() - i) / 2;
		int k = this.getY() + this.getHeight() - 2;
		guiGraphics.fill(j, k, j + i, k + 1, color);
	}

	protected int getTextureY() {
		int i = 2;
		if (this.isSelected() && this.isHoveredOrFocused()) {
			i = 1;
		} else if (this.isSelected()) {
			i = 0;
		} else if (this.isHoveredOrFocused()) {
			i = 3;
		}

		return i * 24;
	}

	public boolean isSelected() {
		return false;
	}
}