/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.client

import dev.pandasystems.pandalib.config.ConfigSynchronizer
import dev.pandasystems.pandalib.event.client.clientPlayerJoinEvent
import dev.pandasystems.pandalib.event.client.clientPlayerLeaveEvent
import dev.pandasystems.pandalib.pandalibConfig
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.collections.set

object PandaLibClient {
	init {
		clientPlayerJoinEvent += { player ->
			println("Client value: ${pandalibConfig.get().debugging.serverValue}")
			println("Player ${player.name} value: ${pandalibConfig.get().debugging.playerValues[player.uuid]}")
		}
	}

	fun configSynchronizerClientInit() {
		// Adding players configs to player configs
		clientPlayerJoinEvent += { player ->
			ConfigSynchronizer.configs.forEach { (_, options) ->
				options.forEach { option ->
					option.playerValues[player.uuid] = option.initialValue
				}
			}
		}

		// Player leave cleanup
		clientPlayerLeaveEvent += { _, _ ->
			ConfigSynchronizer.configs.forEach { (_, options) ->
				options.forEach { option ->
					option.playerValues.clear()
					option.serverValue = null
				}
			}
		}
	}
}