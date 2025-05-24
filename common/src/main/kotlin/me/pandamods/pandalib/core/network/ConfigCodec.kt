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
package me.pandamods.pandalib.core.network

import me.pandamods.pandalib.core.network.packets.ConfigPacketData
import me.pandamods.pandalib.utils.NBTUtils
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec

class ConfigCodec : StreamCodec<RegistryFriendlyByteBuf, ConfigPacketData> {
	override fun decode(byteBuf: RegistryFriendlyByteBuf): ConfigPacketData {
		return ConfigPacketData(byteBuf.readResourceLocation(), NBTUtils.convertTagToJson(byteBuf.readNbt())!!)
	}

	override fun encode(byteBuf: RegistryFriendlyByteBuf, packetData: ConfigPacketData) {
		byteBuf.writeResourceLocation(packetData.resourceLocation)
		byteBuf.writeNbt(NBTUtils.convertJsonToTag(packetData.data))
	}

	companion object {
		val INSTANCE: ConfigCodec = ConfigCodec()
	}
}