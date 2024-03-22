package me.pandamods.pandalib.client.screen.api.widgets.buttons;

import me.pandamods.pandalib.client.screen.api.*;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.network.chat.Component;

public abstract class PLAbstractButton extends AbstractButton implements Element {
	private Element parent;

	public PLAbstractButton(int x, int y, int width, int height, Component component) {
		super(x, y, width, height, component);
	}

	@Override
	public Element getParent() {
		return parent;
	}

	@Override
	public void setParent(Element parent) {
		this.parent = parent;
	}
}
