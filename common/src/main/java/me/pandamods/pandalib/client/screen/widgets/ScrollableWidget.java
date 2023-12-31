package me.pandamods.pandalib.client.screen.widgets;

import me.pandamods.pandalib.client.screen.PandaLibScreen;
import me.pandamods.pandalib.utils.ScreenUtils;
import me.pandamods.pandalib.utils.animation.interpolation.FloatInterpolator;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import org.jetbrains.annotations.Nullable;
import org.joml.Math;

import java.awt.*;

public abstract class ScrollableWidget extends Widget {
	private final FloatInterpolator scrollInterpolator = new FloatInterpolator(.1f, 0f, 0f);
	private double scrollDistance = 0;
	private final FloatInterpolator scrollBarWidthInterpolator = new FloatInterpolator(.25f, 0f, 0f);
	private double scrollBarWidth = 0;

	protected ScrollableWidget(PandaLibScreen screen, Widget parent) {
		super(screen, parent);
	}

	@Override
	public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
		scrollDistance = Math.clamp(0, this.getMaxScrollDistance() - this.getHeight(), scrollDistance);
		scrollInterpolator.setTarget((float) scrollDistance);
		scrollInterpolator.update();

		super.renderWidget(guiGraphics, mouseX, mouseY, partialTick);
		renderScrollBar(guiGraphics, mouseX, mouseY);
	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
		if (super.mouseScrolled(mouseX, mouseY, delta))
			return true;

		if (this.isHovered()) {
			scrollDistance += -delta * 20;
			return true;
		}
		return false;
	}

	public boolean isMouseOverScrollBar(double mouseX, double mouseY) {
		return ScreenUtils.isMouseOver(mouseX, mouseY, (int) (getMaxX() - scrollBarWidth), getMinY(), getMaxX(), getMaxY());
	}

	public abstract int getMaxScrollDistance();

	public boolean shouldShowScrollBar() {
		return this.getMaxScrollDistance() > this.getHeight();
	}

	public double getScrollDistanceDouble() {
		return this.scrollInterpolator.getValue().doubleValue();
	}

	public float getScrollDistanceFloat() {
		return this.scrollInterpolator.getValue();
	}

	public int getScrollDistanceInt() {
		return this.scrollInterpolator.getValue().intValue();
	}

	public int getScrollBarHeight() {
		int height = (int) ((double) this.getHeight() / ((double) this.getMaxScrollDistance() / (double) this.getHeight()));
		return Math.clamp(20, this.getHeight(), height);
	}

	public double getScrollBarWidth() {
		return this.scrollBarWidthInterpolator.getValue();
	}

	private void renderScrollBar(GuiGraphics guiGraphics, int mouseX, int mouseY) {
		if (this.isMouseOverScrollBar(mouseX, mouseY)) scrollBarWidth = 8;
		else scrollBarWidth = 4;
		scrollBarWidthInterpolator.setTarget((float) scrollBarWidth);
		scrollBarWidthInterpolator.update();

		guiGraphics.fill((int) (this.getMaxX() - getScrollBarWidth()), this.getMinY(), this.getMaxX(), this.getMaxY(), Color.black.getRGB());

		renderScrollBarDrag(guiGraphics, (int) (this.getMaxX() - getScrollBarWidth()), this.getMaxX());
	}

	private void renderScrollBarDrag(GuiGraphics guiGraphics, int minX, int maxX) {
		double y = this.getY() + (this.getHeight() - this.getScrollBarHeight()) *
				(this.getScrollDistanceDouble() / (this.getMaxScrollDistance() - this.getHeight()));
		int minY = (int) y;
		int maxY = (int) y + this.getScrollBarHeight();

		int shadingOffset = 75;
		int lightness = 150;
		Color color = new Color(lightness, lightness, lightness);
		Color colorDark = new Color(lightness - shadingOffset, lightness - shadingOffset, lightness - shadingOffset);

		guiGraphics.fill(minX, minY, maxX, maxY, colorDark.getRGB());
		guiGraphics.fill(minX, minY, maxX - 1, maxY - 1, color.getRGB());
	}

	@Override
	public int getChildY() {
		return -this.getScrollDistanceInt();
	}
}