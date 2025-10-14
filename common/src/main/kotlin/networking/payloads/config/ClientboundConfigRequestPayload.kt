/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.networking.payloads.config

import dev.pandasystems.pandalib.networking.CustomPacketPayload
import dev.pandasystems.pandalib.utils.codec.StreamCodec
import dev.pandasystems.pandalib.utils.codecs.UUIDCodec
import dev.pandasystems.pandalib.utils.extensions.resourceLocation
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.resources.ResourceLocation
import java.util.*

class ClientboundConfigRequestPayload(val playerId: UUID) : CustomPacketPayload {
	override fun write(buffer: FriendlyByteBuf) {
		UUIDCodec.encode(buffer, playerId)
	}

	override fun id(): ResourceLocation = RESOURCELOCATION

	companion object {
		val RESOURCELOCATION = resourceLocation("config_request_payload")
		val CODEC: StreamCodec<FriendlyByteBuf, ClientboundConfigRequestPayload> = StreamCodec.composite(
			UUIDCodec, ClientboundConfigRequestPayload::playerId,
			::ClientboundConfigRequestPayload
		)
	}
}