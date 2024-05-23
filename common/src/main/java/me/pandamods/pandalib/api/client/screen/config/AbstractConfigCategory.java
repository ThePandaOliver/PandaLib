package me.pandamods.pandalib.api.client.screen.config;

import me.pandamods.pandalib.api.client.screen.UIComponentHolder;
import me.pandamods.pandalib.api.client.screen.config.menu.ConfigMenu;
import net.minecraft.network.chat.Component;

import java.util.List;

public abstract class AbstractConfigCategory extends UIComponentHolder {
	private AbstractConfigCategory parentCategory;

	public abstract Component getName();

	public abstract List<AbstractConfigCategory> getCategories();

	public AbstractConfigCategory getParentCategory() {
		return parentCategory;
	}

	protected void setParentCategory(AbstractConfigCategory parentCategory) {
		this.parentCategory = parentCategory;
	}

	public abstract void save();
	public abstract void load();
	public abstract void reset();
}