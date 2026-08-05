package dev.pandasystems.pandalib.networking.payload

import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.resources.Identifier

internal class PacketPayload(
    private val payloadType: CustomPacketPayload.Type<PacketPayload>,
    val data: ByteArray,
) : CustomPacketPayload {
    init {
        require(data.size <= MAX_DATA_SIZE) {
            "Packet payloads cannot exceed $MAX_DATA_SIZE bytes."
        }
    }

    override fun type(): CustomPacketPayload.Type<PacketPayload> = payloadType

    companion object {
        const val MAX_DATA_SIZE: Int = 1_048_576

        fun type(id: Identifier): CustomPacketPayload.Type<PacketPayload> =
            CustomPacketPayload.Type(id)

        fun codec(
            payloadType: CustomPacketPayload.Type<PacketPayload>,
        ): StreamCodec<RegistryFriendlyByteBuf, PacketPayload> = StreamCodec.of(
            { buffer, payload -> buffer.writeByteArray(payload.data) },
            { buffer -> PacketPayload(payloadType, buffer.readByteArray(MAX_DATA_SIZE)) },
        )
    }
}
