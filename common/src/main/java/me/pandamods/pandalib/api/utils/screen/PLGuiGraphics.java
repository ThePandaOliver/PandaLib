package me.pandamods.pandalib.api.utils.screen;

import net.minecraft.client.gui.GuiGraphics;
import org.joml.Matrix4f;

import java.awt.*;

/**
 * PLGuiGraphics class extends GuiGraphics class and provides additional and improved functionality for rendering.
 */
public class PLGuiGraphics extends GuiGraphics {
	private static final int SEPARATOR_LINE_DARK_COLOR = new Color(0, 0, 0, 150).getRGB();
	private static final int SEPARATOR_LINE_LIGHT_COLOR = new Color(100, 100, 100, 150).getRGB();

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
}
