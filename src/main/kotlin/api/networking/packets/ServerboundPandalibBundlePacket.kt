/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.api.networking.packets

import dev.pandasystems.pandalib.core.PandaLib
import net.minecraft.network.protocol.BundlePacket
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.PacketFlow
import net.minecraft.network.protocol.PacketType
import net.minecraft.network.protocol.PacketUtils
import net.minecraft.network.protocol.game.ServerGamePacketListener
import net.minecraft.server.network.ServerCommonPacketListenerImpl

val SERVERBOUND_PANDALIB_BUNDLE_TYPE = PacketType<ServerboundPandalibBundlePacket>(PacketFlow.SERVERBOUND, PandaLib.resourceLocation("bundle"))

class ServerboundPandalibBundlePacket(iterable: Iterable<Packet<in ServerGamePacketListener>>): BundlePacket<ServerGamePacketListener>(iterable) {
	override fun type(): PacketType<ServerboundPandalibBundlePacket> {
		return SERVERBOUND_PANDALIB_BUNDLE_TYPE
	}

	override fun handle(listener: ServerGamePacketListener) {
		listener.handlePandalibBundlePacket(this)
	}
}

fun ServerGamePacketListener.handlePandalibBundlePacket(packet: ServerboundPandalibBundlePacket) {
	if (this is ServerCommonPacketListenerImpl) {
		PacketUtils.ensureRunningOnSameThread(packet, this, this.server)

		for (subpacket in packet.subPackets()) {
			subpacket.handle(this)
		}
	}
}