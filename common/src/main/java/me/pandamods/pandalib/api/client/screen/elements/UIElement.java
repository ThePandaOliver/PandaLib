package me.pandamods.pandalib.api.client.screen.elements;

import me.pandamods.pandalib.api.client.screen.PLScreen;
import me.pandamods.pandalib.api.utils.ScreenUtils;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;

import java.util.Optional;

public interface UIElement extends GuiEventListener {
	PLScreen getScreen();
	void setScreen(PLScreen screen);

	Optional<UIElement> getParent();
	void setParent(UIElement parent);

	int getX();
	int getY();
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

	default boolean isInteractable() {
		return true;
	}

	@Override
	default boolean isMouseOver(double mouseX, double mouseY) {
		return ScreenUtils.isMouseOver(mouseX, mouseY, minX(), minY(), maxX(), maxY());
	}
}