/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */
@file:JvmName("ServerLifecycleEvents")

package dev.pandasystems.pandalib.event.serverevents

import dev.pandasystems.pandalib.listener.ListenerFactory
import net.minecraft.server.MinecraftServer

val serverStartingEvent = ListenerFactory.create<(server: MinecraftServer) -> Unit>()
val ServerStoppingEvent = ListenerFactory.create<(server: MinecraftServer) -> Unit>()