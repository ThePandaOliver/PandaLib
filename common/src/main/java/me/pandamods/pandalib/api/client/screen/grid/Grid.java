package me.pandamods.pandalib.api.client.screen.grid;

import me.pandamods.pandalib.api.client.screen.UIComponent;
import me.pandamods.pandalib.api.client.screen.UIComponentHolder;
import me.pandamods.pandalib.api.client.screen.widget.AbstractUIComponent;
import net.minecraft.client.gui.screens.Screen;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Grid extends UIComponentHolder {
	private final List<Child<?>> layoutChild = new ArrayList<>();

	public void addChild(Object component, int row, int column) {
		layoutChild.add(new Child<>(component, row, column));
	}

	public class Child<T> extends UIComponentHolder {
		private final T component;
		private final int row;
		private final int column;

		public Child(T component, int row, int column) {
			this.component = component;
			this.row = row;
			this.column = column;
		}
	}
}
