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