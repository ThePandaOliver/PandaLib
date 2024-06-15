package me.pandamods.pandalib.api.client.screen.elements;

import me.pandamods.pandalib.api.client.screen.PLScreen;
import me.pandamods.pandalib.api.utils.ScreenUtils;
import net.minecraft.client.gui.components.events.GuiEventListener;

import java.util.Optional;

public interface UIElement extends GuiEventListener {
	PLScreen getScreen();
	void setScreen(PLScreen screen);

	Optional<UIElement> getParent();
	void setParent(UIElement parent);

	default int getX() {
		return getParent().map(uiElement -> uiElement.getChildOffsetX() + uiElement.getX() + getRelativeX()).orElse(getRelativeX());
	}
	default int getY() {
		return getParent().map(uiElement -> uiElement.getChildOffsetY() + uiElement.getY() + getRelativeY()).orElse(getRelativeY());
	}

	default int getChildOffsetX() {
		return 0;
	}
	default int getChildOffsetY() {
		return 0;
	}

	int getRelativeX();
	int getRelativeY();

	int getWidth();
	int getHeight();

	default int minX() {
		return getX();
	}
	default int minY() {
		return getY();
	}
	default int maxX() {
		return minX() + getWidth();
	}
	default int maxY() {
		return minY() + getHeight();
	}

	@Override
	default boolean isMouseOver(double mouseX, double mouseY) {
		return ScreenUtils.isMouseOver(mouseX, mouseY, minX(), minY(), maxX(), maxY());
	}

	default void setFocused() {
		getScreen().setFocused(this);
	}

	default void tick() {
	}
}