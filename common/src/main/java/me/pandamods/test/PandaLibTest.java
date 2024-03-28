package me.pandamods.test;

import dev.architectury.platform.Platform;
import me.pandamods.pandalib.api.config.PandaLibConfig;
import me.pandamods.pandalib.api.config.holders.ClientConfigHolder;
import me.pandamods.pandalib.api.config.holders.CommonConfigHolder;
import me.pandamods.test.config.TestClientConfig;
import me.pandamods.test.config.TestCommonConfig;

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

    public static void init() {}

	public static boolean shouldInit() {
		return Platform.isDevelopmentEnvironment() && SHOULD_INIT;
	}
}
