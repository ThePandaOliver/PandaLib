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

package dev.pandasystems.pandalib.fabric.mixin.events;

import dev.pandasystems.pandalib.event.server.ServerBlockEventsKt;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayerGameMode;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayerGameMode.class)
public class ServerPlayerGameModeEventMixin {
	@Shadow protected ServerLevel level;

	@Shadow @Final protected ServerPlayer player;

	@Inject(method = "destroyBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Block;playerWillDestroy(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/entity/player/Player;)Lnet/minecraft/world/level/block/state/BlockState;"), cancellable = true)
	public void onBlockBreakEvent(BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
		var cancelled = !ServerBlockEventsKt.getServerBlockBreakPreEvent().getInvoker().invoke(this.level, pos, this.level.getBlockState(pos), this.player);
		if (cancelled) {
			cir.setReturnValue(false);
		}
	}

	@Inject(method = "destroyBlock", at = @At("RETURN"))
	public void onBlockBreakEventPost(BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
		if (!cir.getReturnValue()) return;
		ServerBlockEventsKt.getServerBlockBreakPostEvent().getInvoker().invoke(this.level, pos, this.level.getBlockState(pos), this.player);
	}
}
