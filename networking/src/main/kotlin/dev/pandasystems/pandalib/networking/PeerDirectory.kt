package dev.pandasystems.pandalib.networking

interface PeerDirectory {
    fun getPeer(id: String): NetworkPeer?
    fun addPeer(peer: NetworkPeer)
    fun removePeer(id: String)
}