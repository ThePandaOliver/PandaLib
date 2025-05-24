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
package me.pandamods.pandalib.neoforge.client

import me.pandamods.pandalib.PandaLib
import me.pandamods.pandalib.client.PandaLibClient
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.common.Mod

@Mod(value = PandaLib.MOD_ID, dist = [Dist.CLIENT])
class PandaLibClientNeoForge(eventBus: IEventBus) {
	init {
		PandaLibClient.init()
	}
}
