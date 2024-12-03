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

package me.pandamods.pandalib.client.screen.core;

import me.pandamods.pandalib.client.screen.utils.FocusHandler;
import me.pandamods.pandalib.client.screen.utils.RenderContext;
import org.jetbrains.annotations.Nullable;

public interface UIComponent {
	void render(RenderContext context, int mouseX, int mouseY, float partialTicks);

	ParentUIComponent getParent();

	default boolean hasParent() {
		return this.getParent() != null;
	}

	void mount(ParentUIComponent parent);
	void dismount();

	default ParentUIComponent root() {
		ParentUIComponent root = this.getParent();
		while (root.hasParent()) root = root.getParent();
		return root;
	}

	@Nullable FocusHandler getFocusHandler();

	void setX(int x);
	void setY(int y);

	void setWidth(int width);
	void setHeight(int height);

	int getX();
	int getY();

	int getWidth();
	int getHeight();

	default boolean isInBoundingBox(double x, double y) {
		return x >= getX() && y >= getY() && x < getX() + getWidth() && y < getY() + getHeight();
	}

	default void position(int x, int y) {
		setX(x);
		setY(y);
	}
	default void size(int width, int height) {
		setWidth(width);
		setHeight(height);
	}
	default void size(int size) {
		size(size, size);
	}

	default boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		return false;
	}

	default boolean keyReleased(int keyCode, int scanCode, int modifiers) {
		return false;
	}

	default boolean charTyped(char codePoint, int modifiers) {
		return false;
	}

	default boolean mousePressed(double mouseX, double mouseY, int button) {
		return false;
	}

	default boolean mouseReleased(double mouseX, double mouseY, int button) {
		return false;
	}

	default boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
		return false;
	}
}
