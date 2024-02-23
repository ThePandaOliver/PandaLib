package me.pandamods.pandalib.fabric;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.pandamods.pandalib.PandaLib;
import me.pandamods.test.PandaLibTest;

public class PandaLibModMenuIntegration implements ModMenuApi {
	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory() {
		return screen -> PandaLibTest.CLIENT_CONFIG.createScreen(screen);
	}
}
