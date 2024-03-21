package me.pandamods.pandalib.client.screen.api;

public class Division implements Element {
	private final Element parent;

	public Division(Element parent) {
		this.parent = parent;
	}

	@Override
	public Element parent() {
		return this.parent;
	}

	@Override
	public int x() {
		return 0;
	}

	@Override
	public int y() {
		return 0;
	}

	@Override
	public int width() {
		return 0;
	}

	@Override
	public int height() {
		return 0;
	}

	@Override
	public int paddingTop() {
		return 0;
	}

	@Override
	public int paddingBottom() {
		return 0;
	}

	@Override
	public int paddingRight() {
		return 0;
	}

	@Override
	public int paddingLeft() {
		return 0;
	}

	@Override
	public int marginTop() {
		return 0;
	}

	@Override
	public int marginBottom() {
		return 0;
	}

	@Override
	public int marginRight() {
		return 0;
	}

	@Override
	public int marginLeft() {
		return 0;
	}
}
