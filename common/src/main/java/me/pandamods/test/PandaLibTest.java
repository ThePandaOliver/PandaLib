package me.pandamods.test;

import dev.architectury.event.CompoundEventResult;
import dev.architectury.event.EventResult;
import dev.architectury.event.events.client.ClientGuiEvent;
import dev.architectury.event.events.client.ClientPlayerEvent;
import dev.architectury.event.events.common.PlayerEvent;
import dev.architectury.platform.Platform;
import me.pandamods.pandalib.api.config.PandaLibConfig;
import me.pandamods.pandalib.api.config.holders.ClientConfigHolder;
import me.pandamods.pandalib.api.config.holders.CommonConfigHolder;
import me.pandamods.test.config.TestClientConfig;
import me.pandamods.test.config.TestCommonConfig;
import net.minecraft.client.Minecraft;

public class PandaLibTest {
	public static boolean SHOULD_INIT = true;

	public static final ClientConfigHolder<TestClientConfig> CLIENT_CONFIG;
	public static final CommonConfigHolder<TestCommonConfig> COMMON_CONFIG;

	static {
		if (shouldInit()) {
			CLIENT_CONFIG = PandaLibConfig.registerClient(TestClientConfig.class);
			COMMON_CONFIG = PandaLibConfig.registerCommon(TestCommonConfig.class);
		} else {
			CLIENT_CONFIG = null;
			COMMON_CONFIG = null;
		}
	}

    public static void init() {
		ClientGuiEvent.SET_SCREEN.register(screen -> {
			if (Minecraft.getInstance().player != null)
				System.out.println(Minecraft.getInstance().player.pandaLib$getConfig(CLIENT_CONFIG).text + "-test");
			return CompoundEventResult.pass();
		});
	}

	public static boolean shouldInit() {
		return Platform.isDevelopmentEnvironment() && SHOULD_INIT;
	}
}
