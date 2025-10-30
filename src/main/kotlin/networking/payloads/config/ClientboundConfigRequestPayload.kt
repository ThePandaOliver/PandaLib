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
import dev.pandasystems.pandalib.utils.codecs.StreamCodec
import dev.pandasystems.pandalib.utils.codecs.UUIDCodec
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.resources.ResourceLocation
import java.util.*

class ClientboundConfigRequestPayload(val playerId: UUID) : CustomPacketPayload {
	override fun write(buffer: FriendlyByteBuf) {
		UUIDCodec.encode(buffer, playerId)
	}

	override fun id(): ResourceLocation = RESOURCELOCATION

	companion object {
		val RESOURCELOCATION = PandaLib.resourceLocation("config_request_payload")
		val CODEC: StreamCodec<FriendlyByteBuf, ClientboundConfigRequestPayload> = StreamCodec.composite(
			UUIDCodec, ClientboundConfigRequestPayload::playerId,
			::ClientboundConfigRequestPayload
		)
	}
}