package me.pandamods.pandalib.client.screen.api;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

public abstract class PLScreen extends Screen {
	private List<Widget> widgets = new ArrayList<>();

	protected PLScreen(Component title) {
		super(title);
	}

	@Override
	protected void init() {
		widgets.forEach(Widget::init);
	}

	@Override
	protected void clearWidgets() {
		widgets.forEach(Widget::clearWidgets);
		widgets.clear();
		super.clearWidgets();
	}

	@Override
	public void rebuildWidgets() {
		super.rebuildWidgets();
	}

	@Override
	protected <T extends GuiEventListener & NarratableEntry> T addWidget(T listener) {
		return super.addWidget(listener);
	}

	protected <T extends Widget> T addWidgetPanel(T widget) {
		addRenderableOnly(widget);
		widgets.add(widget);
		return widget;
	}
}
