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

	public VanillaUIComponent(AbstractWidget widget) {
		this.widget = widget;
		this.width = widget.getWidth();
		this.height = widget.getHeight();
	}

	@Override
	public void mount(ParentUIComponent parent) {
		super.mount(parent);
		this.applyToWidget();
	}

	@Override
	public void unmount() {
		super.unmount();
		this.applyToWidget();
	}

	@Override
	public void position(int x, int y) {
		super.position(x, y);
		applyToWidget();
	}

	@Override
	public void size(int width, int height) {
		super.size(width, height);
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

	// Todo: Add compatibility with Abstract widget Events
}
