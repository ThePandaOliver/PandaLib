package me.pandamods.pandalib.api.client.screen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.layouts.LayoutElement;
import net.minecraft.client.gui.screens.Screen;

import java.util.Optional;

public interface Element extends GuiEventListener {
	Screen getScreen();
	void setScreen(Screen screen);

	Optional<Element> getParent();
	void setParent(Element parent);

	int getX();
	int getY();
	int getWidth();
	int getHeight();

//	void setX(int x);
//	void setY(int y);
//	void setWidth(int width);
//	void setHeight(int height);
//
//	default void setPosition(int x, int y) {
//		setX(x);
//		setY(y);
//	}
//
//	default void setSize(int width, int height) {
//		setWidth(width);
//		setHeight(height);
//	}
//
//	default void setBounds(int minX, int minY, int maxX, int maxY) {
//		setPosition(minX, minY);
//		setSize(maxX - minX, maxY - minY);
//	}

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

	default boolean isFocusable() {
		return true;
	}

	@Override
	default boolean isMouseOver(double mouseX, double mouseY) {
		return mouseX >= this.minX() && mouseX <= this.maxX() && mouseY >= this.minY() && mouseY <= this.maxY();
	}
}