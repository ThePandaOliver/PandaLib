package me.pandamods.pandalib.forge;

import me.pandamods.pandalib.PandaLibClient;
import me.pandamods.pandalib.api.client.screen.config.ConfigScreenProvider;
import me.pandamods.test.PandaLibTest;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.fml.ModLoadingContext;

public class PandaLibForgeClient {
	public static void clientInit() {
		PandaLibClient.init();
		ModLoadingContext.get().registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class, () ->
				new ConfigScreenHandler.ConfigScreenFactory((minecraft, screen) ->
						new ConfigScreenProvider<>(screen, PandaLibTest.CLIENT_CONFIG).get()));
	}
}
