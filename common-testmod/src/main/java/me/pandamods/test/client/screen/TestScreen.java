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

package me.pandamods.test.client.screen;

import me.pandamods.pandalib.client.screen.BasePLScreen;
import me.pandamods.pandalib.client.screen.components.TextUIComponent;
import me.pandamods.pandalib.client.screen.components.VanillaUIComponent;
import me.pandamods.pandalib.client.screen.layouts.StackContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

public class TestScreen extends BasePLScreen<StackContainer> {
	public TestScreen() {
		super(StackContainer::new);
	}

	@Override
	protected void build(StackContainer rootComponent) {
		VanillaUIComponent vanillaTestButton = new VanillaUIComponent(Button.builder(Component.literal("Test Vanilla Button"),
				button -> System.out.println("Test click"))
				.width(200)
				.build());

		vanillaTestButton.mount(rootComponent);

		TextUIComponent textUIComponent = new TextUIComponent(Minecraft.getInstance().font, Component.literal("Test Text"));
		textUIComponent.mount(rootComponent);
	}
}
