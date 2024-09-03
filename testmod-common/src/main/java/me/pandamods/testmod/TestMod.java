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

package me.pandamods.testmod;

import me.pandamods.pandalib.config.PandaLibConfig;
import me.pandamods.pandalib.config.holders.ClientConfigHolder;
import me.pandamods.pandalib.config.holders.CommonConfigHolder;
import me.pandamods.testmod.config.ClientTestConfig;
import me.pandamods.testmod.config.CommonTestConfig;
import net.minecraft.resources.ResourceLocation;

public class TestMod {
	public static final String MOD_ID = "testmod";

	public static final CommonConfigHolder<CommonTestConfig> COMMON_CONFIG = PandaLibConfig.registerCommon(CommonTestConfig.class);
	public static final ClientConfigHolder<ClientTestConfig> CLIENT_CONFIG = PandaLibConfig.registerClient(ClientTestConfig.class);

	public static void init() {
	}

	public static ResourceLocation location(String path) {
		#if MC_VER >= MC_1_21
		return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
		#else
			return new ResourceLocation(MOD_ID, path);
		#endif
	}
}
