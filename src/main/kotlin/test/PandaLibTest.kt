/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.test

import dev.architectury.event.events.common.PlayerEvent
import dev.pandasystems.pandalib.api.networking.registerPlayPacketHandler
import dev.pandasystems.pandalib.api.networking.sendPacketToPlayer
import dev.pandasystems.pandalib.api.platform.game
import dev.pandasystems.pandalib.core.logger
import dev.pandasystems.pandalib.test.networking.HelloWorldPacket
import dev.pandasystems.pandalib.test.networking.helloWorldPacketCodec
import dev.pandasystems.pandalib.test.networking.helloWorldPacketType

object PandaLibTest {
	fun initializeCommonTest() {
		registerPlayPacketHandler(helloWorldPacketType, helloWorldPacketCodec) { payload -> logger.info("Received Hello World packet: $payload") }
		if (game.isServer) {
			PlayerEvent.PLAYER_JOIN.register { player -> sendPacketToPlayer(player, HelloWorldPacket(player.name.string)) }
		}
	}
}