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

import java.util.List;

public interface ParentUIComponent extends UIComponent {
	List<UIComponent> getChildren();
	void updateChildState(UIComponent UIComponent);

	default boolean isOverflowAllowed() {
		return false;
	}

	default UIComponent getChildAt(int x, int y) {
		for (UIComponent child : getChildren()) {
			if (child.isInBoundingBox(x, y)) {
				if (child instanceof ParentUIComponent parent) {
					return parent.getChildAt(x, y);
				} else {
					return this;
				}
			}
		}

		return isInBoundingBox(x, y) ? this : null;
	}

	@Override
	default boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		return UIComponent.super.keyPressed(keyCode, scanCode, modifiers);
	}

	@Override
	default boolean keyRelease(int keyCode, int scanCode, int modifiers) {
		return UIComponent.super.keyRelease(keyCode, scanCode, modifiers);
	}

	@Override
	default boolean mousePressed(double mouseX, double mouseY, int button) {
		for (UIComponent child : getChildren()) {
			if (!child.isInBoundingBox(getX() + mouseX, getY() + mouseY)) continue;
			if (child.mousePressed(mouseX, mouseY, button)) return true;
		}

		return UIComponent.super.mousePressed(mouseX, mouseY, button);
	}

	@Override
	default boolean mouseReleased(double mouseX, double mouseY, int button) {
		for (UIComponent child : getChildren()) {
			if (!child.isInBoundingBox(getX() + mouseX, getY() + mouseY)) continue;
			if (child.mouseReleased(mouseX, mouseY, button)) return true;
		}

		return UIComponent.super.mouseReleased(mouseX, mouseY, button);
	}

	@Override
	default boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
		return UIComponent.super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
	}

	@Override
	default void mouseMoved(double mouseX, double mouseY) {
		UIComponent.super.mouseMoved(mouseX, mouseY);
	}
}
