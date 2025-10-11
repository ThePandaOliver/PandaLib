/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.mixin.events;

import com.llamalad7.mixinextras.sugar.Local;
import dev.pandasystems.pandalib.event.client.ClientBlockEvents;
import dev.pandasystems.pandalib.event.server.ServerBlockEvents;
import dev.pandasystems.pandalib.platform.GameDataKt;
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
		if (!GameDataKt.game.isHost()) {
			ClientBlockEvents.placeEvent().getInvoker()
					.invoke(newContext.getLevel(), newContext.getClickedPos(), blockState, newContext.getPlayer());
		}

		if (GameDataKt.game.isHost()) {
			var cancelled = !ServerBlockEvents.placePreEvent().getInvoker()
					.invoke(newContext.getLevel(), newContext.getClickedPos(), blockState, newContext.getPlayer());
			if (cancelled) {
				cir.setReturnValue(InteractionResult.FAIL);
			}
		}
	}

	@Inject(method = "placeBlock", at = @At("TAIL"))
	public void afterBlockPlace(BlockPlaceContext context, BlockState state, CallbackInfoReturnable<Boolean> cir) {
		if (!GameDataKt.game.isHost()) {
			ClientBlockEvents.placeEvent().getInvoker()
					.invoke(context.getLevel(), context.getClickedPos(), state, context.getPlayer());
		}
		if (GameDataKt.game.isHost()) {
			ServerBlockEvents.placePostEvent().getInvoker()
					.invoke(context.getLevel(), context.getClickedPos(), state, context.getPlayer());
		}
	}
}
