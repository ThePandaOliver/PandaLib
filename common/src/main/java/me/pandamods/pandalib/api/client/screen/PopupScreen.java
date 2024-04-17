package me.pandamods.pandalib.api.client.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class PopupScreen extends PLScreen {
	private final Screen parent;

	protected PopupScreen(Screen parent, Component title) {
		super(title);
		this.parent = parent;
	}

	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
		this.parent.render(guiGraphics, 0, 0, partialTick);
		super.render(guiGraphics, mouseX, mouseY, partialTick);
	}
}
