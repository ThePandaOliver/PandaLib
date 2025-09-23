/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.mixin.events.player;

import dev.pandasystems.pandalib.event.serverevents.ServerPlayerEvents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.portal.TeleportTransition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerEventMixin {
	@Shadow
	public abstract ServerLevel level();

	@Shadow
	private boolean isChangingDimension;

	@Inject(
			method = "teleport(Lnet/minecraft/world/level/portal/TeleportTransition;)Lnet/minecraft/server/level/ServerPlayer;",
			at = @At(
					value = "FIELD",
					target = "Lnet/minecraft/server/level/ServerPlayer;isChangingDimension:Z"
			), cancellable = true
	)
	public void beforeDimensionChange(TeleportTransition teleportTransition, CallbackInfoReturnable<ServerPlayer> cir) {
		var cancelled = !ServerPlayerEvents.getServerPlayerChangeDimensionPreEvent().getInvoker().invoke((ServerPlayer) (Object) this, level(),
				teleportTransition.newLevel(), teleportTransition);
		if (cancelled) {
			cir.setReturnValue(null);
		}
	}

	@Inject(method = "teleport(Lnet/minecraft/world/level/portal/TeleportTransition;)Lnet/minecraft/server/level/ServerPlayer;", at = @At("RETURN"))
	public void afterDimensionChange(TeleportTransition teleportTransition, CallbackInfoReturnable<ServerPlayer> cir) {
		if (this.isChangingDimension) {
			ServerPlayerEvents.getServerPlayerChangeDimensionPostEvent().getInvoker().invoke((ServerPlayer) (Object) this, level(), teleportTransition.newLevel(), teleportTransition);
		}
	}
}
