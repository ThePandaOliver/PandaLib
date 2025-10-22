/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.networking.packets.bundle

import dev.pandasystems.pandalib.networking.packets.ServerboundPLPayloadPacket
import net.minecraft.network.protocol.BundlePacket
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.PacketUtils
import net.minecraft.network.protocol.game.ServerGamePacketListener
import net.minecraft.server.network.ServerGamePacketListenerImpl

class ServerboundPLBundlePacket(iterable: Iterable<Packet<in ServerGamePacketListener>>): BundlePacket<ServerGamePacketListener>(iterable) {
	override fun handle(listener: ServerGamePacketListener) {
		listener.handlePandalibBundlePacket(this)
	}
}

fun ServerGamePacketListener.handlePandalibBundlePacket(packet: ServerboundPLBundlePacket) {
	if (this is ServerGamePacketListenerImpl) {
		PacketUtils.ensureRunningOnSameThread(packet, this, this.server)

		for (subpacket in packet.subPackets()) {
			subpacket.handle(this)
		}
	}
}