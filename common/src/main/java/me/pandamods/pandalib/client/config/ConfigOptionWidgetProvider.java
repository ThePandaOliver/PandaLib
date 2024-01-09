package me.pandamods.pandalib.client.config;

import me.pandamods.pandalib.client.config.screen.ConfigScreen;
import me.pandamods.pandalib.client.config.screen.widgets.ConfigEntry;
import me.pandamods.pandalib.client.config.screen.widgets.ConfigEntryList;
import me.pandamods.pandalib.client.screen.PandaLibScreen;
import me.pandamods.pandalib.client.screen.widgets.Widget;
import me.pandamods.pandalib.client.screen.widgets.WidgetImpl;

public interface ConfigOptionWidgetProvider {
	ConfigEntry create(WidgetImpl parent, ConfigEntry.Data data);
}