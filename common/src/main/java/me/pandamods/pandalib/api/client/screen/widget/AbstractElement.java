package me.pandamods.pandalib.api.client.screen.widget;

import me.pandamods.pandalib.api.client.screen.Element;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;

import java.util.Optional;

public class AbstractElement implements Element {
	public final Minecraft minecraft;

	private Screen screen;
	private Element parent;

	protected int x = 0;
	protected int y = 0;
	protected int width = 0;
	protected int height = 0;

	private boolean focused = false;
	private boolean hovered = false;

	public AbstractElement() {
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
	public Optional<Element> getParent() {
		return Optional.ofNullable(this.parent);
	}

	@Override
	public void setParent(Element parent) {
		this.parent = parent;
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
}