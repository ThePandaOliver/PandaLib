package me.pandamods.pandalib.fabric;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.pandamods.test.client.screen.TestClientConfigScreen;

public class PandaLibModMenuIntegration implements ModMenuApi {
	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory() {
		return TestClientConfigScreen::new;
	}
}
