package dev.pandasystems.pandalib.neoforge.networking

import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.resources.Identifier

internal class NeoForgePacketPayload(
    private val payloadType: CustomPacketPayload.Type<NeoForgePacketPayload>,
    val data: ByteArray,
) : CustomPacketPayload {
    init {
        require(data.size <= MAX_DATA_SIZE) {
            "Packet payloads cannot exceed $MAX_DATA_SIZE bytes."
        }
    }

    override fun type(): CustomPacketPayload.Type<NeoForgePacketPayload> = payloadType

    companion object {
        const val MAX_DATA_SIZE: Int = 1_048_576

        fun type(id: Identifier): CustomPacketPayload.Type<NeoForgePacketPayload> =
            CustomPacketPayload.Type(id)

        fun codec(
            payloadType: CustomPacketPayload.Type<NeoForgePacketPayload>,
        ): StreamCodec<RegistryFriendlyByteBuf, NeoForgePacketPayload> = StreamCodec.of(
            { buffer, payload -> buffer.writeByteArray(payload.data) },
            { buffer -> NeoForgePacketPayload(payloadType, buffer.readByteArray(MAX_DATA_SIZE)) },
        )
    }
}
