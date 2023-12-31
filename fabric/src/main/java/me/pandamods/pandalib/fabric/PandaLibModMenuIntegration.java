package me.pandamods.pandalib.fabric;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.pandamods.pandalib.PandaLib;
import me.pandamods.pandalib.client.config.screen.ConfigScreen;

public class PandaLibModMenuIntegration implements ModMenuApi {
	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory() {
		return screen -> ConfigScreen.Create(PandaLib.MOD_ID, screen);
	}
}
