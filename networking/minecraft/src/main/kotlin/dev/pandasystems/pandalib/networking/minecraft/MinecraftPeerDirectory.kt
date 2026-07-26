package dev.pandasystems.pandalib.networking.minecraft

import dev.pandasystems.pandalib.networking.NetworkPeer
import dev.pandasystems.pandalib.networking.PeerDirectory

class MinecraftPeerDirectory : PeerDirectory {
    private val peers: MutableMap<String, MinecraftNetworkPeer> = mutableMapOf()

    override fun getPeer(id: String): NetworkPeer? {
        return peers[id]
    }

    override fun addPeer(peer: NetworkPeer) {
        require(peer is MinecraftNetworkPeer) {
            "MinecraftPeerDirectory only accepts MinecraftNetworkPeer instances."
        }
        peers[peer.id] = peer
    }

    override fun removePeer(id: String) {
        peers.remove(id)
    }

    fun all(): Collection<MinecraftNetworkPeer> = peers.values.toList()
}
