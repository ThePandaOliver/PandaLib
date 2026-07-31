package dev.pandasystems.pandalib.networking

@JvmInline
value class PacketId(val value: String) {
    init {
        require(value.isNotBlank()) { "Packet ID must not be blank." }
    }

    override fun toString(): String = value
}
