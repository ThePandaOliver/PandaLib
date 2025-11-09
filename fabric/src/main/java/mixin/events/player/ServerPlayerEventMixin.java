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

package dev.pandasystems.pandalib.fabric.mixin.events.player;

import dev.pandasystems.pandalib.event.server.ServerPlayerEventsKt;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerEventMixin {
	@Shadow
	private boolean isChangingDimension;

	@Shadow
	public abstract ServerLevel serverLevel();

	@Inject(
			method = "changeDimension",
			at = @At(
					value = "FIELD",
					target = "Lnet/minecraft/server/level/ServerPlayer;isChangingDimension:Z"
			), cancellable = true
	)
	public void beforeDimensionChange(ServerLevel destination, CallbackInfoReturnable<Entity> cir) {
		var cancelled = !ServerPlayerEventsKt.getServerPlayerChangeDimensionPreEvent().getInvoker().invoke((ServerPlayer) (Object) this, serverLevel(), destination);
		if (cancelled) {
			cir.setReturnValue(null);
		}
	}

	@Inject(method = "changeDimension", at = @At("RETURN"))
	public void afterDimensionChange(ServerLevel destination, CallbackInfoReturnable<Entity> cir) {
		if (this.isChangingDimension) {
			ServerPlayerEventsKt.getServerPlayerChangeDimensionPostEvent().getInvoker().invoke((ServerPlayer) (Object) this, serverLevel(), destination);
		}
	}
}
