/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.neoforge.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import dev.pandasystems.pandalib.api.networking.PacketHandler;
import dev.pandasystems.pandalib.api.networking.PacketRegistry;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.common.ClientCommonPacketListener;
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.ServerCommonPacketListener;
import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.registration.NetworkRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(NetworkRegistry.class)
public class NetworkRegistryMixin {
	@Inject(
			method = "checkPacket(Lnet/minecraft/network/protocol/Packet;Lnet/minecraft/network/protocol/common/ServerCommonPacketListener;)V",
			at = @At(value = "INVOKE", target = "Ljava/lang/UnsupportedOperationException;<init>(Ljava/lang/String;)V"),
			cancellable = true
	)
	private static void checkClientPacket(Packet<?> packet, ServerCommonPacketListener listener, CallbackInfo ci, @Local ClientboundCustomPayloadPacket customPayloadPacket) {
		CustomPacketPayload.Type<? extends CustomPacketPayload> type = customPayloadPacket.payload().type();
		if (PacketRegistry.CLIENT_PACKET_HANDLERS.containsKey(type))
			ci.cancel();
	}

	@Inject(
			method = "checkPacket(Lnet/minecraft/network/protocol/Packet;Lnet/minecraft/network/protocol/common/ClientCommonPacketListener;)V",
			at = @At(value = "INVOKE", target = "Ljava/lang/UnsupportedOperationException;<init>(Ljava/lang/String;)V"),
			cancellable = true
	)
	private static void checkServerPacket(Packet<?> packet, ClientCommonPacketListener listener, CallbackInfo ci, @Local ServerboundCustomPayloadPacket customPayloadPacket) {
		CustomPacketPayload.Type<? extends CustomPacketPayload> type = customPayloadPacket.payload().type();
		if (PacketRegistry.SERVER_PACKET_HANDLERS.containsKey(type))
			ci.cancel();
	}

	@Inject(
			method = "handleModdedPayload(Lnet/minecraft/network/protocol/common/ServerCommonPacketListener;Lnet/minecraft/network/protocol/common/ServerboundCustomPayloadPacket;)V",
			at = @At("HEAD"),
			cancellable = true
	)
	private static void handleClientPayload(ServerCommonPacketListener listener, ServerboundCustomPayloadPacket packet, CallbackInfo ci) {
		CustomPacketPayload payload = packet.payload();
		@SuppressWarnings("unchecked")
		PacketHandler<CustomPacketPayload> packetHandler = (PacketHandler<CustomPacketPayload>) PacketRegistry.CLIENT_PACKET_HANDLERS.get(payload.type());
		if (packetHandler == null) return;
		packetHandler.handle(payload);
		ci.cancel();
	}

	@Inject(
			method = "handleModdedPayload(Lnet/minecraft/network/protocol/common/ClientCommonPacketListener;Lnet/minecraft/network/protocol/common/ClientboundCustomPayloadPacket;)V",
			at = @At("HEAD"),
			cancellable = true
	)
	private static void handleServerPayload(ClientCommonPacketListener listener, ClientboundCustomPayloadPacket packet, CallbackInfo ci) {
		CustomPacketPayload payload = packet.payload();
		@SuppressWarnings("unchecked")
		PacketHandler<CustomPacketPayload> packetHandler = (PacketHandler<CustomPacketPayload>) PacketRegistry.SERVER_PACKET_HANDLERS.get(payload.type());
		if (packetHandler == null) return;
		packetHandler.handle(payload);
		ci.cancel();
	}
}
