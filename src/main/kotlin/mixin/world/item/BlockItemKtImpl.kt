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

package dev.pandasystems.pandalib.mixin.world.item

import dev.pandasystems.pandalib.event.client.clientBlockPlaceEvent
import dev.pandasystems.pandalib.event.server.serverBlockPlacePostEvent
import dev.pandasystems.pandalib.event.server.serverBlockPlacePreEvent
import dev.pandasystems.pandalib.utils.gameEnvironment
import net.minecraft.client.Minecraft
import net.minecraft.world.InteractionResult
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.block.state.BlockState
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable

class BlockItemKtImpl {
	fun beforeBlockPlaceEvent(context: BlockPlaceContext, newContext: BlockPlaceContext, cir: CallbackInfoReturnable<InteractionResult>) {
		val blockState = newContext.level.getBlockState(newContext.clickedPos)
		if (gameEnvironment.isHost) { // Server-side
			val player = newContext.player ?: return
			val cancelled = !serverBlockPlacePreEvent.invoker(newContext.level, newContext.clickedPos, blockState, player)
			if (cancelled) cir.returnValue = InteractionResult.CONSUME
		}
	}

	fun afterBlockPlaceEvent(context: BlockPlaceContext, state: BlockState) {
		if (!gameEnvironment.isHost) { // Client-side
			val player = Minecraft.getInstance().player ?: return
			clientBlockPlaceEvent.invoker(context.level, context.clickedPos, state, player)
		}
		if (gameEnvironment.isHost) { // Server-side
			val player = context.player ?: return
			serverBlockPlacePostEvent.invoker(context.level, context.clickedPos, state, player)
		}
	}
}