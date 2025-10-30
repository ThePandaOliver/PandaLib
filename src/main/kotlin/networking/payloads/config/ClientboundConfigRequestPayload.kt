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
import net.minecraft.core.UUIDUtil
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import java.util.*

class ClientboundConfigRequestPayload(val playerId: UUID) : CustomPacketPayload {
	override fun type(): CustomPacketPayload.Type<ClientboundConfigRequestPayload> = TYPE

	companion object {
		val TYPE = CustomPacketPayload.Type<ClientboundConfigRequestPayload>(PandaLib.resourceLocation("config_request_payload"))
		val CODEC: StreamCodec<FriendlyByteBuf, ClientboundConfigRequestPayload> = StreamCodec.composite(
			UUIDUtil.STREAM_CODEC, ClientboundConfigRequestPayload::playerId,
			::ClientboundConfigRequestPayload
		)
	}
}