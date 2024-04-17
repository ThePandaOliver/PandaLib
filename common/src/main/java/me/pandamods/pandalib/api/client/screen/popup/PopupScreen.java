package me.pandamods.pandalib.api.client.screen.popup;

import me.pandamods.pandalib.api.client.screen.PLScreen;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.awt.*;

public class PopupScreen<T extends Screen> extends PLScreen {
	private static final Color BACKGROUND_COLOR = new Color(0, 0, 0, 125);

	public final T parent;

	protected PopupScreen(T parent, Component title) {
		super(title);
		this.parent = parent;
	}

	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
		guiGraphics.pose().pushPose();
		guiGraphics.pose().translate(0, 0, -50);
		this.parent.render(guiGraphics, 0, 0, partialTick);
		guiGraphics.pose().popPose();
		guiGraphics.fill(0, 0, this.width, this.height, BACKGROUND_COLOR.getRGB());

		super.render(guiGraphics, mouseX, mouseY, partialTick);
	}

	@Override
	public void onClose() {
		this.minecraft.setScreen(parent);
	}
}
