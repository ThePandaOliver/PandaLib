/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.mixin.events.player;

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
