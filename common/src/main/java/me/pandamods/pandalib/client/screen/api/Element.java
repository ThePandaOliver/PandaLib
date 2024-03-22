package me.pandamods.pandalib.client.screen.api;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;

public interface Element {
	Element getParent();
	void setParent(Element element);

	default Minecraft minecraft() {
		return Minecraft.getInstance();
	}
	default Screen screen() {
		return Minecraft.getInstance().screen;
	}

	int getX();
	int getY();
	int getWidth();
	int getHeight();

	default int getGlobalX() {
		if (getParent() != null) return getParent().getGlobalX() + getX();
		return getX();
	}
	default int getGlobalY() {
		if (getParent() != null) return getParent().getGlobalY() + getY();
		return getY();
	}

	default int minX() {
		return getGlobalX();
	}
	default int minY() {
		return getGlobalY();
	}
	default int maxX() {
		return minX() + getWidth();
	}
	default int maxY() {
		return minY() + getHeight();
	}
}
