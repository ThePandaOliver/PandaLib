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

val SERVERBOUND_BUNDLE = PacketType<ServerboundBundlePacket>(PacketFlow.SERVERBOUND, PandaLib.resourceLocation("bundle"))

class ServerboundBundlePacket(iterable: Iterable<Packet<in ServerGamePacketListener>>): BundlePacket<ServerGamePacketListener>(iterable) {
	override fun type(): PacketType<ServerboundBundlePacket> {
		return SERVERBOUND_BUNDLE
	}

	override fun handle(listener: ServerGamePacketListener) {
		listener.handleBundlePacket(this)
	}
}

fun ServerGamePacketListener.handleBundlePacket(packet: ServerboundBundlePacket) {
	if (this is ServerCommonPacketListenerImpl) {
		PacketUtils.ensureRunningOnSameThread(packet, this, this.server)

		for (subpacket in packet.subPackets()) {
			subpacket.handle(this)
		}
	}
}