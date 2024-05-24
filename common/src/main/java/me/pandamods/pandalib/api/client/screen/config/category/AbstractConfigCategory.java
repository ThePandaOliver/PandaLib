package me.pandamods.pandalib.api.client.screen.config.category;

import me.pandamods.pandalib.api.client.screen.elements.UIElementHolder;
import net.minecraft.network.chat.Component;

import java.util.List;

public abstract class AbstractConfigCategory extends UIElementHolder {
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