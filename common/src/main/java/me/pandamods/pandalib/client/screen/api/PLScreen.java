package me.pandamods.pandalib.client.screen.api;

import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

public abstract class PLScreen extends Screen implements WidgetHolder {
	private List<WidgetImpl> widgetImpls = new ArrayList<>();

	protected PLScreen(Component title) {
		super(title);
	}

	@Override
	protected void init() {
		widgetImpls.forEach(WidgetImpl::init);
	}

	@Override
	protected void clearWidgets() {
		widgetImpls.forEach(WidgetImpl::clearWidgets);
		widgetImpls.clear();
		super.clearWidgets();
	}

	@Override
	public void rebuildWidgets() {
		super.rebuildWidgets();
	}

	@Override
	protected <T extends GuiEventListener & NarratableEntry> T addWidget(T listener) {
		if (listener instanceof WidgetImpl widget) widget.setScreen(this);
		return super.addWidget(listener);
	}

	protected <T extends WidgetImpl> T addWidgetPanel(T widget) {
		addRenderableOnly(widget);
		widgetImpls.add(widget);
		widget.setScreen(this);
		return widget;
	}

	@Override
	public List<WidgetImpl> widgets() {
		return widgetImpls;
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
