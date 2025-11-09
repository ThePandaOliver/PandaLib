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

<<<<<<<< HEAD:forge/src/main/java/mixin/events/player/ClientPacketListenerEventMixin.java
package dev.pandasystems.pandalib.forge.mixin.events.player;
========
package dev.pandasystems.pandalib.fabric.mixin.events.player;
>>>>>>>> versions/dev/1.20.3:fabric/src/main/java/mixin/events/player/ClientPacketListenerEventMixin.java

import dev.pandasystems.pandalib.event.client.ClientPlayerEventsKt;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientCommonPacketListenerImpl;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.CommonListenerCookie;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.Connection;
import net.minecraft.network.TickablePacketListener;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundLoginPacket;
import net.minecraft.network.protocol.game.ClientboundRespawnPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(ClientPacketListener.class)
public abstract class ClientPacketListenerEventMixin extends ClientCommonPacketListenerImpl implements ClientGamePacketListener, TickablePacketListener {
	protected ClientPacketListenerEventMixin(Minecraft minecraft, Connection connection, CommonListenerCookie commonListenerCookie) {
		super(minecraft, connection, commonListenerCookie);
	}

	@Inject(method = "handleLogin", at = @At("TAIL"))
	public void handleLogin(ClientboundLoginPacket packet, CallbackInfo ci) {
		ClientPlayerEventsKt.getClientPlayerJoinEvent().getInvoker().invoke(Objects.requireNonNull(this.minecraft.player));
	}

	@Unique
	private LocalPlayer pandaLib$oldPlayer;

	@Inject(method = "handleRespawn", at = @At("HEAD"))
	public void handleBeforeRespawn(ClientboundRespawnPacket packet, CallbackInfo ci) {
		pandaLib$oldPlayer = this.minecraft.player;
	}

	@Inject(method = "handleRespawn", at = @At("TAIL"))
	public void handleRespawn(ClientboundRespawnPacket packet, CallbackInfo ci) {
		var newPlayer = this.minecraft.player;
		ClientPlayerEventsKt.getClientPlayerRespawnEvent().getInvoker().invoke(pandaLib$oldPlayer, newPlayer);
	}
}
