package me.pandamods.pandalib.api.client.screen.layouts;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public interface PLLayoutElement2 {
	void setX(int x);
	void setY(int y);

	default void setPosition(int x, int y) {
		this.setX(x);
		this.setY(y);
	}

	int getX();
	int getY();

	void setWidth(int width);
	void setHeight(int height);

	default void setSize(int width, int height) {
		this.setWidth(width);
		this.setHeight(height);
	}

	int getWidth();
	int getHeight();
}