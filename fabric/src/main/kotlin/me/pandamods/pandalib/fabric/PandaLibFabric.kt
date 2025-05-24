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
package me.pandamods.pandalib.fabric

import me.pandamods.pandalib.PandaLib
import me.pandamods.pandalib.event.events.NetworkingEvents
import me.pandamods.pandalib.platform.Services
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents.ServerStarted
import net.minecraft.server.MinecraftServer

class PandaLibFabric : ModInitializer {
	override fun onInitialize() {
		ServerLifecycleEvents.SERVER_STARTED.register(ServerStarted { server: MinecraftServer? -> Companion.server = server })

		PandaLib.init()
		NetworkingEvents.PACKET_PAYLOAD_REGISTRY.invoker().register(Services.NETWORK)
	}

	companion object {
		var server: MinecraftServer? = null
	}
}
