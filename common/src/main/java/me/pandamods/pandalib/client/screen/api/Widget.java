package me.pandamods.pandalib.client.screen.api;

import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;

import java.util.ArrayList;
import java.util.List;

public abstract class Widget implements Renderable {
	private final PLScreen screen;

	protected List<Renderable> renderables = new ArrayList<>();
	protected List<Widget> widgets = new ArrayList<>();

	public Widget(PLScreen screen) {
		this.screen = screen;
	}

	protected void init() {
		widgets.forEach(Widget::init);
	}

	protected <T extends Renderable> T addRenderableOnly(T renderable) {
		renderables.add(renderable);
		return renderable;
	}

	protected <T extends GuiEventListener & Renderable & NarratableEntry> T addRenderableWidget(T widget) {
		renderables.add(widget);
		addWidget(widget);
		return widget;
	}

	protected <T extends GuiEventListener & NarratableEntry> T addWidget(T listener) {
		return screen.addWidget(listener);
	}

	protected <T extends Widget> T addWidgetPanel(T widget) {
		widgets.add(widget);
		renderables.add(widget);
		return widget;
	}

	void clearWidgets() {
		widgets.forEach(Widget::clearWidgets);
		renderables.clear();
		widgets.clear();
	}
}
