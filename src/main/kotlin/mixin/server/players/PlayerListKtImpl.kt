/*
 * Copyright (C) 2025 Oliver Froberg (The Panda Oliver)
 *
 * This program is free software: you can redistribute it and/or modify 
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 * You should have received a copy of the GNU Lesser General Public License
 *  along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package dev.pandasystems.pandalib.mixin.server.players

import dev.pandasystems.pandalib.event.server.serverPlayerJoinEvent
import dev.pandasystems.pandalib.event.server.serverPlayerLeaveEvent
import dev.pandasystems.pandalib.event.server.serverPlayerRespawnEvent
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.Entity
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable

class PlayerListKtImpl {
	fun onPlayerJoinEvent(player: ServerPlayer) {
		serverPlayerJoinEvent.invoker(player)
	}

	fun onPlayerLeaveEvent(player: ServerPlayer) {
		serverPlayerLeaveEvent.invoker(player)
	}

	fun onRespawnEvent(player: ServerPlayer, keepInventory: Boolean, removalReason: Entity.RemovalReason, cir: CallbackInfoReturnable<ServerPlayer>) {
		val newPlayer = cir.getReturnValue()
		serverPlayerRespawnEvent.invoker(player, newPlayer!!, keepInventory, removalReason)
	}
}