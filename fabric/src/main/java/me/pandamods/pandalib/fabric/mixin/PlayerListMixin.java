///*
// * Copyright (C) 2024 Oliver Froberg (The Panda Oliver)
// *
// * This program is free software: you can redistribute it and/or modify
// *  it under the terms of the GNU General Public License as published by
// *  the Free Software Foundation, either version 3 of the License, or
// *  any later version.
// *
// * You should have received a copy of the GNU General Public License
// *  along with this program. If not, see <https://www.gnu.org/licenses/>.
// */
//
//package me.pandamods.pandalib.fabric.mixin;
//
//import me.pandamods.pandalib.event.events.client.PlayerEvents;
//import net.minecraft.network.Connection;
//import net.minecraft.server.level.ServerPlayer;
//import net.minecraft.server.network.CommonListenerCookie;
//import net.minecraft.server.players.PlayerList;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.injection.At;
//import org.spongepowered.asm.mixin.injection.Inject;
//import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//
//@Mixin(PlayerList.class)
//public class PlayerListMixin {
//    @Inject(method = "placeNewPlayer", at = @At("RETURN"))
//    private void placeNewPlayer(Connection connection, ServerPlayer player, CommonListenerCookie cookie, CallbackInfo ci) {
//        PlayerEvents.PLAYER_JOIN.invoker().join(player);
//    }
//
//    @Inject(method = "remove", at = @At("HEAD"))
//    private void remove(ServerPlayer player, CallbackInfo ci) {
//		PlayerEvents.PLAYER_QUIT.invoker().quit(player);
//    }
//}