/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.test

import dev.pandasystems.pandalib.api.event.addEventListener
import dev.pandasystems.pandalib.api.event.commonevents.PlayerJoinEvent
import dev.pandasystems.pandalib.api.networking.registerPacketCodec
import dev.pandasystems.pandalib.api.networking.registerPacketHandler
import dev.pandasystems.pandalib.api.networking.sendPacketToPlayer
import dev.pandasystems.pandalib.api.networking.sendPacketToServer
import dev.pandasystems.pandalib.api.platform.game
import dev.pandasystems.pandalib.core.logger
import dev.pandasystems.pandalib.test.networking.HelloWorldPacket
import dev.pandasystems.pandalib.test.networking.helloWorldPacketCodec
import dev.pandasystems.pandalib.test.networking.helloWorldPacketType
import net.minecraft.server.level.ServerPlayer

object PandaLibTest {
	fun initializeCommonTest() {
		registerPacketCodec(helloWorldPacketType, helloWorldPacketCodec)
		registerPacketHandler(helloWorldPacketType) { payload -> logger.info("Received Hello World packet: $payload") }

		addEventListener<PlayerJoinEvent>(::onPlayerJoin)
	}

	private fun onPlayerJoin(event: PlayerJoinEvent) {
		if (game.isDedicatedServer) {
			if (event.player is ServerPlayer)
				sendPacketToPlayer(
					event.player,
					HelloWorldPacket(event.player.name.string),
					HelloWorldPacket("World"),
					HelloWorldPacket("from the server!")
				)
		} else {
			sendPacketToServer(
				HelloWorldPacket("Hello"),
				HelloWorldPacket("from the client!"),
				HelloWorldPacket("This is a test of the PandaLib networking system.")
			)
		}
	}
}