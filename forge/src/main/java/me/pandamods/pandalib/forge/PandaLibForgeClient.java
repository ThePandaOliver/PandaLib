package me.pandamods.pandalib.forge;

import me.pandamods.pandalib.PandaLibClient;

public class PandaLibForgeClient {
	public static void clientInit() {
		PandaLibClient.init();
//		ModLoadingContext.get().registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class, () ->
//				new ConfigScreenHandler.ConfigScreenFactory((minecraft, screen) -> new ConfigScreen(screen)));
	}
}
