package me.pandamods.pandalib.client.screen.api;

import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;

public abstract class InteractableWidget extends Widget implements GuiEventListener, NarratableEntry {
	public InteractableWidget(PLScreen screen) {
		super(screen);
	}
}
