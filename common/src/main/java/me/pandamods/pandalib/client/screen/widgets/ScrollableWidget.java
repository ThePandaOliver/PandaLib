package me.pandamods.pandalib.client.screen.widgets;

import me.pandamods.pandalib.client.screen.PandaLibScreen;
import me.pandamods.pandalib.utils.animation.interpolation.FloatInterpolator;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.entity.Display;
import org.joml.Math;
import org.lwjgl.glfw.GLFW;

import java.awt.*;
import java.awt.event.MouseEvent;

public abstract class ScrollableWidget extends Widget {
	private final FloatInterpolator scrollXInterpolator = new FloatInterpolator(.1f, 0f, 0f);
	private final FloatInterpolator scrollYInterpolator = new FloatInterpolator(.1f, 0f, 0f);
	private double scrollDistanceX = 0;
	private double scrollDistanceY = 0;

	protected ScrollableWidget(PandaLibScreen screen, Widget parent) {
		super(screen, parent);
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
		return this.scrollXInterpolator.getValue().doubleValue();
	}

	public float getScrollDistanceXFloat() {
		return this.scrollXInterpolator.getValue();
	}

	public int getScrollDistanceXInt() {
		return this.scrollXInterpolator.getValue().intValue();
	}

	public double getScrollDistanceYDouble() {
		return this.scrollYInterpolator.getValue().doubleValue();
	}

	public float getScrollDistanceYFloat() {
		return this.scrollYInterpolator.getValue();
	}

	public int getScrollDistanceYInt() {
		return this.scrollYInterpolator.getValue().intValue();
	}

	public int getScrollBarHeightY() {
		int height = (int) ((double) this.getHeight() / ((double) this.getMaxScrollDistanceY() / (double) this.getHeight()));
		return Math.clamp(20, this.getHeight(), height);
	}

	public double getScrollBarWidthY() {
		return 3;
	}

	public double getScrollBarHeightX() {
		return 3;
	}

	public int getScrollBarWidthX() {
		int width = (int) ((double) this.getWidth() / ((double) this.getMaxScrollDistanceX() / (double) this.getWidth()));
		return Math.clamp(20, this.getWidth(), width);

	}

	@Override
	public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
		scrollXInterpolator.update();
		scrollYInterpolator.update();

		super.renderWidget(guiGraphics, mouseX, mouseY, partialTick);
		if (shouldShowScrollBarX())
			renderScrollBarX(guiGraphics, (int) (this.getMaxY() - getScrollBarHeightX()), this.getMaxY());
		if (shouldShowScrollBarY())
			renderScrollBarY(guiGraphics, (int) (this.getMaxX() - getScrollBarWidthY()), this.getMaxX());
	}

	private void renderScrollBarX(GuiGraphics guiGraphics, int minY, int maxY) {
		double x = this.getX() + (this.getWidth() - this.getScrollBarWidthX()) *
				(this.getScrollDistanceXDouble() / (this.getMaxScrollDistanceX() - this.getWidth()));
		int minX = (int) x;
		int maxX = (int) x + this.getScrollBarWidthX();

		renderScrollBar(guiGraphics, minX, minY, maxX, maxY);
	}

	private void renderScrollBarY(GuiGraphics guiGraphics, int minX, int maxX) {
		double y = this.getY() + (this.getHeight() - this.getScrollBarHeightY()) *
				(this.getScrollDistanceYDouble() / (this.getMaxScrollDistanceY() - this.getHeight()));
		int minY = (int) y;
		int maxY = (int) y + this.getScrollBarHeightY();

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
			this.scrollXInterpolator.setBounds((float) scrollDistanceX, (float) scrollDistanceX);
			this.scrollYInterpolator.setBounds((float) scrollDistanceY, (float) scrollDistanceY);
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