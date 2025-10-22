/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */
package dev.pandasystems.pandalib.networking.packets.bundle

import dev.pandasystems.pandalib.utils.extensions.resourceLocation
import net.minecraft.network.protocol.BundleDelimiterPacket
import net.minecraft.network.protocol.PacketFlow
import net.minecraft.network.protocol.PacketType
import net.minecraft.network.protocol.common.ServerCommonPacketListener
import net.minecraft.network.protocol.game.ServerGamePacketListener

val serverboundPLBundleDelimiterType = PacketType<ServerboundPLBundleDelimiterPacket>(PacketFlow.SERVERBOUND, resourceLocation("bundle_delimiter"))

class ServerboundPLBundleDelimiterPacket : BundleDelimiterPacket<ServerCommonPacketListener>() {
	override fun type(): PacketType<ServerboundPLBundleDelimiterPacket> {
		return serverboundPLBundleDelimiterType
	}
}

