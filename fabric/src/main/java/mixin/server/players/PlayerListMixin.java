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

package dev.pandasystems.pandalib.fabric.mixin.server.players;

import dev.pandasystems.pandalib.mixin.server.players.PlayerListKtImpl;
import net.minecraft.network.Connection;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.CommonListenerCookie;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerList.class)
public class PlayerListMixin {
	@Inject(method = "placeNewPlayer", at = @At("RETURN"))
	private void onPlayerJoinEvent(Connection connection, ServerPlayer player, CommonListenerCookie cookie, CallbackInfo ci) {
		PlayerListKtImpl.INSTANCE.onPlayerJoinEvent(player);
	}

	@Inject(method = "remove", at = @At("HEAD"))
	private void onPlayerLeaveEvent(ServerPlayer player, CallbackInfo ci) {
		PlayerListKtImpl.INSTANCE.onPlayerLeaveEvent(player);
	}

	@Inject(method = "respawn", at = @At("TAIL"))
	private void onRespawn(ServerPlayer player, boolean keepEverything, CallbackInfoReturnable<ServerPlayer> cir) {
		PlayerListKtImpl.INSTANCE.onRespawnEvent(player, keepEverything, cir);
	}
}
