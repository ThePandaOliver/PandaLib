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

package dev.pandasystems.pandalib.mixin.network.protocol

import dev.pandasystems.pandalib.networking.packets.clientboundPLPayloadCodec
import dev.pandasystems.pandalib.networking.packets.clientboundPLPayloadPacketType
import dev.pandasystems.pandalib.networking.packets.serverboundPLPayloadCodec
import dev.pandasystems.pandalib.networking.packets.serverboundPLPayloadPacketType
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.protocol.ProtocolInfoBuilder
import net.minecraft.network.protocol.game.ClientGamePacketListener
import net.minecraft.network.protocol.game.ServerGamePacketListener

object ConfigurationProtocolsKtImpl {
	fun addClientPacket(protocolInfoBuilder: ProtocolInfoBuilder<ClientGamePacketListener, RegistryFriendlyByteBuf>) {
		protocolInfoBuilder.addPacket(
			clientboundPLPayloadPacketType,
			clientboundPLPayloadCodec
		)
	}
	
	fun addServerPacket(protocolInfoBuilder: ProtocolInfoBuilder<ServerGamePacketListener, RegistryFriendlyByteBuf>) {
		protocolInfoBuilder.addPacket(
			serverboundPLPayloadPacketType,
			serverboundPLPayloadCodec
		)
	}
}