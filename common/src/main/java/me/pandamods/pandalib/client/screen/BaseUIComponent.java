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

package me.pandamods.pandalib.client.screen;

import me.pandamods.pandalib.client.screen.core.ParentUIComponent;
import me.pandamods.pandalib.client.screen.core.UIComponent;
import org.jetbrains.annotations.Nullable;

public abstract class BaseUIComponent implements UIComponent {
	protected ParentUIComponent parent = null;

	protected int x, y;
	protected int width, height;

	@Override
	public ParentUIComponent getParent() {
		return parent;
	}

	@Override
	public void mount(ParentUIComponent parent) {
		ParentUIComponent oldParent = this.parent;
		this.parent = parent;
		if (parent != null)
			parent.updateChildState(this);
		if (oldParent != null)
			oldParent.updateChildState(this);
	}

	@Override
	public void dismount() {
		mount(null);
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
}
