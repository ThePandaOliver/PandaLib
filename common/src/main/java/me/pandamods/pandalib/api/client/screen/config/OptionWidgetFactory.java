package me.pandamods.pandalib.api.client.screen.config;

public class OptionWidgetFactory<T> {
	private final OptionGuiProvider<T> provider;

	public OptionWidgetFactory(OptionGuiProvider<T> provider) {
		this.provider = provider;
	}
}