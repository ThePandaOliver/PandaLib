package me.pandamods.pandalib;

import me.pandamods.test.PandaLibClientTest;
import me.pandamods.test.PandaLibTest;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class PandaLibClient {
	public static void init() {
		if (PandaLibTest.shouldInit()) {
			PandaLibClientTest.init();
		}
	}
}
