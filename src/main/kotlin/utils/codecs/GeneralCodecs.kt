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

package dev.pandasystems.pandalib.utils.codecs

import net.minecraft.network.FriendlyByteBuf
import net.minecraft.resources.ResourceLocation
import java.util.*

object ResourceLocationCodec: StreamCodec<FriendlyByteBuf, ResourceLocation> {
	override fun decode(byteBuf: FriendlyByteBuf): ResourceLocation {
		return byteBuf.readResourceLocation()
	}

	override fun encode(byteBuf: FriendlyByteBuf, resourceLocation: ResourceLocation) {
		byteBuf.writeResourceLocation(resourceLocation)
	}
}

object UUIDCodec: StreamCodec<FriendlyByteBuf, UUID> {
	override fun decode(byteBuf: FriendlyByteBuf): UUID {
		return UUID(byteBuf.readLong(), byteBuf.readLong())
	}

	override fun encode(byteBuf: FriendlyByteBuf, uuid: UUID) {
		byteBuf.writeLong(uuid.mostSignificantBits)
		byteBuf.writeLong(uuid.leastSignificantBits)
	}
}

class OptionalCodec<T : Any>(private val codec: StreamCodec<FriendlyByteBuf, T>): StreamCodec<FriendlyByteBuf, Optional<T>> {
	override fun decode(byteBuf: FriendlyByteBuf): Optional<T> {
		if (byteBuf.readBoolean()) {
			return Optional.ofNullable(codec.decode(byteBuf))
		}
		return Optional.empty()
	}

	override fun encode(byteBuf: FriendlyByteBuf, optional: Optional<T>) {
		optional.isPresent.let(byteBuf::writeBoolean)
		optional.ifPresent { codec.encode(byteBuf, it) }
	}
}