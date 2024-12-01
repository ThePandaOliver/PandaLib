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
import me.pandamods.pandalib.client.screen.layouts.StackContainer;

public class TestScreen extends BasePLScreen<StackContainer> {
	private StackContainer stack;

	public TestScreen() {
		super(StackContainer::new);
	}

	@Override
	protected void build(StackContainer rootComponent) {
		this.stack = rootComponent;
	}

	int i = 0;

	@Override
	public void tick() {
		if (stack == null) return;
		if (stack.getChildren().size() > 10) {
			stack.getChildren().getFirst().unmount();
		}
		TextUIComponent text = new TextUIComponent();
		text.setText("Hello World! " + ++i);
		text.mount(stack);
	}
}
