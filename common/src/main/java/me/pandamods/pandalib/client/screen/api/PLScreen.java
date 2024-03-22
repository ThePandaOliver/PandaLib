package me.pandamods.pandalib.client.screen.api;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public abstract class PLScreen extends Screen implements Element {
	protected PLScreen(Component title) {
		super(title);
	}
}
