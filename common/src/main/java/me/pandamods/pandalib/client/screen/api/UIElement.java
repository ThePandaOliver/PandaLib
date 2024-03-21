package me.pandamods.pandalib.client.screen.api;

import me.pandamods.pandalib.client.screen.api.widgets.buttons.PLAbstractButton;

public interface UIElement {
	PLScreen getScreen();
	void setScreen(PLScreen screen);

	WidgetImpl getParent();
	void setParent(WidgetImpl parent);

	int getX();
	int getY();
	int width();
	int height();

	default int x() {
		if (getParent() != null)
			return getX() + getParent().x();
		return getX();
	}
	default int y() {
		if (getParent() != null)
			return getY() + getParent().y();
		return getY();
	}

	default int minX() {
		return x();
	}
	default int minY() {
		return y();
	}
	default int maxX() {
		return x() + width();
	}
	default int maxY() {
		return y() + height();
	}
}
