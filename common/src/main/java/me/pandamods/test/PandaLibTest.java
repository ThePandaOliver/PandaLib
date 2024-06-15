package me.pandamods.test;

import dev.architectury.platform.Platform;
import me.pandamods.pandalib.api.config.PandaLibConfig;
import me.pandamods.pandalib.api.config.holders.ClientConfigHolder;
import me.pandamods.pandalib.api.config.holders.CommonConfigHolder;
import me.pandamods.test.config.TestConfig;

public class PandaLibTest {
	public static boolean SHOULD_INIT = true;

	public static final CommonConfigHolder<TestConfig> CONFIG;

	static {
		if (shouldInit()) {
			CONFIG = PandaLibConfig.registerCommon(TestConfig.class);
		} else {
			CONFIG = null;
		}
	}

    public static void init() {
	}

	public static boolean shouldInit() {
		return Platform.isDevelopmentEnvironment() && SHOULD_INIT;
	}
}
