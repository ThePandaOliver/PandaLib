package me.pandamods.pandalib.client.screen.api.config;

import me.pandamods.pandalib.client.screen.api.PLScreen;
import me.pandamods.pandalib.client.screen.api.WidgetImpl;

public class ConfigCategory extends WidgetImpl {
	private final ConfigMenu configMenu;

	public ConfigCategory(ConfigMenu configMenu, String name) {
		super(configMenu);
		this.configMenu = configMenu;
	}

	@Override
	public int getLocalX() {
		return 0;
	}

	@Override
	public int getLocalY() {
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
}
