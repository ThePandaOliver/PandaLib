package me.pandamods.pandalib.client.screen.api;

public interface UIElement {
	Widget getParent();
	void setParent(Widget parent);

	int localX();
	int localY();
	int width();
	int height();

	default int globalX() {
		if (getParent() != null)
			return localX() + getParent().globalX();
		return localX();
	}
	default int globalY() {
		if (getParent() != null)
			return localY() + getParent().globalY();
		return localY();
	}

	default int minX() {
		return globalX();
	}
	default int minY() {
		return globalY();
	}
	default int maxX() {
		return globalX() + width();
	}
	default int maxY() {
		return globalY() + height();
	}

	void setX(int x);
	void setY(int y);
	void setWidth(int width);
	void setHeight(int height);

	default void setPosition(int x, int y) {
		setX(x);
		setY(y);
	}

	default void setSize(int width, int height) {
		setWidth(width);
		setHeight(height);
	}

	default void setBounds(int minX, int minY, int maxX, int maxY) {
		setX(minX);
		setY(minY);
		setWidth(maxX - minX);
		setHeight(maxY - minY);
	}
}
