package dev.pandasystems.pandalib.networking.minecraft.fabric

import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.resources.Identifier

class FabricPacketPayload(
    val id: Identifier,
    val data: ByteArray
) : CustomPacketPayload {
    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> =
        CustomPacketPayload.Type<FabricPacketPayload>(Identifier.fromNamespaceAndPath("pandalib", "packet"))
}