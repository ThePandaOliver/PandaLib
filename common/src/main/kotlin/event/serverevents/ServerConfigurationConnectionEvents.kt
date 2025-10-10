/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */
@file:JvmName("ServerConfigurationConnectionEvents")

package dev.pandasystems.pandalib.event.serverevents

import dev.pandasystems.pandalib.utils.event
import net.minecraft.server.MinecraftServer
import net.minecraft.server.network.ServerConfigurationPacketListenerImpl

@get:JvmName("configure")
val serverConfigurationConnectionEvent = event<(handler: ServerConfigurationPacketListenerImpl, server: MinecraftServer) -> Unit>()