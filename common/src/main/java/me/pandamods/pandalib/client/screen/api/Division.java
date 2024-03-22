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
}
