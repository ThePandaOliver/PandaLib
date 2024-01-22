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
}
