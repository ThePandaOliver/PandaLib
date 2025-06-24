/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.mixin;

import dev.pandasystems.pandalib.api.networking.PacketHandler;
import dev.pandasystems.pandalib.api.networking.PacketRegistry;
import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerGamePacketListenerImpl.class)
public class ServerPacketListenerMixin {
	@Inject(method = "handleCustomPayload", at = @At("HEAD"), cancellable = true)
	public void handleCustomPayload(ServerboundCustomPayloadPacket packet, CallbackInfo ci) {
		CustomPacketPayload payload = packet.payload();
		@SuppressWarnings("unchecked")
		PacketHandler<CustomPacketPayload> packetHandler = (PacketHandler<CustomPacketPayload>) PacketRegistry.SERVER_PACKET_HANDLERS.get(payload.type());
		if (packetHandler == null) return;
		packetHandler.handle(payload);
		ci.cancel();
	}
}
