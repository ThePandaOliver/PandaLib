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

import me.pandamods.pandalib.client.screen.utils.RenderContext;

public interface UIComponent {
	void render(RenderContext context, int mouseX, int mouseY, float partialTicks);

	ParentUIComponent getParent();

	void mount(ParentUIComponent parent);
	void unmount();

	void setX(int x);
	void setY(int y);

	void setWidth(int width);
	void setHeight(int height);

	int getX();
	int getY();

	int getWidth();
	int getHeight();

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
}
