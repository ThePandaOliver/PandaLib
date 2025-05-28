package dev.pandasystems.pandalib.impl.networking

import dev.architectury.utils.Env
import net.minecraft.world.entity.player.Player

class NetworkContext(
	/**
	 * @return The Sender if this was sent to the server or the receiver if this was sent to a client
	 */
	val player: Player,
	/**
	 * @return the environment direction the packet was send.
	 */
	val direction: Env
)
