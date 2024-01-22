package me.pandamods.pandalib.client.screen.widgets;

import java.util.List;
import java.util.function.Function;

public interface WidgetImpl {
	List<Widget> widgets();

	void setX(int x);
	int getX();
	default int getChildX() {
		return 0;
	}
	void setY(int y);
	int getY();
	default int getChildY() {
		return 0;
	}
	void setWidth(int width);
	int getWidth();
	void setHeight(int height);
	int getHeight();

	default int getMinX() {
		return this.getX();
	}
	default int getMinY() {
		return this.getY();
	}
	default int getMaxX() {
		return this.getX() + this.getWidth();
	}
	default int getMaxY() {
		return this.getY() + this.getHeight();
	}
	default void setPosition(int x, int y) {
		this.setX(x);
		this.setY(y);
	}
	default void setSize(int width, int height) {
		this.setWidth(width);
		this.setHeight(height);
	}
	default void setBounds(int minX, int minY, int maxX, int maxY) {
		this.setPosition(minX, minY);
		this.setSize(maxX - minX, maxY - minY);
	}

	default boolean loopWidgetsMouseIsOver(double mouseX, double mouseY, Function<Widget, Boolean> function) {
		for (Widget widget : this.widgets()) {
			if (widget.isMouseOver(mouseX, mouseY) && widget.isVisible()) {
				return function.apply(widget);
			}
		}
		return false;
	}
}
