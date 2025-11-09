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

package dev.pandasystems.pandalib.neoforge.mixin.events;

import com.llamalad7.mixinextras.sugar.Local;
import dev.pandasystems.pandalib.event.client.ClientBlockEventsKt;
import dev.pandasystems.pandalib.event.server.ServerBlockEventsKt;
import dev.pandasystems.pandalib.utils.GameEnvironmentKt;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockItem.class)
public class BlockItemEventMixin {
	@Inject(method = "place", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/BlockItem;placeBlock(Lnet/minecraft/world/item/context/BlockPlaceContext;Lnet/minecraft/world/level/block/state/BlockState;)Z"), cancellable = true)
	public void beforeBlockPlace(BlockPlaceContext context, CallbackInfoReturnable<InteractionResult> cir, @Local(ordinal = 1) BlockPlaceContext newContext) {
		var blockState = newContext.getLevel().getBlockState(newContext.getClickedPos());
		if (!GameEnvironmentKt.getGameEnvironment().isHost()) {
			ClientBlockEventsKt.getClientBlockPlaceEvent().getInvoker()
					.invoke(newContext.getLevel(), newContext.getClickedPos(), blockState, newContext.getPlayer());
		}

		if (GameEnvironmentKt.getGameEnvironment().isHost()) {
			var cancelled = !ServerBlockEventsKt.getServerBlockPlacePreEvent().getInvoker()
					.invoke(newContext.getLevel(), newContext.getClickedPos(), blockState, newContext.getPlayer());
			if (cancelled) {
				cir.setReturnValue(InteractionResult.FAIL);
			}
		}
	}

	@Inject(method = "placeBlock", at = @At("TAIL"))
	public void afterBlockPlace(BlockPlaceContext context, BlockState state, CallbackInfoReturnable<Boolean> cir) {
		if (!GameEnvironmentKt.getGameEnvironment().isHost()) {
			ClientBlockEventsKt.getClientBlockPlaceEvent().getInvoker()
					.invoke(context.getLevel(), context.getClickedPos(), state, context.getPlayer());
		}
		if (GameEnvironmentKt.getGameEnvironment().isHost()) {
			ServerBlockEventsKt.getServerBlockPlacePostEvent().getInvoker()
					.invoke(context.getLevel(), context.getClickedPos(), state, context.getPlayer());
		}
	}
}
