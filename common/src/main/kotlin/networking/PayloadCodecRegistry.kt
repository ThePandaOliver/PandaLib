/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.networking

import dev.pandasystems.pandalib.utils.codec.StreamCodec
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.resources.ResourceLocation

object PayloadCodecRegistry {
	@JvmField
	internal val packetCodecs = mutableMapOf<ResourceLocation, StreamCodec<FriendlyByteBuf, CustomPacketPayload>>()

	@JvmStatic
	fun <T : CustomPacketPayload> register(resourceLocation: ResourceLocation, codec: StreamCodec<FriendlyByteBuf, T>) {
		require(!packetCodecs.containsKey(resourceLocation)) { "Packet type $resourceLocation already has a codec" }
		@Suppress("UNCHECKED_CAST")
		packetCodecs[resourceLocation] = codec as StreamCodec<FriendlyByteBuf, CustomPacketPayload>
	}
}