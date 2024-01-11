package me.pandamods.pandalib.client.screen.widgets;

import me.pandamods.pandalib.utils.animation.interpolation.FloatInterpolator;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import org.joml.Math;

import java.awt.*;

public abstract class ScrollableWidget extends Widget {
	private final FloatInterpolator scrollXInterpolator = new FloatInterpolator(0f);
	private final FloatInterpolator scrollYInterpolator = new FloatInterpolator(0f);
	private double scrollDistanceX = 0;
	private double scrollDistanceY = 0;

	public ScrollableWidget(WidgetImpl parent) {
		super(parent);
	}

	public abstract int getMaxScrollDistanceX();
	public abstract int getMaxScrollDistanceY();

	public boolean shouldShowScrollBarX() {
		return this.getMaxScrollDistanceX() > this.getWidth();
	}
	public boolean shouldShowScrollBarY() {
		return this.getMaxScrollDistanceY() > this.getHeight();
	}

	public double getScrollDistanceXDouble() {
		return this.scrollXInterpolator.getAsDouble();
	}
	public float getScrollDistanceXFloat() {
		return this.scrollXInterpolator.getAsFloat();
	}
	public int getScrollDistanceXInt() {
		return this.scrollXInterpolator.getAsInt();
	}
	public double getScrollDistanceYDouble() {
		return this.scrollYInterpolator.getAsDouble();
	}
	public float getScrollDistanceYFloat() {
		return this.scrollYInterpolator.getAsFloat();
	}
	public int getScrollDistanceYInt() {
		return this.scrollYInterpolator.getAsInt();
	}

	public int getScrollBarLengthY() {
		int height = (int) ((double) this.getHeight() / ((double) this.getMaxScrollDistanceY() / (double) this.getHeight()));
		return Math.clamp(20, this.getHeight(), height);
	}

	public double getScrollBarWidth() {
		return 3;
	}

	public int getScrollBarLengthX() {
		int width = (int) ((double) this.getWidth() / ((double) this.getMaxScrollDistanceX() / (double) this.getWidth()));
		return Math.clamp(20, this.getWidth(), width);

	}

	@Override
	public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
		scrollXInterpolator.update();
		scrollYInterpolator.update();

		super.renderWidget(guiGraphics, mouseX, mouseY, partialTick);
		if (shouldShowScrollBarX())
			renderScrollBarX(guiGraphics, (int) (this.getMaxY() - getScrollBarWidth()), this.getMaxY());
		if (shouldShowScrollBarY())
			renderScrollBarY(guiGraphics, (int) (this.getMaxX() - getScrollBarWidth()), this.getMaxX());
	}

	private void renderScrollBarX(GuiGraphics guiGraphics, int minY, int maxY) {
		double x = this.getX() + (this.getWidth() - this.getScrollBarLengthX()) *
				(this.getScrollDistanceXDouble() / (this.getMaxScrollDistanceX() - this.getWidth()));
		int minX = (int) x;
		int maxX = (int) x + this.getScrollBarLengthX();

		renderScrollBar(guiGraphics, minX, minY, maxX, maxY);
	}

	private void renderScrollBarY(GuiGraphics guiGraphics, int minX, int maxX) {
		double y = this.getY() + (this.getHeight() - this.getScrollBarLengthY()) *
				(this.getScrollDistanceYDouble() / (this.getMaxScrollDistanceY() - this.getHeight()));
		int minY = (int) y;
		int maxY = (int) y + this.getScrollBarLengthY();

		renderScrollBar(guiGraphics, minX, minY, maxX, maxY);
	}

	private void renderScrollBar(GuiGraphics guiGraphics, int minX, int minY, int maxX, int maxY) {
		int shadingOffset = 75;
		int lightness = 150;
		Color color = new Color(lightness, lightness, lightness);
		Color colorDark = new Color(lightness - shadingOffset, lightness - shadingOffset, lightness - shadingOffset);
		Color colorLight = new Color(lightness + shadingOffset, lightness + shadingOffset, lightness + shadingOffset);

		guiGraphics.fill(minX + 1, minY + 1, maxX, maxY, 10, colorDark.getRGB());
		guiGraphics.fill(minX, minY, maxX - 1, maxY - 1, 10, colorLight.getRGB());
		guiGraphics.fill(minX + 1, minY + 1, maxX - 1, maxY - 1, 10, color.getRGB());
	}

	@Override
	public int getChildY() {
		return -this.getScrollDistanceYInt();
	}

	@Override
	public int getChildX() {
		return -this.getScrollDistanceXInt();
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		if (button == 1) {
			this.setDragged(true);
		}
		return super.mouseClicked(mouseX, mouseY, button);
	}

	@Override
	public boolean mouseReleased(double mouseX, double mouseY, int button) {
		if (this.isDragged()) {
			this.setDragged(false);
		}
		return super.mouseReleased(mouseX, mouseY, button);
	}

	@Override
	public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
		if (super.mouseDragged(mouseX, mouseY, button, dragX, dragY))
			return true;

		if (this.isDragged()) {
			scrollDistanceX -= dragX;
			scrollDistanceY -= dragY;
			updateScroll();
			this.scrollXInterpolator.setTime(this.scrollXInterpolator.getDuration());
			this.scrollYInterpolator.setTime(this.scrollXInterpolator.getDuration());
			return true;
		}
		return false;
	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
		if (super.mouseScrolled(mouseX, mouseY, delta))
			return true;

		if (this.isHovered()) {
			if (Screen.hasShiftDown()) {
				scrollDistanceX -= delta * 20;
			} else {
				scrollDistanceY -= delta * 20;
			}
			updateScroll();
			return true;

		}
		return false;
	}

	protected void updateScroll() {
		scrollDistanceX = Math.clamp(0, this.getMaxScrollDistanceX() - this.getWidth(), scrollDistanceX);
		scrollDistanceY = Math.clamp(0, this.getMaxScrollDistanceY() - this.getHeight(), scrollDistanceY);
		scrollXInterpolator.setTarget((float) scrollDistanceX);
		scrollYInterpolator.setTarget((float) scrollDistanceY);
	}
}