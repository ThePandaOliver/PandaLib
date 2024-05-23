package me.pandamods.pandalib.api.client.screen;

import me.pandamods.pandalib.api.utils.screen.PLGuiGraphics;
import net.minecraft.client.gui.GuiGraphics;

public interface PLRenderable {
	void render(PLGuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick);
}
