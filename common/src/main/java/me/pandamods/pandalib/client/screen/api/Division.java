package me.pandamods.pandalib.client.screen.api;

import dev.architectury.hooks.client.screen.ScreenHooks;
import me.pandamods.pandalib.client.screen.api.widgets.AbstractElement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;

import java.util.HashSet;
import java.util.Set;

public abstract class Division extends AbstractElement {
	private final Set<Division> divisions = new HashSet<>();
	private final Set<Renderable> renderables = new HashSet<>();

	protected void init() {
		divisions.forEach(Division::init);
	}

	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
		renderables.forEach(renderable -> renderable.render(guiGraphics, mouseX, mouseY, partialTick));
		super.render(guiGraphics, mouseX, mouseY, partialTick);
	}

	protected <T extends Element & GuiEventListener & NarratableEntry & Renderable> T addElement(T element) {
		if (element instanceof Division division)
			divisions.add(division);
		ScreenHooks.addWidget(screen(), element);
		renderables.add(element);
		element.setParent(this);
		return element;
	}
}
