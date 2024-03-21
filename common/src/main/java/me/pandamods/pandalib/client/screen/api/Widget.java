package me.pandamods.pandalib.client.screen.api;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;

import java.util.ArrayList;
import java.util.List;

public abstract class Widget implements Renderable, UIElement, WidgetHolder, GuiEventListener {
	private Widget parent;
	public final Minecraft minecraft;

	protected List<Renderable> renderables = new ArrayList<>();
	protected List<Widget> widgets = new ArrayList<>();

	private int x = 0;
	private int y = 0;
	private int width = 0;
	private int height = 0;

	private boolean hovered = false;
	private boolean focused = false;

	public Widget() {
		this.minecraft = Minecraft.getInstance();
	}

	protected void init() {
		widgets.forEach(Widget::init);
	}

	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
		hovered = this.isMouseOver(mouseX, mouseY);
		renderables.forEach(renderable -> renderable.render(guiGraphics, mouseX, mouseY, partialTick));
	}

	@Override
	public int localX() {
		return x;
	}

	@Override
	public int localY() {
		return y;
	}

	@Override
	public int width() {
		return width;
	}

	@Override
	public int height() {
		return height;
	}

	@Override
	public void setX(int x) {
		this.x = x;
	}

	@Override
	public void setY(int y) {
		this.y = y;
	}

	@Override
	public void setWidth(int width) {
		this.width = width;
	}

	@Override
	public void setHeight(int height) {
		this.height = height;
	}

	@Override
	public void setParent(Widget parent) {
		this.parent = parent;
	}

	public Screen getScreen() {
		return this.minecraft.screen;
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
		if (listener instanceof UIElement uiElement) uiElement.setParent(this);
		return getScreen().addWidget(listener);
	}

	protected <T extends Widget> T addWidgetPanel(T widget) {
		widgets.add(widget);
		renderables.add(widget);
		widget.setParent(this);
		return widget;
	}

	protected void clearWidgets() {
		widgets.forEach(Widget::clearWidgets);
		renderables.clear();
		widgets.clear();
	}

	@Override
	public Widget getParent() {
		return parent;
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
	public boolean isMouseOver(double mouseX, double mouseY) {
		return minX() <= mouseX && minY() <= mouseY && maxX() >= mouseX && maxY() >= mouseY;
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
		if (GuiEventListener.super.mouseClicked(mouseX, mouseY, button)) return true;
		return onMouseClick(mouseX, mouseY, button);
	}

	@Override
	public boolean mouseReleased(double mouseX, double mouseY, int button) {
		if (GuiEventListener.super.mouseReleased(mouseX, mouseY, button)) return true;
		return onMouseRelease(mouseX, mouseY, button);
	}

	@Override
	public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
		if (GuiEventListener.super.mouseDragged(mouseX, mouseY, button, dragX, dragY)) return true;
		return onMouseDrag(mouseX, mouseY, button, dragX, dragY);
	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
		if (GuiEventListener.super.mouseScrolled(mouseX, mouseY, delta)) return true;
		return onMouseScroll(mouseX, mouseY, delta);
	}

	@Override
	public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
		if (GuiEventListener.super.keyReleased(keyCode, scanCode, modifiers)) return true;
		return onMouseRelease(keyCode, scanCode, modifiers);
	}

	@Override
	public boolean charTyped(char codePoint, int modifiers) {
		if (GuiEventListener.super.charTyped(codePoint, modifiers)) return true;
		return onCharType(codePoint, modifiers);
	}
}
