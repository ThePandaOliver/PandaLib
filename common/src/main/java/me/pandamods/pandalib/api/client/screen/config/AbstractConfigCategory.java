package me.pandamods.pandalib.api.client.screen.config;

import me.pandamods.pandalib.api.client.screen.UIComponentHolder;
import net.minecraft.network.chat.Component;

public abstract class AbstractConfigCategory extends UIComponentHolder {
	@Override
	public int getX() {
		return ConfigSideBar.OPEN_SIZE;
	}

	@Override
	public int getWidth() {
		return this.getScreen().width - ConfigSideBar.OPEN_SIZE - 2;
	}

	@Override
	public int getHeight() {
		return this.getScreen().height;
	}

	public abstract Component getName();

	public abstract void save();
	public abstract void load();
	public abstract void reset();
}