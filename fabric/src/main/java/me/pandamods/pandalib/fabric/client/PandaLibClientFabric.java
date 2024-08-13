package me.pandamods.pandalib.fabric.client;

import me.pandamods.pandalib.client.PandaLibClient;
import net.fabricmc.api.ClientModInitializer;

public class PandaLibClientFabric implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		PandaLibClient.init();
	}
}
