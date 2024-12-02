/*
 * Copyright (C) 2024 Oliver Froberg (The Panda Oliver)
 *
 * This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 * You should have received a copy of the GNU Lesser General Public License
 *  along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package me.pandamods.pandalib.client.screen.utils;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;

public class RenderContext {
	public final GuiGraphics guiGraphics;

	public RenderContext(GuiGraphics guiGraphics) {
		this.guiGraphics = guiGraphics;
	}

	public void scissor(int minX, int minY, int maxX, int maxY, Runnable render) {
		guiGraphics.enableScissor(minX, minY, maxX, maxY);
		render.run();
		guiGraphics.disableScissor();
	}

	public PoseStack getPoseStack() {
		return guiGraphics.pose();
	}

	public double getDeltaSeconds() {
		return 1f / Minecraft.getInstance().getFps();
	}
}
