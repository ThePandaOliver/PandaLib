package dev.pandasystems.pandalib.networking

@JvmInline
value class PacketId(val value: String) {
    init {
        require(value.isNotEmpty()) { "Packet ID must not be blank." }
    }
}

interface PacketCodec<T> {
    fun encode(value: T): ByteArray
    fun decode(bytes: ByteArray): T
}

interface NetworkPeer {
    val id: String
}

fun interface NetworkExecutor {
    fun execute(task: () -> Unit)
}