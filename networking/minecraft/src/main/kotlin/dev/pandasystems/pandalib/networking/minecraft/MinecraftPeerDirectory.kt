package dev.pandasystems.pandalib.networking.minecraft

import dev.pandasystems.pandalib.networking.NetworkPeer
import dev.pandasystems.pandalib.networking.PeerDirectory

class MinecraftPeerDirectory : PeerDirectory {
    val peers: MutableMap<String, MinecraftNetworkPeer> = mutableMapOf()

    override fun getPeer(id: String): NetworkPeer? {
        return peers[id]
    }

    override fun addPeer(peer: NetworkPeer) {
        peers[peer.id] = peer as MinecraftNetworkPeer
    }

    override fun removePeer(id: String) {
        peers.remove(id)
    }
}