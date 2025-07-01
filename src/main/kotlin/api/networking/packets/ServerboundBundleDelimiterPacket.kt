/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */
package dev.pandasystems.pandalib.api.networking.packets

import dev.pandasystems.pandalib.core.PandaLib
import net.minecraft.network.protocol.BundleDelimiterPacket
import net.minecraft.network.protocol.PacketFlow
import net.minecraft.network.protocol.PacketType
import net.minecraft.network.protocol.game.ServerGamePacketListener

val SERVERBOUND_BUNDLE_DELIMITER_TYPE = PacketType<ServerboundBundleDelimiterPacket>(PacketFlow.SERVERBOUND, PandaLib.resourceLocation("bundle_delimiter"))

class ServerboundBundleDelimiterPacket : BundleDelimiterPacket<ServerGamePacketListener>() {
	override fun type(): PacketType<ServerboundBundleDelimiterPacket> {
		return SERVERBOUND_BUNDLE_DELIMITER_TYPE
	}
}

