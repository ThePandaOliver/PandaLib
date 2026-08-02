package dev.pandasystems.pandalib.fabric

import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.resources.Identifier

internal class FabricPacketPayload(
    private val payloadType: CustomPacketPayload.Type<FabricPacketPayload>,
    val data: ByteArray,
) : CustomPacketPayload {
    init {
        require(data.size <= MAX_DATA_SIZE) {
            "Packet payloads cannot exceed $MAX_DATA_SIZE bytes."
        }
    }

    override fun type(): CustomPacketPayload.Type<FabricPacketPayload> = payloadType

    companion object {
        const val MAX_DATA_SIZE: Int = 1_048_576

        fun type(id: Identifier): CustomPacketPayload.Type<FabricPacketPayload> =
            CustomPacketPayload.Type(id)

        fun codec(
            payloadType: CustomPacketPayload.Type<FabricPacketPayload>,
        ): StreamCodec<RegistryFriendlyByteBuf, FabricPacketPayload> = StreamCodec.of(
            { buffer, payload -> buffer.writeByteArray(payload.data) },
            { buffer -> FabricPacketPayload(payloadType, buffer.readByteArray(MAX_DATA_SIZE)) },
        )
    }
}
