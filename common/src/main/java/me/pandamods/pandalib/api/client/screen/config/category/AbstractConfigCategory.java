package me.pandamods.pandalib.api.client.screen.config.category;

import me.pandamods.pandalib.api.client.screen.elements.ScrollableUIElementHolder;
import me.pandamods.pandalib.api.client.screen.elements.UIElementHolder;
import net.minecraft.network.chat.Component;

import java.util.List;

public abstract class AbstractConfigCategory extends ScrollableUIElementHolder {
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

	@Override
	public int getContentWidth() {
		return 0;
	}

	@Override
	public int getContentHeight() {
		return 0;
	}
}