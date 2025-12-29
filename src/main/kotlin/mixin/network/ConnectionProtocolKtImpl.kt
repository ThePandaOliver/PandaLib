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

package dev.pandasystems.pandalib.mixin.network

import dev.pandasystems.pandalib.networking.packets.ClientboundPLPayloadPacket
import dev.pandasystems.pandalib.networking.packets.ServerboundPLPayloadPacket
import dev.pandasystems.pandalib.networking.packets.clientboundPLPayloadCodec
import dev.pandasystems.pandalib.networking.packets.serverboundPLPayloadCodec
import net.minecraft.network.ConnectionProtocol
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.PacketListener
import net.minecraft.network.protocol.PacketFlow
import net.minecraft.network.protocol.common.ClientCommonPacketListener
import net.minecraft.network.protocol.common.ServerCommonPacketListener

object ConnectionProtocolKtImpl {
	fun <T : PacketListener> addFlow(
		packetFlow: PacketFlow,
		packetSet: ConnectionProtocol.PacketSet<T>
	) {
		if (packetFlow == PacketFlow.CLIENTBOUND) {
			(packetSet as ConnectionProtocol.PacketSet<ClientCommonPacketListener>)
				.addPacket(ClientboundPLPayloadPacket::class.java) { obj: FriendlyByteBuf -> clientboundPLPayloadCodec.decode(obj) }
		} else if (packetFlow == PacketFlow.SERVERBOUND) {
			(packetSet as ConnectionProtocol.PacketSet<ServerCommonPacketListener>)
				.addPacket(ServerboundPLPayloadPacket::class.java) { obj: FriendlyByteBuf -> serverboundPLPayloadCodec.decode(obj) }
		}
	}
}