package me.pandamods.pandalib.client.screen.api;

public interface Element {
	Element parent();

	int x();
	int y();
	int width();
	int height();

	int paddingTop();
	int paddingBottom();
	int paddingRight();
	int paddingLeft();

	int marginTop();
	int marginBottom();
	int marginRight();
	int marginLeft();
}
