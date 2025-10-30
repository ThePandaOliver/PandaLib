/*
 * Copyright (C) 2025-2025 Oliver Froberg (The Panda Oliver)
 *
 * This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 * You should have received a copy of the GNU Lesser General Public License
 *  along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package dev.pandasystems.pandalib.event.client

import dev.pandasystems.pandalib.utils.event
import net.minecraft.client.player.LocalPlayer
import net.minecraft.network.chat.Component

val clientPlayerJoinEvent = event<(player: LocalPlayer) -> Unit>()
val clientPlayerLeaveEvent = event<(player: LocalPlayer, reason: Component) -> Unit>()
val clientPlayerRespawnEvent = event<(oldPlayer: LocalPlayer, newPlayer: LocalPlayer) -> Unit>()
