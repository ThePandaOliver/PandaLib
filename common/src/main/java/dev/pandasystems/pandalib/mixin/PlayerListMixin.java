package dev.pandasystems.pandalib.mixin;

import dev.pandasystems.pandalib.core.handles.player.PlayerHandleKt;
import dev.pandasystems.pandalib.event.events.ServerPlayerConnectionEventContext;
import dev.pandasystems.pandalib.event.events.ServerPlayerEventsKt;
import dev.pandasystems.pandalib.event.events.ServerPlayerRespawnEventContext;
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
abstract class PlayerListMixin {
	@Inject(method = "respawn", at = @At("TAIL"))
	private void afterRespawn(ServerPlayer oldPlayer, boolean alive, Entity.RemovalReason removalReason, CallbackInfoReturnable<ServerPlayer> cir) {
		ServerPlayer newPlayer = cir.getReturnValue();
		ServerPlayerEventsKt.getPlayerServerAfterRespawn().invoke(new ServerPlayerRespawnEventContext(PlayerHandleKt.handle(oldPlayer), PlayerHandleKt.handle(newPlayer), alive));
	}

	@Inject(method = "placeNewPlayer", at = @At("RETURN"))
	private void firePlayerJoinEvent(Connection connection, ServerPlayer player, CommonListenerCookie clientData, CallbackInfo ci) {
		ServerPlayerEventsKt.getPlayerServerJoin().invoke(new ServerPlayerConnectionEventContext(PlayerHandleKt.handle(player)));
	}

	@Inject(method = "remove", at = @At("HEAD"))
	private void firePlayerLeaveEvent(ServerPlayer player, CallbackInfo ci) {
		ServerPlayerEventsKt.getPlayerServerLeave().invoke(new ServerPlayerConnectionEventContext(PlayerHandleKt.handle(player)));
	}
}