/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.networking.payloads.config

import dev.pandasystems.pandalib.utils.extensions.resourceLocation
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload

class CommonConfigRequestPayload() : CustomPacketPayload {
	override fun type(): CustomPacketPayload.Type<CommonConfigRequestPayload> = TYPE

	companion object {
		val TYPE = CustomPacketPayload.Type<CommonConfigRequestPayload>(resourceLocation("config_request_payload"))
		val CODEC: StreamCodec<FriendlyByteBuf, CommonConfigRequestPayload> = StreamCodec.of({ byteBuf, payload -> }, { byteBuf -> CommonConfigRequestPayload() })
	}
}