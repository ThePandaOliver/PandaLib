package me.pandamods.test.client.screen;

import me.pandamods.pandalib.client.screen.api.PLScreen;
import me.pandamods.pandalib.client.screen.api.config.ConfigMenu;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class TestClientConfigScreen extends ConfigMenu {
	private final Screen parent;

	public TestClientConfigScreen(Screen parent) {
		super(Component.empty());
		this.parent = parent;
	}

	@Override
	public void onClose() {
		this.minecraft.setScreen(parent);
	}
}
