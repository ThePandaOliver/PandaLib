package dev.pandasystems.pandalib.networking

@JvmInline
value class PacketId(val value: String) {
    init {
        require(value.isNotBlank()) { "Packet ID must not be blank." }
    }

    override fun toString(): String = value

    companion object {
        /** Creates a conventional namespaced packet id, such as `examplemod:sync`. */
        fun of(namespace: String, path: String): PacketId {
            require(namespace.isNotBlank()) { "Packet namespace must not be blank." }
            require(path.isNotBlank()) { "Packet path must not be blank." }
            return PacketId("$namespace:$path")
        }
    }
}
