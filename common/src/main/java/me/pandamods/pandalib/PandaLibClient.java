package me.pandamods.pandalib;

import me.pandamods.pandalib.event.EventHandlerClient;
import me.pandamods.pandalib.network.PacketHandlerClient;
import me.pandamods.test.PandaLibClientTest;
import me.pandamods.test.PandaLibTest;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class PandaLibClient {
	public static void init() {
		PacketHandlerClient.init();
		EventHandlerClient.init();

		if (PandaLibTest.shouldInit()) {
			PandaLibClientTest.init();
		}
	}
}
