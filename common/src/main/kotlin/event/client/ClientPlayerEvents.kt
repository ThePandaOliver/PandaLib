/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */
@file:JvmName("ClientPlayerEvents")

package dev.pandasystems.pandalib.event.client

import dev.pandasystems.pandalib.utils.event
import net.minecraft.client.player.LocalPlayer

val clientPlayerJoinEvent = event<(player: LocalPlayer) -> Unit>()
val clientPlayerLeaveEvent = event<(player: LocalPlayer) -> Unit>()
val clientPlayerRespawnEvent = event<(oldPlayer: LocalPlayer, newPlayer: LocalPlayer) -> Unit>()
