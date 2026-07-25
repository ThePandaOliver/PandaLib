package dev.pandasystems.pandalib.networking

@JvmInline
value class PacketId(val value: String) {
    init {
        require(value.isNotEmpty()) { "Packet ID must not be blank." }
    }
}