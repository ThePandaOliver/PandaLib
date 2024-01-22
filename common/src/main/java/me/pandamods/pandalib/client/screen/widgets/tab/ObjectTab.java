package me.pandamods.pandalib.client.screen.widgets.tab;

public class ObjectTab<T> extends AbstractTab {
	private final T value;

	public ObjectTab(T value) {
		this.value = value;
	}

	public T getValue() {
		return value;
	}
}
