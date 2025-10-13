/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.networking.packets.bundle

import dev.pandasystems.pandalib.utils.extensions.resourceLocation
import net.minecraft.network.protocol.*
import net.minecraft.network.protocol.game.ServerGamePacketListener
import net.minecraft.server.network.ServerCommonPacketListenerImpl

val serverboundPLBundleType = PacketType<ServerboundPLBundlePacket>(PacketFlow.SERVERBOUND, resourceLocation("bundle"))

class ServerboundPLBundlePacket(iterable: Iterable<Packet<in ServerGamePacketListener>>): BundlePacket<ServerGamePacketListener>(iterable) {
	override fun type(): PacketType<ServerboundPLBundlePacket> {
		return serverboundPLBundleType
	}

	override fun handle(listener: ServerGamePacketListener) {
		listener.handlePandalibBundlePacket(this)
	}
}

fun ServerGamePacketListener.handlePandalibBundlePacket(packet: ServerboundPLBundlePacket) {
	if (this is ServerCommonPacketListenerImpl) {
		PacketUtils.ensureRunningOnSameThread(packet, this, this.server)

		for (subpacket in packet.subPackets()) {
			subpacket.handle(this)
		}
	}
}