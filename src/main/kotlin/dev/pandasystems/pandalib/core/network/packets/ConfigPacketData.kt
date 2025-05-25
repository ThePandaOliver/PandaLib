/*
 * Copyright (C) 2024 Oliver Froberg (The Panda Oliver)
 *
 * This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 * You should have received a copy of the GNU Lesser General Public License
 *  along with this program. If not, see <https://www.gnu.org/licenses/>.
 */
package dev.pandasystems.pandalib.core.network.packets

import com.google.gson.JsonElement
import dev.pandasystems.pandalib.PandaLib.resourceLocation
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.resources.ResourceLocation

@JvmRecord
data class ConfigPacketData(val resourceLocation: ResourceLocation, val data: JsonElement) : CustomPacketPayload {
	override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> {
		return TYPE
	}

	companion object {
		@JvmField
		val TYPE: CustomPacketPayload.Type<ConfigPacketData> = CustomPacketPayload.Type<ConfigPacketData>(resourceLocation("config_sync"))
	}
}