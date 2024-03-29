package me.pandamods.pandalib.api.client.screen.config;

import me.pandamods.pandalib.api.client.screen.ElementHolder;

public class AbstractConfigCategory extends ElementHolder {
	@Override
	public int getX() {
		return ConfigCategoryList.OPEN_SIZE;
	}

	@Override
	public int getWidth() {
		return this.getScreen().width - ConfigCategoryList.OPEN_SIZE - 2;
	}

	@Override
	public int getHeight() {
		return this.getScreen().height;
	}
}