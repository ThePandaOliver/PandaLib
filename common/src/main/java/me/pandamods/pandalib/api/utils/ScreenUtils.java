package me.pandamods.pandalib.api.utils;

import com.mojang.blaze3d.Blaze3D;
import net.minecraft.Util;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

import java.awt.*;

public class ScreenUtils {
	public static void renderScrollingString(GuiGraphics guiGraphics, Font font, Component text, int minX, int minY, int maxX, int maxY, int color) {
		int i = font.width(text);
		int j = (minY + maxY - 9) / 2 + 1;
		int k = maxX - minX;
		if (i > k) {
			int l = i - k;
			double d0 = (double) Util.getMillis() / 1000.0;
			double d1 = Math.max((double)l * 0.5, 3.0);
			double d2 = Math.sin(1.5707963267948966 * Math.cos(6.283185307179586 * d0 / d1)) / 2.0 + 0.5;
			double d3 = Mth.lerp(d2, 0.0, l);
			guiGraphics.enableScissor(minX, minY, maxX, maxY);
			guiGraphics.drawString(font, text, minX - (int)d3, j, color);
			guiGraphics.disableScissor();
		} else {
			guiGraphics.drawCenteredString(font, text, (minX + maxX) / 2, j, color);
		}
	}

	public static boolean isMouseOver(double mouseX, double mouseY, int minX, int minY, int maxX, int maxY) {
		return mouseX >= minX && mouseX <= maxX && mouseY >= minY && mouseY <= maxY;
	}
}
