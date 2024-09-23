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

package me.pandamods.pandalib;

import me.pandamods.pandalib.core.event.EventHandler;
import net.minecraft.resources.ResourceLocation;

public class PandaLib {
    public static final String MOD_ID = "pandalib";

    public static void init() {
//		ConfigNetworking.registerPackets();
		EventHandler.Register();
    }

	public static ResourceLocation resourceLocation(String path) {
		#if MC_VER >= MC_1_21
			return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
		#else
			return new ResourceLocation(MOD_ID, path);
		#endif
	}
}