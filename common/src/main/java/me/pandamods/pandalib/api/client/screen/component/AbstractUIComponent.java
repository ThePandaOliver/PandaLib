package me.pandamods.pandalib.api.client.screen.component;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.layouts.LayoutElement;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.screens.Screen;

import java.util.Optional;
import java.util.function.Consumer;

public class AbstractUIComponent implements UIComponent, LayoutElement {
	public final Minecraft minecraft;

	private Screen screen;
	private UIComponent parent;

	protected int x = 0;
	protected int y = 0;
	protected int width = 0;
	protected int height = 0;

	private boolean focused = false;
	private boolean hovered = false;

	public AbstractUIComponent() {
		this.minecraft = Minecraft.getInstance();
	}

	@Override
	public Screen getScreen() {
		return this.screen;
	}

	@Override
	public void setScreen(Screen screen) {
		this.screen = screen;
	}

	@Override
	public Optional<UIComponent> getParent() {
		return Optional.ofNullable(this.parent);
	}

	@Override
	public void setParent(UIComponent parent) {
		this.parent = parent;
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
	public int getX() {
		return this.x;
	}

	@Override
	public int getY() {
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

	@Override
	public void visitWidgets(Consumer<AbstractWidget> consumer) {}

	@Override
	public ScreenRectangle getRectangle() {
		return UIComponent.super.getRectangle();
	}
}