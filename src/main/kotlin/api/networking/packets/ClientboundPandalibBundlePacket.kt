/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.api.networking.packets

import dev.pandasystems.pandalib.core.PandaLib
import net.minecraft.client.multiplayer.ClientCommonPacketListenerImpl
import net.minecraft.network.protocol.BundlePacket
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.PacketFlow
import net.minecraft.network.protocol.PacketType
import net.minecraft.network.protocol.PacketUtils
import net.minecraft.network.protocol.game.ClientGamePacketListener
import net.minecraft.network.protocol.game.ServerGamePacketListener
import net.minecraft.server.network.ServerCommonPacketListenerImpl

val CLIENTBOUND_PANDALIB_BUNDLE_TYPE = PacketType<ClientboundPandalibBundlePacket>(PacketFlow.CLIENTBOUND, PandaLib.resourceLocation("bundle"))

class ClientboundPandalibBundlePacket(iterable: Iterable<Packet<in ClientGamePacketListener>>): BundlePacket<ClientGamePacketListener>(iterable) {
	override fun type(): PacketType<ClientboundPandalibBundlePacket> {
		return CLIENTBOUND_PANDALIB_BUNDLE_TYPE
	}

	override fun handle(listener: ClientGamePacketListener) {
		listener.handlePandalibBundlePacket(this)
	}
}

fun ClientGamePacketListener.handlePandalibBundlePacket(packet: ClientboundPandalibBundlePacket) {
	if (this is ClientCommonPacketListenerImpl) {
		PacketUtils.ensureRunningOnSameThread(packet, this, this.minecraft)

		for (subpacket in packet.subPackets()) {
			subpacket.handle(this)
		}
	}
}