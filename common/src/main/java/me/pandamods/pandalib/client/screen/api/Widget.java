package me.pandamods.pandalib.client.screen.api;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;

import java.util.ArrayList;
import java.util.List;

public abstract class Widget implements Renderable, UIElement, GuiEventListener, NarratableEntry {
	public final PLScreen screen;
	private final Widget parent;
	public final Minecraft minecraft;

	protected List<Renderable> renderables = new ArrayList<>();
	protected List<Widget> widgets = new ArrayList<>();

	public Widget(Widget parent) {
		this(parent.screen, parent);
	}

	public Widget(PLScreen screen) {
		this(screen, null);
	}

	public Widget(PLScreen screen, Widget parent) {
		this.screen = screen;
		this.parent = parent;
		this.minecraft = Minecraft.getInstance();
	}

	protected void init() {
		widgets.forEach(Widget::init);
	}

	@Override
	public final void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
		renderWidget(guiGraphics, partialTick);
	}

	protected void renderWidget(GuiGraphics guiGraphics, float partialTick) {
		renderables.forEach(renderable -> renderable.render(guiGraphics, (int) getMouseX(), (int) getMouseY(), partialTick));
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

	public double getMouseX() {
		if (parent() != null)
			return parent().getMouseX();
		return minecraft.mouseHandler.xpos();
	}

	public double getMouseY() {
		if (parent() != null)
			return parent().getMouseY();
		return minecraft.mouseHandler.ypos();
	}
}
