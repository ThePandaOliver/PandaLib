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

package me.pandamods.pandalib.api.client.screen.layouts;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.navigation.ScreenRectangle;

@Environment(EnvType.CLIENT)
public interface PLLayout {
	void setX(int var1);
	void setY(int var1);

	int getX();
	int getY();

	default void setPosition(int x, int y) {
		this.setX(x);
		this.setY(y);
	}

	void setWidth(int width);
	void setHeight(int height);

	int getWidth();
	int getHeight();

	default void setSize(int width, int height) {
		this.setWidth(width);
		this.setHeight(height);
	}

	default ScreenRectangle getRectangle() {
		return new ScreenRectangle(this.getX(), this.getY(), this.getWidth(), this.getHeight());
	}
}