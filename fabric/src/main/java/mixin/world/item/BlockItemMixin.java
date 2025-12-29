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

package dev.pandasystems.pandalib.fabric.mixin.world.item;

import com.llamalad7.mixinextras.sugar.Local;
import dev.pandasystems.pandalib.mixin.world.item.BlockItemKtImpl;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockItem.class)
public class BlockItemMixin {
	@Unique
    private BlockItemKtImpl pandaLib$impl = new BlockItemKtImpl();
	
	@Inject(method = "place", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/BlockItem;placeBlock(Lnet/minecraft/world/item/context/BlockPlaceContext;Lnet/minecraft/world/level/block/state/BlockState;)Z"), cancellable = true)
	void beforeBlockPlace(BlockPlaceContext context, CallbackInfoReturnable<InteractionResult> cir, @Local(name = "blockPlaceContext") BlockPlaceContext newContext) {
		pandaLib$impl.beforeBlockPlaceEvent(context, newContext, cir);
	}

	@Inject(method = "placeBlock", at = @At("TAIL"))
	void afterBlockPlace(BlockPlaceContext context, BlockState state, CallbackInfoReturnable<Boolean> cir) {
		pandaLib$impl.afterBlockPlaceEvent(context, state);
	}
}
