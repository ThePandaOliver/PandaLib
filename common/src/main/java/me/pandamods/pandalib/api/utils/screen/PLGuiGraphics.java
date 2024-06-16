package me.pandamods.pandalib.api.utils.screen;

import net.minecraft.Util;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import org.joml.Math;
import org.joml.Matrix4f;

import java.awt.*;

/**
 * PLGuiGraphics class extends GuiGraphics class and provides additional and improved functionality for rendering.
 */
public class PLGuiGraphics extends GuiGraphics {
	private static final int SEPARATOR_LINE_DARK_COLOR = new Color(0, 0, 0, 125).getRGB();
	private static final int SEPARATOR_LINE_LIGHT_COLOR = new Color(100, 100, 100, 125).getRGB();

	public PLGuiGraphics(GuiGraphics guiGraphics) {
		super(guiGraphics.minecraft, guiGraphics.pose, guiGraphics.bufferSource);
		this.scissorStack = guiGraphics.scissorStack;
	}

	@Override
	public void enableScissor(int minX, int minY, int maxX, int maxY) {
		Matrix4f matrix = this.pose().last().pose();
		minX += (int) matrix.m30();
		minY += (int) matrix.m31();
		maxX += (int) matrix.m30();
		maxY += (int) matrix.m31();
		super.enableScissor(minX, minY, maxX, maxY);
	}

	public void renderSeparatorLine(int x, int y, int length, boolean isVertical) {
		if (isVertical) renderVerticalSeparatorLine(x, y, length);
		else renderHorizontalSeparatorLine(x, y, length);
	}

	private void renderVerticalSeparatorLine(int x, int y, int length) {
		this.fill(x - 1, y, x, y + length, SEPARATOR_LINE_DARK_COLOR);
		this.fill(x, y, x + 1, y + length, SEPARATOR_LINE_LIGHT_COLOR);
	}

	private void renderHorizontalSeparatorLine(int x, int y, int length) {
		this.fill(x, y - 1, x + length, y, SEPARATOR_LINE_DARK_COLOR);
		this.fill(x, y, x + length, y + 1, SEPARATOR_LINE_LIGHT_COLOR);
	}

	public void drawScrollingString(Font font, Component text, int minX, int minY, int maxX, int maxY, int color) {
		int textWidth = font.width(text);
		int y = (minY + maxY - 9) / 2 + 1;
		int width = maxX - minX;
		if (textWidth > width) {
			int scrollMaxAmount = textWidth - width;
			double time = Util.getMillis() / 1000.0;
			double lerpTime = (Math.sin(time / 5) + 1) / 2;
			double x = Math.lerp(0.0, scrollMaxAmount, lerpTime);
			this.enableScissor(minX, minY, maxX, maxY);
			this.drawString(font, text, minX - (int) x, y, color);
			this.disableScissor();
		} else {
			this.drawCenteredString(font, text, (minX + maxX) / 2, y, color);
		}
	}
}
