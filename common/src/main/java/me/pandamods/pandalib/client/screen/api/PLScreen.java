package me.pandamods.pandalib.client.screen.api;

import dev.architectury.hooks.client.screen.ScreenHooks;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.HashSet;
import java.util.Set;

public abstract class PLScreen extends Screen implements Element {
	private final Set<Division> divisions = new HashSet<>();

	protected PLScreen(Component title) {
		super(title);
	}

	@Override
	public Element getParent() {
		return null;
	}
	@Override
	public void setParent(Element element) {}

	@Override
	protected void init() {
		divisions.forEach(Division::init);
	}

	protected <T extends Element & GuiEventListener & NarratableEntry & Renderable> T addElement(T element) {
		if (element instanceof Division division)
			divisions.add(division);
		addRenderableWidget(element);
		element.setParent(this);
		return element;
	}

	@Override
	public int getX() {
		return 0;
	}

	@Override
	public int getY() {
		return 0;
	}

	@Override
	public int getWidth() {
		return this.width;
	}

	@Override
	public int getHeight() {
		return this.height;
	}
}
