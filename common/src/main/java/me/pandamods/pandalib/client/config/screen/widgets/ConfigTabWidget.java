package me.pandamods.pandalib.client.config.screen.widgets;

import me.pandamods.pandalib.client.config.ConfigCategoryImpl;
import me.pandamods.pandalib.client.config.screen.ConfigScreen;
import me.pandamods.pandalib.client.screen.widgets.tab.AbstractTab;
import me.pandamods.pandalib.client.screen.widgets.tab.AbstractTabWidget;
import me.pandamods.pandalib.client.screen.widgets.tab.ObjectTab;

public class ConfigTabWidget extends AbstractTabWidget<ConfigTabButton> {
	private final ConfigScreen configScreen;

	public ConfigTabWidget(ConfigScreen parent) {
		super(parent);
		this.configScreen = parent;
	}

	@Override
	public void onTabClick(ConfigTabButton tab) {
		configScreen.setCategory(tab.getValue());
	}
}
