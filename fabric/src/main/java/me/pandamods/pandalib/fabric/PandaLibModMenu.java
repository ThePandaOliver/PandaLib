package me.pandamods.pandalib.fabric;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.pandamods.pandalib.api.client.screen.config.menu.ConfigScreenProvider;
import me.pandamods.pandalib.api.config.PandaLibConfig;
import me.pandamods.test.PandaLibTest;
import me.pandamods.test.config.TestConfig;

public class PandaLibModMenu implements ModMenuApi {
	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory() {
		return screen -> new ConfigScreenProvider<>(screen, PandaLibTest.CONFIG).get();
	}
}
