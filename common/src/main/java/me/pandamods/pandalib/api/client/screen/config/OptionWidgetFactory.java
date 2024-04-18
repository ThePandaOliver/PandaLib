package me.pandamods.pandalib.api.client.screen.config;

public class OptionWidgetFactory<T> {
	private final OptionWidgetProvider<T> provider;

	public OptionWidgetFactory(OptionWidgetProvider<T> provider) {
		this.provider = provider;
	}
}