/*
 * Copyright (C) 2024 Oliver Froberg (The Panda Oliver)
 *
 * This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 * You should have received a copy of the GNU General Public License
 *  along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package me.pandamods.pandalib.api.client.screen.elements;

import me.pandamods.pandalib.api.client.screen.PLRenderable;
import me.pandamods.pandalib.api.utils.screen.PLGuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;

import java.util.List;
import java.util.Optional;

public interface UIElementHolderAccessor extends GuiEventListener, PLRenderable {
	List<UIElement> getChildren();
	List<NarratableEntry> getNarratables();
	List<PLRenderable> getRenderables();
	List<UIElementHolder> getHolders();

	default void init() {}

	default <T> T addElement(T element) {
		if (element instanceof UIElement)
			addElement((UIElement) element);
		return element;
	}

	default <T extends UIElement> T addElement(T element) {
		this.getChildren().add(element);

		if (element instanceof NarratableEntry)
			this.getNarratables().add((NarratableEntry) element);

		if (element instanceof UIElementHolder holder) {
			this.getHolders().add(holder);
			holder.init();
		}

		if (element instanceof PLRenderable)
			this.getRenderables().add((PLRenderable) element);
		return element;
	}

	@SuppressWarnings("SuspiciousMethodCalls")
	default void removeElement(Object listener) {
		this.getChildren().remove(listener);
		this.getNarratables().remove(listener);
		this.getRenderables().remove(listener);
		this.getHolders().remove(listener);
	}

	default void clearElements() {
		this.getChildren().clear();
		this.getNarratables().clear();
		this.getHolders().forEach(UIElementHolder::clearElements);
		this.getHolders().clear();
		this.getRenderables().clear();
	}

	default void rebuildWidgets() {
		this.clearElements();
		this.init();
	}

	default void repositionElements() {
		this.rebuildWidgets();
	}

	default void tick() {
		this.getChildren().forEach(UIElement::tick);
	}

	@Override
	default void render(PLGuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
		for (PLRenderable renderable : getRenderables()) {
			renderable.render(guiGraphics, mouseX, mouseY, partialTick);
		}
	}

	default Optional<UIElement> getElementAt(double mouseX, double mouseY) {
		for (UIElement element : this.getChildren()) {
			if (!element.isMouseOver(mouseX, mouseY)) continue;
			return Optional.of(element);
		}
		return Optional.empty();
	}

	@Override
	default boolean mouseClicked(double mouseX, double mouseY, int button) {
		for (UIElement element : this.getChildren()) {
			if (!element.isMouseOver(mouseX, mouseY)) continue;
			element.setFocused();
			if (element.mouseClicked(mouseX, mouseY, button)) {
				return true;
			}
		}
		return false;
	}

	@Override
	default boolean mouseReleased(double mouseX, double mouseY, int button) {
		for (UIElement element : this.getChildren()) {
			if (!element.isMouseOver(mouseX, mouseY)) continue;
			if (element.mouseReleased(mouseX, mouseY, button))
				return true;
		}
		return false;
	}

	@Override
	default boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
		for (UIElement element : this.getChildren()) {
			if (!element.isMouseOver(mouseX, mouseY)) continue;
			if (element.mouseDragged(mouseX, mouseY, button, dragX, dragY))
				return true;
		}
		return false;
	}

	@Override
	default boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
		for (UIElement element : this.getChildren()) {
			if (!element.isMouseOver(mouseX, mouseY)) continue;
			if (element.mouseScrolled(mouseX, mouseY, scrollX, scrollY))
				return true;
		}
		return false;
	}

	@Override
	default void mouseMoved(double mouseX, double mouseY) {
		for (UIElement element : this.getChildren()) {
			element.mouseMoved(mouseX, mouseY);
		}
	}

	@Override
	default void setFocused(boolean focused) {}

	@Override
	default boolean isFocused() {
		return false;
	}
}
