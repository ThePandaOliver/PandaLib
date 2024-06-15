package me.pandamods.pandalib.api.client.screen.popup;

import me.pandamods.pandalib.api.client.screen.PLScreen;
import me.pandamods.pandalib.api.utils.screen.PLGuiGraphics;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public abstract class PopupScreen extends PLScreen {
	private final Screen parentScreen;

	public PopupScreen(Screen parentScreen) {
		this.parentScreen = parentScreen;
		this.parentScreen.init(getMinecraft(), getWidth(), getHeight());
	}

	public Screen getParentScreen() {
		return parentScreen;
	}

	@Override
	public void render(PLGuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
		parentScreen.render(guiGraphics, 0, 0, partialTick);
		super.render(guiGraphics, mouseX, mouseY, partialTick);
	}

	@Override
	public void resize(Minecraft minecraft, int width, int height) {
		parentScreen.resize(getMinecraft(), width, height);
		super.resize(minecraft, width, height);
	}

	@Override
	public void close() {
		this.getMinecraft().setScreen(this.parentScreen);
	}
}
