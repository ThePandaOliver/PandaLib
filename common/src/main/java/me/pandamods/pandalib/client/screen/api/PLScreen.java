package me.pandamods.pandalib.client.screen.api;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

public abstract class PLScreen extends Screen implements WidgetHolder {
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

	@Override
	public List<Widget> widgets() {
		return widgets;
	}

	@Override
	public void mouseMoved(double mouseX, double mouseY) {
		this.onMouseMove(mouseX, mouseY);
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		if (super.mouseClicked(mouseX, mouseY, button)) return true;
		return onMouseClick(mouseX, mouseY, button);
	}

	@Override
	public boolean mouseReleased(double mouseX, double mouseY, int button) {
		if (super.mouseReleased(mouseX, mouseY, button)) return true;
		return onMouseRelease(mouseX, mouseY, button);
	}

	@Override
	public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
		if (super.mouseDragged(mouseX, mouseY, button, dragX, dragY)) return true;
		return onMouseDrag(mouseX, mouseY, button, dragX, dragY);
	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
		if (super.mouseScrolled(mouseX, mouseY, delta)) return true;
		return onMouseScroll(mouseX, mouseY, delta);
	}

	@Override
	public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
		if (super.keyReleased(keyCode, scanCode, modifiers)) return true;
		return onMouseRelease(keyCode, scanCode, modifiers);
	}

	@Override
	public boolean charTyped(char codePoint, int modifiers) {
		if (super.charTyped(codePoint, modifiers)) return true;
		return onCharType(codePoint, modifiers);
	}
}
