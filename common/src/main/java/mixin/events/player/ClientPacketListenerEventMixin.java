/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.mixin.events.player;

import dev.pandasystems.pandalib.event.client.ClientPlayerEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.TickablePacketListener;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundLoginPacket;
import net.minecraft.network.protocol.game.ClientboundRespawnPacket;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(ClientPacketListener.class)
public abstract class ClientPacketListenerEventMixin implements ClientGamePacketListener, TickablePacketListener {
	@Shadow
	@Final
	public Minecraft minecraft;

	@Inject(method = "handleLogin", at = @At("TAIL"))
	public void handleLogin(ClientboundLoginPacket packet, CallbackInfo ci) {
		ClientPlayerEvents.getClientPlayerJoinEvent().getInvoker().invoke(Objects.requireNonNull(this.minecraft.player));
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
		ClientPlayerEvents.getClientPlayerRespawnEvent().getInvoker().invoke(pandaLib$oldPlayer, newPlayer);
	}
}
