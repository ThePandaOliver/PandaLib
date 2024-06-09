package me.pandamods.pandalib.neoforge;

import me.pandamods.pandalib.PandaLibClient;
import me.pandamods.pandalib.api.client.screen.config.menu.ConfigScreenProvider;
import me.pandamods.test.PandaLibTest;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.neoforge.client.ConfigScreenHandler;

public class PandaLibNeoForgeClient {
	public static void clientInit() {
		PandaLibClient.init();
		ModLoadingContext.get().registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class, () ->
				new ConfigScreenHandler.ConfigScreenFactory((minecraft, screen) ->
						new ConfigScreenProvider<>(screen, PandaLibTest.CLIENT_CONFIG).get()));
	}
}
