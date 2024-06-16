/*
 * Copyright (C) 2024 Oliver Froberg (The Panda Oliver)
 *
 * This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 * You should have received a copy of the GNU General Public License
 *  along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

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
