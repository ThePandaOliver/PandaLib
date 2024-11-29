package me.pandamods.pandalib.client.screen;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public abstract class BasePLScreen extends Screen {
	protected BasePLScreen(Component title) {
		super(title);
	}

	protected BasePLScreen() {
		this(Component.empty());
	}

	protected abstract void build(RootComponent rootComponent);

	@Override
	protected void init() {
	}
}
