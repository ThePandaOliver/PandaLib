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

package dev.pandasystems.pandalib.neoforge.mixin.server.level;

import dev.pandasystems.pandalib.mixin.server.level.ServerPlayerKtImpl;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.portal.DimensionTransition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin {
	@Shadow
	private boolean isChangingDimension;

	@Unique
	private ServerPlayerKtImpl pandaLib$impl = new ServerPlayerKtImpl((ServerPlayer) (Object) this);

	@Inject(
			method = "changeDimension",
			at = @At("HEAD"),
			cancellable = true
	)
	public void beforeDimensionChange(DimensionTransition teleportTransition, CallbackInfoReturnable<ServerPlayer> cir) {
		pandaLib$impl.onDimensionChangePreEvent(teleportTransition, cir);
	}

	@Inject(method = "teleport(Lnet/minecraft/world/level/portal/TeleportTransition;)Lnet/minecraft/server/level/ServerPlayer;", at = @At("RETURN"))
	public void afterDimensionChange(DimensionTransition teleportTransition, CallbackInfoReturnable<ServerPlayer> cir) {
		pandaLib$impl.onDimensionChangePostEvent(teleportTransition, cir, this.isChangingDimension);
	}
}
