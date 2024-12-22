/*
 * Copyright (C) 2024 Oliver Froberg (The Panda Oliver)
 *
 * This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 * You should have received a copy of the GNU Lesser General Public License
 *  along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package me.pandamods.test;

import me.pandamods.pandalib.config.PandaLibConfig;
import me.pandamods.pandalib.config.holders.ClientConfigHolder;
import me.pandamods.pandalib.config.holders.CommonConfigHolder;
import me.pandamods.test.config.ClientTestConfig;
import me.pandamods.test.config.CommonTestConfig;
import net.minecraft.resources.ResourceLocation;

public class TestMod {
	public static final String MOD_ID = "testmod";
	private static TestMod instance;

	private static final CommonConfigHolder<CommonTestConfig> COMMON_TEST_CONFIG = PandaLibConfig.registerCommon(CommonTestConfig.class);
	private static final ClientConfigHolder<ClientTestConfig> CLIENT_TEST_CONFIG = PandaLibConfig.registerClient(ClientTestConfig.class);

	public TestMod() {
		instance = this;
	}

	public static ResourceLocation resourceLocation(String path) {
		return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
	}

	public static TestMod getInstance() {
		return instance;
	}
}
