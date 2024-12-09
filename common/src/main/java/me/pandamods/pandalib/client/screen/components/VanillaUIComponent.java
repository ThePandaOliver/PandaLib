/*
 * Copyright (C) 2024 Oliver Froberg (The Panda Oliver)
 *
 * This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 * You should have received a copy of the GNU Lesser General Public License
 *  along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package me.pandamods.pandalib.client.screen.components;

import me.pandamods.pandalib.client.screen.BaseUIComponent;
import me.pandamods.pandalib.client.screen.core.ParentUIComponent;
import me.pandamods.pandalib.client.screen.utils.RenderContext;
import me.pandamods.pandalib.mixin.accessor.AbstractWidgetAccessor;
import net.minecraft.client.gui.components.AbstractWidget;

public class VanillaUIComponent extends BaseUIComponent {
	private final AbstractWidget widget;

	private boolean dragging = false;
	private int draggingButton = 0;
	private double lastMouseX, lastMouseY;

	protected VanillaUIComponent(AbstractWidget widget) {
		this.widget = widget;
		this.x = widget.getX();
		this.y = widget.getY();
		this.width = widget.getWidth();
		this.height = widget.getHeight();
	}

	public static VanillaUIComponent of(AbstractWidget widget) {
		return new VanillaUIComponent(widget);
	}

	@Override
	public void mount(ParentUIComponent parent) {
		super.mount(parent);
		this.applyToWidget();
	}

	@Override
	public void dismount() {
		super.dismount();
		this.applyToWidget();
	}

	@Override
	public void setX(int x) {
		super.setX(x);
		applyToWidget();
	}

	@Override
	public void setY(int y) {
		super.setY(y);
		applyToWidget();
	}

	@Override
	public void setWidth(int width) {
		super.setWidth(width);
		applyToWidget();
	}

	@Override
	public void setHeight(int height) {
		super.setHeight(height);
		applyToWidget();
	}

	public void applyToWidget() {
		AbstractWidgetAccessor accessor = (AbstractWidgetAccessor) this.widget;
		accessor.pandaLib$setX(this.x);
		accessor.pandaLib$setY(this.y);
		accessor.pandaLib$setWidth(this.width);
		accessor.pandaLib$setHeight(this.height);
	}

	@Override
	public void render(RenderContext context, int mouseX, int mouseY, float partialTicks) {
		widget.render(context.guiGraphics, mouseX, mouseY, partialTicks);
	}

	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		return this.widget.keyPressed(keyCode, scanCode, modifiers);
	}

	@Override
	public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
		return this.widget.keyReleased(keyCode, scanCode, modifiers);
	}

	@Override
	public boolean charTyped(char codePoint, int modifiers) {
		return this.widget.charTyped(codePoint, modifiers);
	}

	@Override
	public boolean mousePressed(double mouseX, double mouseY, int button) {
		if (!dragging) {
			dragging = true;
			draggingButton = button;
		}
		return this.widget.mouseClicked(mouseX, mouseY, button);
	}

	@Override
	public boolean mouseReleased(double mouseX, double mouseY, int button) {
		dragging = false;
		return this.widget.mouseReleased(mouseX, mouseY, button);
	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
		return this.widget.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
	}

	@Override
	public void mouseMoved(double mouseX, double mouseY) {
		this.widget.mouseMoved(mouseX, mouseY);
		if (dragging) {
			double dragX = mouseX - lastMouseX;
			double dragY = mouseY - lastMouseY;
			this.widget.mouseDragged(mouseX, mouseY, draggingButton, dragX, dragY);
		}
		this.lastMouseX = mouseX;
		this.lastMouseY = mouseY;
	}

	@Override
	public void onFocusGained() {
		this.widget.setFocused(true);
	}

	@Override
	public void onFocusLost() {
		this.widget.setFocused(false);
	}
}
