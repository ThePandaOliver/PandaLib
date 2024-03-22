package me.pandamods.pandalib.client.screen.api;

import dev.architectury.hooks.client.screen.ScreenHooks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;

import java.util.HashSet;
import java.util.Set;

public abstract class Division implements Element, GuiEventListener, NarratableEntry, Renderable {
	private final Set<Division> divisions = new HashSet<>();
	private final Set<Renderable> renderables = new HashSet<>();
	private Element parent;
	private boolean hovered;
	private boolean focused;

	protected void init() {
		divisions.forEach(Division::init);
	}

	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
		renderables.forEach(renderable -> renderable.render(guiGraphics, mouseX, mouseY, partialTick));
	}

	@Override
	public Element getParent() {
		return this.parent;
	}

	@Override
	public void setParent(Element element) {
		this.parent = element;
	}

	protected <T extends Element & GuiEventListener & NarratableEntry & Renderable> T addElement(T element) {
		if (element instanceof Division division)
			divisions.add(division);
		ScreenHooks.addWidget(screen(), element);
		renderables.add(element);
		element.setParent(this);
		return element;
	}

	@Override
	public void setFocused(boolean focused) {
		this.focused = focused;
	}

	@Override
	public boolean isFocused() {
		return this.focused;
	}

	public boolean isHovered() {
		return hovered;
	}

	@Override
	public NarrationPriority narrationPriority() {
		return NarrationPriority.NONE;
	}
}
