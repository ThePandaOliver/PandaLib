package me.pandamods.pandalib.client.screen.api;

public interface UIElement {
	Widget parent();

	int getLocalX();
	int getLocalY();
	int width();
	int height();

	default int getX() {
		if (parent() != null)
			return getLocalX() + parent().getX();
		return getLocalX();
	}
	default int getY() {
		if (parent() != null)
			return getLocalY() + parent().getY();
		return getLocalY();
	}

	default int minX() {
		return getX();
	}
	default int minY() {
		return getY();
	}
	default int maxX() {
		return getX() + width();
	}
	default int maxY() {
		return getY() + height();
	}

	void setX(int x);
	void setY(int y);
	void setWidth(int width);
	void setHeight(int height);

	default void setPosition(int x, int y) {
		setX(x);
		setY(y);
	}

	default void setScale(int width, int height) {
		setWidth(width);
		setHeight(height);
	}
}
