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
import net.minecraft.client.multiplayer.ClientCommonPacketListenerImpl
import net.minecraft.network.protocol.*
import net.minecraft.network.protocol.common.ClientCommonPacketListener

val clientboundPLBundleType = PacketType<ClientboundPLBundlePacket>(PacketFlow.CLIENTBOUND, PandaLib.resourceLocation("bundle"))

class ClientboundPLBundlePacket(iterable: Iterable<Packet<in ClientCommonPacketListener>>): BundlePacket<ClientCommonPacketListener>(iterable) {
	override fun type(): PacketType<ClientboundPLBundlePacket> {
		return clientboundPLBundleType
	}

	override fun handle(listener: ClientCommonPacketListener) {
		listener.handlePandalibBundlePacket(this)
	}
}

fun ClientCommonPacketListener.handlePandalibBundlePacket(packet: ClientboundPLBundlePacket) {
	if (this is ClientCommonPacketListenerImpl) {
		PacketUtils.ensureRunningOnSameThread(packet, this, this.minecraft)

		for (subpacket in packet.subPackets()) {
			subpacket.handle(this)
		}
	}
}