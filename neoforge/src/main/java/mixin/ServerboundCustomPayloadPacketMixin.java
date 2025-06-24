/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.neoforge.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.pandasystems.pandalib.api.networking.PacketRegistry;
import dev.pandasystems.pandalib.core.interfaces.PandaLibCustomPayloadPacketCodec;
import net.minecraft.network.ConnectionProtocol;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;

@Mixin(ServerboundCustomPayloadPacket.class)
public class ServerboundCustomPayloadPacketMixin {
	@WrapOperation(method = "<clinit>",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/network/protocol/common/custom/CustomPacketPayload;codec(Lnet/minecraft/network/protocol/common/custom/CustomPacketPayload$FallbackProvider;Ljava/util/List;Lnet/minecraft/network/ConnectionProtocol;Lnet/minecraft/network/protocol/PacketFlow;)Lnet/minecraft/network/codec/StreamCodec;",
					ordinal = 0
			)
	)
	private static StreamCodec<FriendlyByteBuf, CustomPacketPayload> wrapPlayCodec(
			CustomPacketPayload.FallbackProvider<FriendlyByteBuf> fallbackProvider,
			List<CustomPacketPayload.TypeAndCodec<? super FriendlyByteBuf, ?>> typeAndCodecs,
			ConnectionProtocol connectionProtocol, PacketFlow packetFlow, Operation<StreamCodec<FriendlyByteBuf, CustomPacketPayload>> original
	) {
		StreamCodec<FriendlyByteBuf, CustomPacketPayload> codec = original.call(fallbackProvider, typeAndCodecs, connectionProtocol, packetFlow);
		PandaLibCustomPayloadPacketCodec<FriendlyByteBuf> pandalibCodec = (PandaLibCustomPayloadPacketCodec<FriendlyByteBuf>) codec;
		pandalibCodec.pandalibSetPacketCodecProvider((byteBuf, resourceLocation) -> PacketRegistry.SERVER_PACKET_CODEC.get(resourceLocation));
		return codec;
	}

	@WrapOperation(method = "<clinit>",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/network/protocol/common/custom/CustomPacketPayload;codec(Lnet/minecraft/network/protocol/common/custom/CustomPacketPayload$FallbackProvider;Ljava/util/List;Lnet/minecraft/network/ConnectionProtocol;Lnet/minecraft/network/protocol/PacketFlow;)Lnet/minecraft/network/codec/StreamCodec;",
					ordinal = 1
			)
	)
	private static StreamCodec<FriendlyByteBuf, CustomPacketPayload> wrapConfigCodec(
			CustomPacketPayload.FallbackProvider<FriendlyByteBuf> fallbackProvider,
			List<CustomPacketPayload.TypeAndCodec<? super FriendlyByteBuf, ?>> typeAndCodecs,
			ConnectionProtocol connectionProtocol, PacketFlow packetFlow, Operation<StreamCodec<FriendlyByteBuf, CustomPacketPayload>> original
	) {
		StreamCodec<FriendlyByteBuf, CustomPacketPayload> codec = original.call(fallbackProvider, typeAndCodecs, connectionProtocol, packetFlow);
		PandaLibCustomPayloadPacketCodec<FriendlyByteBuf> pandalibCodec = (PandaLibCustomPayloadPacketCodec<FriendlyByteBuf>) codec;
		pandalibCodec.pandalibSetPacketCodecProvider((byteBuf, resourceLocation) -> PacketRegistry.SERVER_PACKET_CODEC.get(resourceLocation));
		return codec;
	}
}
