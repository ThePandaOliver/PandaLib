/*
 * Copyright (C) 2025 Oliver Froberg (The Panda Oliver)
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

import dev.pandasystems.pandalib.PandaLib
import net.minecraft.network.protocol.*
import net.minecraft.network.protocol.common.ServerCommonPacketListener
import net.minecraft.server.network.ServerCommonPacketListenerImpl

val serverboundPLBundleType = PacketType<ServerboundPLBundlePacket>(PacketFlow.SERVERBOUND, PandaLib.resourceLocation("bundle"))

class ServerboundPLBundlePacket(iterable: Iterable<Packet<in ServerCommonPacketListener>>): BundlePacket<ServerCommonPacketListener>(iterable) {
	override fun type(): PacketType<ServerboundPLBundlePacket> {
		return serverboundPLBundleType
	}

	override fun handle(listener: ServerCommonPacketListener) {
		listener.handlePandalibBundlePacket(this)
	}
}

fun ServerCommonPacketListener.handlePandalibBundlePacket(packet: ServerboundPLBundlePacket) {
	if (this is ServerCommonPacketListenerImpl) {
		PacketUtils.ensureRunningOnSameThread(packet, this, this.server)

		for (subpacket in packet.subPackets()) {
			subpacket.handle(this)
		}
	}
}