package me.pandamods.pandalib.api.client.screen.elements.widgets.buttons;

import net.minecraft.network.chat.Component;

public class PLButton extends PLAbstractButton {
	private Component text;

	public PLButton(Component text) {
		this.text = text;
	}

	@Override
	public Component getText() {
		return this.text;
	}

	public void setText(Component text) {
		this.text = text;
	}
}
