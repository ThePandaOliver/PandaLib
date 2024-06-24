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

import com.mojang.logging.LogUtils;
import me.pandamods.pandalib.event.EventHandler;
import me.pandamods.pandalib.network.ConfigNetworking;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;

public class PandaLib {
    public static final String MOD_ID = "pandalib";
	public static final Logger LOGGER = LogUtils.getLogger();

    public static void init() {
		ConfigNetworking.registerPackets();
		EventHandler.Register();
    }

	public static ResourceLocation ID(String path) {
		return new ResourceLocation(MOD_ID, path);
	}
}
