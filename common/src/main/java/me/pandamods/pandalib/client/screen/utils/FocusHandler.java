/*
 * Copyright (C) 2024 Oliver Froberg (The Panda Oliver)
 *
 * This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 * You should have received a copy of the GNU Lesser General Public License
 *  along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package me.pandamods.pandalib.client.screen.utils;

import me.pandamods.pandalib.client.screen.core.UIComponent;
import org.jetbrains.annotations.Nullable;

public class FocusHandler {
	@Nullable
	protected UIComponent focused;

	public void focus(UIComponent focused) {
		if (this.focused == focused) return;
		if (this.focused != null) this.focused.onFocusLost();
		this.focused = focused;
		if (this.focused != null) this.focused.onFocusGained();
	}

	public void clearFocus() {
		focus(null);
	}

	@Nullable
	public UIComponent getFocused() {
		return focused;
	}

	public boolean isFocused(UIComponent component) {
		return focused == component;
	}
}
