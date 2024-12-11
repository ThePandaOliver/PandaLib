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

package me.pandamods.pandalib.client.screen.containers;

import me.pandamods.pandalib.client.screen.BaseParentUIComponent;
import me.pandamods.pandalib.client.screen.core.UIComponent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UIContainer extends BaseParentUIComponent {
	protected final List<UIComponent> children = new ArrayList<>();
	protected final List<UIComponent> viewChildren = Collections.unmodifiableList(children);

	@Override
	public List<UIComponent> getChildren() {
		return viewChildren;
	}

	@Override
	protected void addChild(UIComponent UIComponent) {
		children.add(UIComponent);
	}

	@Override
	protected void removeChild(UIComponent UIComponent) {
		children.remove(UIComponent);
	}
}
