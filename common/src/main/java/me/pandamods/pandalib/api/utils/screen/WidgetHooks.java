package me.pandamods.pandalib.api.utils.screen;

import net.minecraft.client.gui.components.AbstractWidget;

public class WidgetHooks {
	public static void setX(AbstractWidget widget, int x) {
		widget.setX(x);
	}

	public static void setY(AbstractWidget widget, int y) {
		widget.setX(y);
	}

	public static void setWidth(AbstractWidget widget, int width) {
		widget.setWidth(width);
	}

	public static void setHeight(AbstractWidget widget, int height) {
		widget.height = height;
	}
}
