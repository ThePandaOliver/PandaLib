/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.mixin.events.player;

import dev.pandasystems.pandalib.api.event.EventListener;
import dev.pandasystems.pandalib.api.event.commonevents.ServerPlayerWorldChangeEvent;
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
	@Shadow private boolean isChangingDimension;

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
		var event = new ServerPlayerWorldChangeEvent.Pre((ServerPlayer) (Object) this, serverLevel(), destination);
		EventListener.invokeEvent(event);
		if (event.getCancelled()) {
			cir.setReturnValue(null);
		}
	}

	@Inject(method = "changeDimension", at = @At("RETURN"))
	public void afterDimensionChange(ServerLevel destination, CallbackInfoReturnable<Entity> cir) {
		if (this.isChangingDimension) {
			EventListener.invokeEvent(new ServerPlayerWorldChangeEvent.Post((ServerPlayer) (Object) this, serverLevel(), destination));
		}
	}
}
