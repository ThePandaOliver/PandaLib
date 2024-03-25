package me.pandamods.pandalib;

import com.mojang.logging.LogUtils;
import dev.architectury.platform.Platform;
import me.pandamods.pandalib.event.EventHandler;
import me.pandamods.pandalib.network.PacketHandler;
import me.pandamods.test.PandaLibTest;
import org.slf4j.Logger;

public class PandaLib {
    public static final String MOD_ID = "pandalib";
	public static final Logger LOGGER = LogUtils.getLogger();

    public static void init() {
		PacketHandler.init();
		EventHandler.init();

		if (PandaLibTest.shouldInit()) {
			PandaLibTest.init();
		}
    }
}
