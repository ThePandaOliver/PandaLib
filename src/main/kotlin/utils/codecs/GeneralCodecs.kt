/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.utils.codecs

import dev.pandasystems.pandalib.utils.codec.StreamCodec
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.resources.ResourceLocation
import java.util.Optional
import java.util.UUID

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