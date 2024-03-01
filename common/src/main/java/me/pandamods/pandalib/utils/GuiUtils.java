package me.pandamods.pandalib.utils;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;

import java.awt.*;

public class GuiUtils {
	public static void drawColor(GuiGraphics guiGraphics, double x, double y, double width, double height, Color color) {
		drawColor(guiGraphics, x, y, width, height, 0, color);
	}

	public static void drawColor(GuiGraphics guiGraphics, double x, double y, double width, double height, int z, Color color) {
		PoseStack poseStack = guiGraphics.pose();
		poseStack.pushPose();
		poseStack.translate(x, y, 0);
		poseStack.scale((float) width, (float) height, 1);
		guiGraphics.fill(0, 0, 1, 1, z, color.getRGB());
		poseStack.popPose();
	}
}
