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

package me.pandamods.pandalib.api.client.screen;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;

import java.util.List;

/**
 * The ScreenHooks class provides utility methods for accessing the internal properties of a Screen object.
 */
@Environment(EnvType.CLIENT)
public class ScreenHooks {
	public static List<GuiEventListener> getChildren(Screen screen) {
		return screen.children;
	}
	public static List<NarratableEntry> getNarratables(Screen screen) {
		return screen.narratables;
	}
	public static List<Renderable> getRenderables(Screen screen) {
		return screen.renderables;
	}
	public static GuiEventListener getFocused(Screen screen) {
		return screen.focused;
	}
}