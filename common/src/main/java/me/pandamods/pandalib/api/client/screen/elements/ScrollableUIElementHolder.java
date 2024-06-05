package me.pandamods.pandalib.api.client.screen.elements;

import me.pandamods.pandalib.api.utils.screen.PLGuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import org.joml.Math;

import java.awt.*;

public abstract class ScrollableUIElementHolder extends UIElementHolder {
	protected int scrollHorizontalAmount = 0;
	protected int scrollVerticalAmount = 0;

	public abstract int scrollHorizontalLength();
	public abstract int scrollVerticalLength();

	public boolean shouldShowHorizontalScrollbar() {
		return scrollHorizontalLength() > this.getWidth();
	}

	public boolean shouldShowVerticalScrollbar() {
		return scrollVerticalLength() > this.getHeight();
	}

	public int getScrollHorizontalAmount() {
		return scrollHorizontalAmount;
	}

	public int getScrollVerticalAmount() {
		return scrollVerticalAmount;
	}

	@Override
	public int getChildOffsetX() {
		return -getScrollHorizontalAmount();
	}

	@Override
	public int getChildOffsetY() {
		return -getScrollVerticalAmount();
	}

	@Override
	public void render(PLGuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
		guiGraphics.enableScissor(minX(), minY(), maxX(), maxY());
		guiGraphics.pose().pushPose();
		guiGraphics.pose().translate(getChildOffsetX(), getChildOffsetY(), 0);
		renderElement(guiGraphics, mouseX, mouseY, partialTick);
		guiGraphics.pose().popPose();
		super.render(guiGraphics, mouseX, mouseY, partialTick);
		guiGraphics.disableScissor();

		if (shouldShowVerticalScrollbar())
			renderVerticalScrollbar(guiGraphics, mouseX, mouseY, partialTick);
	}

	protected void renderElement(PLGuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {}

	private static final int SCROLLBAR_BACKGROUND_COLOR = new Color(0, 0, 0, 150).getRGB();
	private static final int SCROLLBAR_COLOR_DARK = new Color(100, 100, 100, 150).getRGB();
	private static final int SCROLLBAR_COLOR = new Color(150, 150, 150, 150).getRGB();

	private void renderVerticalScrollbar(PLGuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
		guiGraphics.fill(maxX() - 4, minY(), maxX(), maxY(), SCROLLBAR_BACKGROUND_COLOR);

		int size = 100;
		float scrollPercentage = ((float) this.getScrollVerticalAmount()) / (this.scrollVerticalLength() - this.getHeight());
		int y = this.getY() + (int) ((this.getHeight() - size) * scrollPercentage);
		guiGraphics.fill(maxX() - 4, y, maxX(), y + size, SCROLLBAR_COLOR_DARK);
		guiGraphics.fill(maxX() - 4, y, maxX() - 1, y + size - 1, SCROLLBAR_COLOR);
	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
		if (super.mouseScrolled(mouseX, mouseY, delta)) return true;

		if (Screen.hasShiftDown())
			scrollHorizontalAmount -= (int) delta * 10;
		else
			scrollVerticalAmount -= (int) delta * 10;
		scrollHorizontalAmount = Math.clamp(0, scrollHorizontalLength() - this.getWidth(), scrollHorizontalAmount);
		scrollVerticalAmount = Math.clamp(0, scrollVerticalLength() - this.getHeight(), scrollVerticalAmount);
		return true;
	}
}
