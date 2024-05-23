package me.pandamods.pandalib.api.client.screen;

import me.pandamods.pandalib.api.utils.ScreenUtils;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;

import java.util.Optional;

public interface UIComponent extends GuiEventListener {
	Screen getScreen();
	void setScreen(Screen screen);

	Optional<UIComponent> getParent();
	void setParent(UIComponent parent);

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