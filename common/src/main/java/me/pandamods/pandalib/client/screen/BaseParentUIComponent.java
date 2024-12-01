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
import me.pandamods.pandalib.client.screen.utils.RenderContext;

public abstract class BaseParentUIComponent extends BaseUIComponent implements ParentUIComponent {
	@Override
	public void render(RenderContext context, int mouseX, int mouseY, float partialTicks) {
		Runnable renderChildren = () -> renderChildren(context, mouseX, mouseY, partialTicks);
		if (this.isOverflowAllowed())
			renderChildren.run();
		else
			context.scissor(this.getX(), this.getY(), this.getX() + this.getWidth(), this.getY() + this.getHeight(), renderChildren);
	}

	protected void renderChildren(RenderContext context, int mouseX, int mouseY, float partialTicks) {
		for (UIComponent child : this.getChildren()) {
			renderChild(child, context, mouseX, mouseY, partialTicks);
		}
	}

	public void renderChild(UIComponent child, RenderContext context, int mouseX, int mouseY, float partialTicks) {
		child.render(context, mouseX, mouseY, partialTicks);
	}

	@Override
	public void updateChildState(UIComponent UIComponent) {
		if (UIComponent.getParent() == this) {
			if (!getChildren().contains(UIComponent)) {
				addChild(UIComponent);
			}
		} else {
			removeChild(UIComponent);
		}
	}

	protected abstract void addChild(UIComponent UIComponent);
	protected abstract void removeChild(UIComponent UIComponent);
}
