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

package me.pandamods.pandalib.client.screen.layouts;

import me.pandamods.pandalib.client.screen.BaseParentUIComponent;
import me.pandamods.pandalib.client.screen.core.UIComponent;
import me.pandamods.pandalib.client.screen.utils.RenderContext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StackContainer extends BaseParentUIComponent {
	private final List<UIComponent> children = new ArrayList<>();
	private final List<UIComponent> viewChildren = Collections.unmodifiableList(children);

	private boolean realign = true;
	private int stackLength = 0;

	@Override
	protected void renderChildren(RenderContext context, int mouseX, int mouseY, float partialTicks) {
		if (realign) stackLength = 0;
		super.renderChildren(context, mouseX, mouseY, partialTicks);
	}

	@Override
	public void renderChild(UIComponent child, RenderContext context, int mouseX, int mouseY, float partialTicks) {
		if (realign) child.setY(stackLength);
		super.renderChild(child, context, mouseX, mouseY, partialTicks);
		if (realign) stackLength += child.getHeight();
	}

	@Override
	public List<UIComponent> getChildren() {
		return viewChildren;
	}

	@Override
	public void updateChildState(UIComponent uiComponent) {
		super.updateChildState(uiComponent);
		align();
	}

	@Override
	protected void addChild(UIComponent UIComponent) {
		children.add(UIComponent);
	}

	@Override
	protected void removeChild(UIComponent UIComponent) {
		children.remove(UIComponent);
	}

	/**
	 * Realigns children next frame
	 */
	public void align() {
		realign = true;
	}
}
