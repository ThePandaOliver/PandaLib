/*
 * Copyright (C) 2026 Oliver Froberg (The Panda Oliver)
 *
 * This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 * You should have received a copy of the GNU Lesser General Public License
 *  along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package dev.pandasystems.pandalib.client.config

import dev.pandasystems.pandalib.PandaLib
import dev.pandasystems.pandalib.config.ConfigRegistry
import dev.pandasystems.pandalib.config.ConfigSynchronizer
import dev.pandasystems.pandalib.config.ConfigSynchronizer.applyConfigPayload
import dev.pandasystems.pandalib.config.ConfigSynchronizer.createConfigPayload
import dev.pandasystems.pandalib.config.exceptions.ConfigNotRegisteredException
import dev.pandasystems.pandalib.event.client.clientPlayerJoinEvent
import dev.pandasystems.pandalib.event.client.clientPlayerLeaveEvent
import dev.pandasystems.pandalib.networking.ClientConfigurationNetworking
import dev.pandasystems.pandalib.networking.payloads.config.ClientboundConfigRequestPayload
import dev.pandasystems.pandalib.networking.payloads.config.CommonConfigPayload
import kotlin.jvm.optionals.getOrNull

object ClientConfigSynchronizer {
	val configs get() = ConfigSynchronizer.configs

	internal fun init() {
		PandaLib.logger.debug("Client Config Synchronizer is initializing...")

		// Config receiving
		ClientConfigurationNetworking.registerHandler(CommonConfigPayload.TYPE) { payload, _ ->
			ConfigSynchronizer.handleConfigPayload(payload)
		}

		// Client Config request
		ClientConfigurationNetworking.registerHandler(ClientboundConfigRequestPayload.TYPE) { payload, _ ->
			PandaLib.logger.debug("Received config settings request from server")
			// Respond with all client configs
			val payloads = configs.mapNotNull { (resourceLocation, _) ->
				PandaLib.logger.debug("Attempting to create config payload for: {}", resourceLocation)
				try {
					val configObject = ConfigRegistry.get<Any>(resourceLocation)
					val payload = configObject.createConfigPayload(payload.playerId)
					PandaLib.logger.debug("Successfully created config payload for: {}", resourceLocation)
					return@mapNotNull payload
				} catch (e: ConfigNotRegisteredException) {
					PandaLib.logger.error(
						"Failed to create config payload for unknown config: $resourceLocation", e
					)
					return@mapNotNull null
				}
			}
			ClientConfigurationNetworking.send(payloads)
			PandaLib.logger.debug("Sent all successfully created config payloads to Server")
		}

		// Adding local players configs to player configs
		clientPlayerJoinEvent += { player ->
			ConfigSynchronizer.configs.forEach { (_, options) ->
				options.forEach { option ->
					option.playerValues[player.uuid] = option.initialValue
				}
			}
		}

		// Cleanup
		clientPlayerLeaveEvent += { _ ->
			configs.forEach { (_, options) ->
				options.forEach { option ->
					option.playerValues.clear()
					option.serverValue = null
				}
			}
		}

		PandaLib.logger.debug("Client Config Synchronizer finished initializing!")
	}
}