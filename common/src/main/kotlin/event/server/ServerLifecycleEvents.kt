/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */
@file:JvmName("ServerLifecycleEvents")

package dev.pandasystems.pandalib.event.server

import dev.pandasystems.pandalib.utils.event
import net.minecraft.server.MinecraftServer

val serverStartingEvent = event<(server: MinecraftServer) -> Unit>()
val serverStoppingEvent = event<(server: MinecraftServer) -> Unit>()