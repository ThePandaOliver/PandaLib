package me.pandamods.pandalib.utils;

public class ScreenUtils {
	public static boolean isMouseOver(double mouseX, double mouseY, int minX, int minY, int maxX, int maxY) {
		return minX <= mouseX && maxX >= mouseX && minY <= mouseY && maxY >= mouseY;
	}
}
