/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.networking.payloads.config

import com.google.gson.JsonObject
import dev.pandasystems.pandalib.utils.codecs.JsonObjectCodec
import dev.pandasystems.pandalib.utils.extensions.resourceLocation
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap
import net.minecraft.core.UUIDUtil
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.resources.ResourceLocation
import java.util.*

data class CommonConfigPayload(
	val resourceLocation: ResourceLocation,
	val optionObject: JsonObject,
	val playerId: Optional<UUID>
) : CustomPacketPayload {
	override fun type(): CustomPacketPayload.Type<CommonConfigPayload> = TYPE

	companion object {
		val TYPE = CustomPacketPayload.Type<CommonConfigPayload>(resourceLocation("config_payload"))
		val CODEC: StreamCodec<FriendlyByteBuf, CommonConfigPayload> = StreamCodec.composite(
			ResourceLocation.STREAM_CODEC, CommonConfigPayload::resourceLocation,
			JsonObjectCodec, CommonConfigPayload::optionObject,
			ByteBufCodecs.optional(UUIDUtil.STREAM_CODEC), CommonConfigPayload::playerId,
			::CommonConfigPayload
		)
	}
}