/*
 * Copyright (C) 2025-2025 Oliver Froberg (The Panda Oliver)
 *
 * This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 * You should have received a copy of the GNU Lesser General Public License
 *  along with this program. If not, see <https://www.gnu.org/licenses/>.
 */
package dev.pandasystems.pandalib.networking.packets.bundle

import dev.pandasystems.pandalib.utils.extensions.resourceLocation
import net.minecraft.network.protocol.BundleDelimiterPacket
import net.minecraft.network.protocol.PacketFlow
import net.minecraft.network.protocol.PacketType
import net.minecraft.network.protocol.common.ServerCommonPacketListener

val serverboundPLBundleDelimiterType = PacketType<ServerboundPLBundleDelimiterPacket>(PacketFlow.SERVERBOUND, resourceLocation("bundle_delimiter"))

class ServerboundPLBundleDelimiterPacket : BundleDelimiterPacket<ServerCommonPacketListener>() {
	override fun type(): PacketType<ServerboundPLBundleDelimiterPacket> {
		return serverboundPLBundleDelimiterType
	}
}

