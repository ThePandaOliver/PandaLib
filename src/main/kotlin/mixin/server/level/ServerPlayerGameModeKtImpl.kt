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

import dev.pandasystems.pandalib.event.server.serverBlockBreakPostEvent
import dev.pandasystems.pandalib.event.server.serverBlockBreakPreEvent
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable

object ServerPlayerGameModeKtImpl {
	fun onBlockBreakEventPre(level: ServerLevel, pos: BlockPos, player: ServerPlayer, cir: CallbackInfoReturnable<Boolean>) {
		val cancelled = !serverBlockBreakPreEvent.invoker(level, pos, level.getBlockState(pos), player)
		if (cancelled) {
			cir.returnValue = false
		}
	}

	fun onBlockBreakEventPost(level: ServerLevel, pos: BlockPos, player: ServerPlayer) {
		serverBlockBreakPostEvent.invoker(level, pos, level.getBlockState(pos), player)
	}
}