package me.pandamods.pandalib.client.screen.api.widgets;

import me.pandamods.pandalib.client.screen.api.Element;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;

public abstract class AbstractElement implements Element, GuiEventListener, NarratableEntry, Renderable {
	private Element parent;
	private boolean hovered = false;
	private boolean focused = false;
	public boolean visible = true;
	public boolean active = true;

	private int x = 0;
	private int y = 0;
	private int width = 0;
	private int height = 0;

	@Override
	public Element getParent() {
		return this.parent;
	}

	@Override
	public void setParent(Element element) {
		this.parent = element;
	}

	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
		this.hovered = this.isMouseOver(mouseX, mouseY);
	}

	@Override
	public int getX() {
		return x;
	}
	@Override
	public int getY() {
		return y;
	}
	@Override
	public int getWidth() {
		return width;
	}
	@Override
	public int getHeight() {
		return height;
	}

	public void setX(int x) {
		this.x = x;
	}
	public void setY(int y) {
		this.y = y;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public void setHeight(int height) {
		this.height = height;
	}

	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}
	public void setSize(int width, int height) {
		this.width = width;
		this.height = height;
	}
	public void setBounds(int minX, int minY, int maxX, int maxY) {
		this.x = minX;
		this.y = minY;
		this.width = maxX - minX;
		this.height = maxY - minY;
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

	public boolean isHoveredOrFocused() {
		return this.isHovered() || this.isFocused();
	}

	@Override
	public boolean isActive() {
		return active;
	}

	public boolean isVisible() {
		return visible;
	}

	public boolean isActiveOrVisible() {
		return this.isActive() || this.isVisible();
	}

	@Override
	public NarrationPriority narrationPriority() {
		if (this.isFocused()) {
			return NarratableEntry.NarrationPriority.FOCUSED;
		}
		if (this.isHovered()) {
			return NarratableEntry.NarrationPriority.HOVERED;
		}
		return NarratableEntry.NarrationPriority.NONE;
	}

	@Override
	public boolean isMouseOver(double mouseX, double mouseY) {
		return this.isActiveOrVisible() && this.minX() > mouseX && this.maxX() < mouseX && this.minY() > mouseY && this.maxY() < mouseY;
	}
}
