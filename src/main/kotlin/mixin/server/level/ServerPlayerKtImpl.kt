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

package dev.pandasystems.pandalib.mixin.server.level

import dev.pandasystems.pandalib.event.server.serverPlayerChangeDimensionPostEvent
import dev.pandasystems.pandalib.event.server.serverPlayerChangeDimensionPreEvent
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.level.portal.TeleportTransition
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable

class ServerPlayerKtImpl(val player: ServerPlayer) {
	fun onDimensionChangePreEvent(teleportTransition: TeleportTransition, cir: CallbackInfoReturnable<ServerPlayer>) {
		val cancelled = !serverPlayerChangeDimensionPreEvent.invoker(
			player, player.level(),
			teleportTransition.newLevel(), teleportTransition
		)
		if (cancelled) cir.returnValue = null
	}

	fun onDimensionChangePostEvent(teleportTransition: TeleportTransition, cir: CallbackInfoReturnable<ServerPlayer>, isChangingDimension: Boolean) {
		if (isChangingDimension && cir.getReturnValue() != null) {
			serverPlayerChangeDimensionPostEvent.invoker.invoke(player, player.level(), teleportTransition.newLevel(), teleportTransition)
		}
	}
}