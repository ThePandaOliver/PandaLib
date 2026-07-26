package dev.pandasystems.pandalib.networking

import java.util.UUID

interface PeerDirectory {
    fun getPeer(id: String): NetworkPeer?
    fun addPeer(peer: NetworkPeer)
    fun removePeer(id: String)
}