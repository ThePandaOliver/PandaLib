package me.pandamods.pandalib.utils;

import com.mojang.math.Divisor;
import it.unimi.dsi.fastutil.ints.IntIterator;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class ScreenUtils {
	public static boolean isMouseOver(double mouseX, double mouseY, int minX, int minY, int maxX, int maxY) {
		return minX <= mouseX && maxX >= mouseX && minY <= mouseY && maxY >= mouseY;
	}

	public static void blitNineSliced(GuiGraphics guiGraphics, ResourceLocation resourceLocation,
									  int x, int y, int width, int height,
									  int textureX, int textureY, int textureWidth, int textureHeight,
									  int topSlice, int bottomSlice, int leftSlice, int rightSlice) {
		int x1 = x;
		int x2 = x + leftSlice;
		int x3 = x + width - rightSlice;

		int y1 = y;
		int y2 = y + topSlice;
		int y3 = y + height - bottomSlice;

		int width1 = leftSlice;
		int width2 = width - leftSlice - rightSlice;
		int width3 = rightSlice;

		int height1 = topSlice;
		int height2 = height - topSlice - bottomSlice;
		int height3 = bottomSlice;

		int textureX1 = textureX;
		int textureX2 = textureX + leftSlice;
		int textureX3 = textureX + textureWidth - rightSlice;

		int textureY1 = textureY;
		int textureY2 = textureY + topSlice;
		int textureY3 = textureY + textureHeight - bottomSlice;

		int textureWidth1 = leftSlice;
		int textureWidth2 = textureWidth - leftSlice - rightSlice;
		int textureWidth3 = rightSlice;

		int textureHeight1 = topSlice;
		int textureHeight2 = textureHeight - topSlice - bottomSlice;
		int textureHeight3 = bottomSlice;

		guiGraphics.blit(resourceLocation,
				x1, y1, textureX1, textureY1,
				width1, height1, textureWidth, textureHeight);
		blitRepeating(guiGraphics, resourceLocation,
				x2, y1, width2, height1,
				textureX2, textureY1, textureWidth2, textureHeight1, textureWidth, textureHeight);
		guiGraphics.blit(resourceLocation,
				x3, y1, textureX3, textureY1,
				width3, height1, textureWidth, textureHeight);

		blitRepeating(guiGraphics, resourceLocation,
				x1, y2, width1, height2,
				textureX1, textureY2, textureWidth1, textureHeight2, textureWidth, textureHeight);
		blitRepeating(guiGraphics, resourceLocation,
				x2, y2, width2, height2,
				textureX2, textureY2, textureWidth2, textureHeight2, textureWidth, textureHeight);
		blitRepeating(guiGraphics, resourceLocation,
				x3, y2, width3, height2,
				textureX3, textureY2, textureWidth3, textureHeight2, textureWidth, textureHeight);

		guiGraphics.blit(resourceLocation,
				x1, y3, textureX1, textureY3,
				width1, height3, textureWidth, textureHeight);
		blitRepeating(guiGraphics, resourceLocation,
				x2, y3, width2, height3,
				textureX2, textureY3, textureWidth2, textureHeight3, textureWidth, textureHeight);
		guiGraphics.blit(resourceLocation,
				x3, y3, textureX3, textureY3,
				width3, height3, textureWidth, textureHeight);
	}

	public static void blitRepeating(GuiGraphics guiGraphics, ResourceLocation atlasLocation, int x, int y, int width, int height,
									 int uOffset, int vOffset, int sourceWidth, int sourceHeight, int textureWidth, int textureHeight) {
		int i = x;

		int j;
		for(IntIterator intIterator = slices(width, sourceWidth); intIterator.hasNext(); i += j) {
			j = intIterator.nextInt();
			int k = (sourceWidth - j) / 2;
			int l = y;

			int m;
			for(IntIterator intIterator2 = slices(height, sourceHeight); intIterator2.hasNext(); l += m) {
				m = intIterator2.nextInt();
				int n = (sourceHeight - m) / 2;
				guiGraphics.blit(atlasLocation, i, l, uOffset + k, vOffset + n, j, m, textureWidth, textureHeight);
			}
		}
	}

	private static IntIterator slices(int target, int total) {
		int i = Mth.positiveCeilDiv(target, total);
		return new Divisor(target, i);
	}
}
