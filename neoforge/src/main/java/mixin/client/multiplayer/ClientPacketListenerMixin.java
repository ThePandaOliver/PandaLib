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

package dev.pandasystems.pandalib.neoforge.mixin.client.multiplayer;

import dev.pandasystems.pandalib.mixin.client.multiplayer.ClientPacketListenerKtImpl;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.CommonListenerCookie;
import net.minecraft.network.Connection;
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

@Mixin(ClientPacketListener.class)
public abstract class ClientPacketListenerMixin extends ClientCommonPacketListenerImpl implements ClientGamePacketListener, TickablePacketListener {
	protected ClientPacketListenerMixin(Minecraft minecraft, Connection connection, CommonListenerCookie commonListenerCookie) {
		super(minecraft, connection, commonListenerCookie);
	}

	@Inject(method = "handleLogin", at = @At("TAIL"))
	public void handleLogin(ClientboundLoginPacket packet, CallbackInfo ci) {
		ClientPacketListenerKtImpl.INSTANCE.onLoginEvent();
	}

	@Inject(method = "handleRespawn", at = @At("HEAD"))
	public void handleBeforeRespawn(ClientboundRespawnPacket packet, CallbackInfo ci) {
		ClientPacketListenerKtImpl.INSTANCE.onRespawnPreEvent();
	}

	@Inject(method = "handleRespawn", at = @At("TAIL"))
	public void handleRespawn(ClientboundRespawnPacket packet, CallbackInfo ci) {
		ClientPacketListenerKtImpl.INSTANCE.onRespawnPostEvent();
	}
}
