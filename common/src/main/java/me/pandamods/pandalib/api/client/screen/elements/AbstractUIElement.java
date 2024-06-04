package me.pandamods.pandalib.api.client.screen.elements;

import me.pandamods.pandalib.api.client.screen.PLScreen;
import me.pandamods.pandalib.api.client.screen.layouts.PLLayoutElement;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.navigation.ScreenRectangle;

import java.util.Optional;
import java.util.function.Consumer;

public abstract class AbstractUIElement implements UIElement, PLLayoutElement {
	private PLScreen screen;
	private UIElement parent;

	protected int x = 0;
	protected int y = 0;
	protected int width = 0;
	protected int height = 0;

	private boolean active = true;
	private boolean visible = true;
	private boolean focused = false;
	private boolean hovered = false;

	@Override
	public PLScreen getScreen() {
		return this.screen;
	}

	@Override
	public void setScreen(PLScreen screen) {
		this.screen = screen;
	}

	@Override
	public Optional<UIElement> getParent() {
		return Optional.ofNullable(this.parent);
	}

	@Override
	public void setParent(UIElement parent) {
		this.parent = parent;
	}

	@Override
	public void setX(int x) {
		setRelativeX(getParent().map(uiElement -> x - uiElement.getX()).orElse(x));
	}

	@Override
	public void setY(int y) {
		setRelativeY(getParent().map(uiElement -> y - uiElement.getY()).orElse(y));
	}

	@Override
	public int getX() {
		return UIElement.super.getX();
	}

	@Override
	public int getY() {
		return UIElement.super.getY();
	}

	public void setRelativeX(int x) {
		this.x = x;
	}

	public void setRelativeY(int y) {
		this.y = y;
	}

	@Override
	public int getRelativeX() {
		return this.x;
	}

	@Override
	public int getRelativeY() {
		return this.y;
	}

	@Override
	public int getWidth() {
		return this.width;
	}

	@Override
	public int getHeight() {
		return this.height;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public void setSize(int width, int height) {
		this.width = width;
		this.height = height;
	}

	@Override
	public boolean isFocused() {
		return this.focused;
	}

	@Override
	public void setFocused(boolean focused) {
		this.focused = focused;
	}

	public boolean isHovered() {
		return hovered;
	}

	public void setHovered(boolean hovered) {
		this.hovered = hovered;
	}

	public boolean isHoveredOrFocused() {
		return isFocused() || isHovered();
	}

	protected void checkHoverState(int mouseX, int mouseY) {
		this.hovered = this.isMouseOver(mouseX, mouseY);
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public boolean isActive() {
		return active;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public boolean isVisible() {
		return visible;
	}

	public boolean isActiveAndVisible() {
		return isActive() && isVisible();
	}

	@Override
	public void visitWidgets(Consumer<AbstractWidget> consumer) {}

	@Override
	public ScreenRectangle getRectangle() {
		return UIElement.super.getRectangle();
	}

	@Override
	public boolean isMouseOver(double mouseX, double mouseY) {
		if (!this.isVisible()) return false;
		return UIElement.super.isMouseOver(mouseX, mouseY);
	}
}