/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.mixin.events.player;

import dev.pandasystems.pandalib.event.server.ServerPlayerEvents;
import net.minecraft.network.Connection;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.CommonListenerCookie;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerList.class)
public class PlayerListEventMixin {
	@Inject(method = "placeNewPlayer", at = @At("RETURN"))
	private void onPlayerJoinEvent(Connection connection, ServerPlayer player, CallbackInfo ci) {
		ServerPlayerEvents.getServerPlayerJoinEvent().getInvoker().invoke(player);
	}

	@Inject(method = "remove", at = @At("HEAD"))
	private void onPlayerLeaveEvent(ServerPlayer player, CallbackInfo ci) {
		ServerPlayerEvents.getServerPlayerLeaveEvent().getInvoker().invoke(player);
	}

	@Inject(method = "respawn", at = @At("TAIL"))
	private void onRespawn(ServerPlayer player, boolean keepInventory, CallbackInfoReturnable<ServerPlayer> cir) {
		ServerPlayer newPlayer = cir.getReturnValue();
		ServerPlayerEvents.getServerPlayerRespawnEvent().getInvoker().invoke(player, newPlayer, keepInventory);
	}
}
