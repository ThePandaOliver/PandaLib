/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.networking

import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.resources.ResourceLocation
import kotlin.collections.containsKey

object PayloadCodecRegistry {
	@JvmField
	internal val packetCodecs = mutableMapOf<ResourceLocation, CustomPacketPayload.TypeAndCodec<FriendlyByteBuf, CustomPacketPayload>>()

	@JvmStatic
	fun <T : CustomPacketPayload> register(type: CustomPacketPayload.Type<T>, codec: StreamCodec<FriendlyByteBuf, T>) {
		require(!packetCodecs.containsKey(type.id)) { "Packet type $type already has a codec" }
		@Suppress("UNCHECKED_CAST")
		packetCodecs[type.id] = CustomPacketPayload.TypeAndCodec(type, codec) as CustomPacketPayload.TypeAndCodec<FriendlyByteBuf, CustomPacketPayload>
	}
}