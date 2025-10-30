/*
 * Copyright (C) 2025-2025 Oliver Froberg (The Panda Oliver)
 *
 * This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 * You should have received a copy of the GNU Lesser General Public License
 *  along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package dev.pandasystems.pandalib.networking

import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.resources.ResourceLocation

object PayloadCodecRegistry {
	internal val packetCodecs = mutableMapOf<ResourceLocation, CustomPacketPayload.TypeAndCodec<FriendlyByteBuf, CustomPacketPayload>>()

	fun <T : CustomPacketPayload> register(type: CustomPacketPayload.Type<T>, codec: StreamCodec<FriendlyByteBuf, T>) {
		require(!packetCodecs.containsKey(type.id)) { "Packet type $type already has a codec" }
		@Suppress("UNCHECKED_CAST")
		packetCodecs[type.id] = CustomPacketPayload.TypeAndCodec(type, codec) as CustomPacketPayload.TypeAndCodec<FriendlyByteBuf, CustomPacketPayload>
	}
}