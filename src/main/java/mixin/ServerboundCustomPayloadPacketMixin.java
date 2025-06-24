/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.pandasystems.pandalib.api.networking.PacketRegistry;
import dev.pandasystems.pandalib.core.interfaces.PandaLibCustomPayloadPacketCodec;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
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
					target = "Lnet/minecraft/network/protocol/common/custom/CustomPacketPayload;codec(Lnet/minecraft/network/protocol/common/custom/CustomPacketPayload$FallbackProvider;Ljava/util/List;)Lnet/minecraft/network/codec/StreamCodec;"
			)
	)
	private static StreamCodec<FriendlyByteBuf, CustomPacketPayload> wrapCodec(
			CustomPacketPayload.FallbackProvider<FriendlyByteBuf> fallbackProvider,
			List<CustomPacketPayload.TypeAndCodec<? super FriendlyByteBuf, ?>> typeAndCodecs,
			Operation<StreamCodec<FriendlyByteBuf, CustomPacketPayload>> original
	) {
		StreamCodec<FriendlyByteBuf, CustomPacketPayload> codec = original.call(fallbackProvider, typeAndCodecs);
		PandaLibCustomPayloadPacketCodec<FriendlyByteBuf> pandalibCodec = (PandaLibCustomPayloadPacketCodec<FriendlyByteBuf>) codec;
		pandalibCodec.pandalibSetPacketCodecProvider((byteBuf, resourceLocation) -> {
			// CustomPayloadC2SPacket does not have a separate codec for play/configuration. We know if the packetByteBuf is a PacketByteBuf we are in the play phase.
			if (byteBuf instanceof RegistryFriendlyByteBuf) {
				return PacketRegistry.SERVER_PACKET_CODEC.get(resourceLocation);
			}

			return PacketRegistry.SERVER_PACKET_CODEC.get(resourceLocation);
		});
		return codec;
	}
}
