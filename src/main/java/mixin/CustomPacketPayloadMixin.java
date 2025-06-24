/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.pandasystems.pandalib.core.interfaces.CustomPayloadTypeProvider;
import dev.pandasystems.pandalib.core.interfaces.PandaLibCustomPayloadPacketCodec;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;

@Mixin(targets = "net/minecraft/network/protocol/common/custom/CustomPacketPayload$1")
public abstract class CustomPacketPayloadMixin<B extends FriendlyByteBuf> implements StreamCodec<B, CustomPacketPayload>, PandaLibCustomPayloadPacketCodec<B> {
	@Unique
	private CustomPayloadTypeProvider<B> pandaLib$customPayloadTypeProvider;

	@Override
	public void pandalibSetPacketCodecProvider(@NotNull CustomPayloadTypeProvider<B> customPayloadTypeProvider) {
		if (this.pandaLib$customPayloadTypeProvider != null) {
			throw new IllegalStateException("Payload codec provider is already set!");
		}

		this.pandaLib$customPayloadTypeProvider = customPayloadTypeProvider;
	}

	@WrapOperation(method = {
			"writeCap",
			"decode(Lnet/minecraft/network/FriendlyByteBuf;)Lnet/minecraft/network/protocol/common/custom/CustomPacketPayload;"
	}, at = @At(value = "INVOKE", target = "Lnet/minecraft/network/protocol/common/custom/CustomPacketPayload$1;findCodec(Lnet/minecraft/resources/ResourceLocation;)Lnet/minecraft/network/codec/StreamCodec;"))
	private StreamCodec<B, ? extends CustomPacketPayload> wrapFindCodec(@Coerce StreamCodec<B, CustomPacketPayload> instance, ResourceLocation resourceLocation, Operation<StreamCodec<B, CustomPacketPayload>> original, B byteBuf) {
		if (pandaLib$customPayloadTypeProvider != null) {
			CustomPacketPayload.TypeAndCodec<B, ? extends CustomPacketPayload> payloadType = pandaLib$customPayloadTypeProvider.get(byteBuf, resourceLocation);

			if (payloadType != null) {
				return payloadType.codec();
			}
		}

		return original.call(instance, resourceLocation);
	}
}
