/*
 * Copyright (C) 2025 Oliver Froberg (The Panda Oliver)
 *
 * This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 * You should have received a copy of the GNU Lesser General Public License
 *  along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package dev.pandasystems.pandalib.networking.payloads.config

import dev.pandasystems.pandalib.PandaLib
import dev.pandasystems.pandalib.utils.codec.StreamCodec
import dev.pandasystems.pandalib.utils.codecs.OptionalCodec
import dev.pandasystems.pandalib.utils.codecs.ResourceLocationCodec
import dev.pandasystems.pandalib.utils.codecs.TreeObjectCodec
import dev.pandasystems.pandalib.utils.codecs.UUIDCodec
import dev.pandasystems.pandalib.utils.extensions.resourceLocation
import dev.pandasystems.universalserializer.elements.TreeObject
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.resources.ResourceLocation
import java.util.*

data class CommonConfigPayload(
	val resourceLocation: ResourceLocation,
	val optionObject: TreeObject,
	val playerId: Optional<UUID>
) : CustomPacketPayload {
	override fun write(buffer: FriendlyByteBuf) {
		ResourceLocationCodec.encode(buffer, resourceLocation)
		TreeObjectCodec.encode(buffer, optionObject)
		OptionalCodec(UUIDCodec).encode(buffer, playerId)
	}

	override fun id(): ResourceLocation = RESOURCELOCATION

	companion object {
		val RESOURCELOCATION = PandaLib.resourceLocation("config_payload")
		val CODEC: StreamCodec<FriendlyByteBuf, CommonConfigPayload> = StreamCodec.composite(
			ResourceLocationCodec, CommonConfigPayload::resourceLocation,
			TreeObjectCodec, CommonConfigPayload::optionObject,
			OptionalCodec(UUIDCodec), CommonConfigPayload::playerId,
			::CommonConfigPayload
		)
	}
}