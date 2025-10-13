/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.networking.payloads.config

import com.google.gson.JsonObject
import dev.pandasystems.pandalib.utils.codec.StreamCodec
import dev.pandasystems.pandalib.utils.codecs.JsonObjectCodec
import dev.pandasystems.pandalib.utils.codecs.OptionalCodec
import dev.pandasystems.pandalib.utils.codecs.ResourceLocationCodec
import dev.pandasystems.pandalib.utils.codecs.UUIDCodec
import dev.pandasystems.pandalib.utils.extensions.resourceLocation
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.resources.ResourceLocation
import java.util.*

data class CommonConfigPayload(
	val resourceLocation: ResourceLocation,
	val optionObject: JsonObject,
	val playerId: Optional<UUID>
) : CustomPacketPayload {
	override fun write(buffer: FriendlyByteBuf) {
		ResourceLocationCodec.encode(buffer, resourceLocation)
		JsonObjectCodec.encode(buffer, optionObject)
		OptionalCodec(UUIDCodec).encode(buffer, playerId)
	}

	override fun id(): ResourceLocation = RESOURCELOCATION

	companion object {
		val RESOURCELOCATION = resourceLocation("config_payload")
		val CODEC: StreamCodec<FriendlyByteBuf, CommonConfigPayload> = StreamCodec.composite(
			ResourceLocationCodec, CommonConfigPayload::resourceLocation,
			JsonObjectCodec, CommonConfigPayload::optionObject,
			OptionalCodec(UUIDCodec), CommonConfigPayload::playerId,
			::CommonConfigPayload
		)
	}
}