/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.mixin;

import dev.pandasystems.pandalib.api.networking.PacketHandler;
import dev.pandasystems.pandalib.api.networking.PacketRegistry;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public class ClientPacketListenerMixin {
	@Inject(method = "handleCustomPayload", at = @At("HEAD"), cancellable = true)
	public void handleCustomPayload(CustomPacketPayload payload, CallbackInfo ci) {
		@SuppressWarnings("unchecked")
		PacketHandler<CustomPacketPayload> packetHandler = (PacketHandler<CustomPacketPayload>) PacketRegistry.CLIENT_PACKET_HANDLERS.get(payload.type());
		if (packetHandler == null) return;
		packetHandler.handle(payload);
		ci.cancel();
	}
}
