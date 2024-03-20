package me.pandamods.pandalib.client.screen.api;

public abstract class Widget extends WidgetImpl {
	private int x = 0;
	private int y = 0;
	private int width = 0;
	private int height = 0;

	public Widget(WidgetImpl parent) {
		super(parent);
	}

	public Widget(PLScreen screen) {
		super(screen);
	}

	public Widget(PLScreen screen, WidgetImpl parent) {
		super(screen, parent);
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public void setPosition(int x, int y) {
		setX(x);
		setY(y);
	}

	public void setScale(int width, int height) {
		setWidth(width);
		setHeight(height);
	}
}
