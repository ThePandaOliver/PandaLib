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

import net.minecraft.client.multiplayer.ClientPacketListener
import net.minecraft.network.protocol.BundlePacket
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.PacketUtils

class ClientboundPLBundlePacket(iterable: Iterable<Packet<ClientPacketListener>>) : BundlePacket<ClientPacketListener>(iterable) {
	override fun handle(listener: ClientPacketListener) {
		listener.handlePandalibBundlePacket(this)
	}
}

fun ClientPacketListener.handlePandalibBundlePacket(packet: ClientboundPLBundlePacket) {
	PacketUtils.ensureRunningOnSameThread(packet, this, this.minecraft)

	for (subpacket in packet.subPackets()) {
		subpacket.handle(this)
	}
}