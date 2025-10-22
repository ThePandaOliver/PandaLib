/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.client.config

import dev.pandasystems.pandalib.config.Config
import dev.pandasystems.pandalib.config.ConfigRegistry
import dev.pandasystems.pandalib.config.ConfigSynchronizer
import dev.pandasystems.pandalib.config.ConfigSynchronizer.applyConfigPayload
import dev.pandasystems.pandalib.config.ConfigSynchronizer.createConfigPayload
import dev.pandasystems.pandalib.event.client.clientPlayerJoinEvent
import dev.pandasystems.pandalib.event.client.clientPlayerLeaveEvent
import dev.pandasystems.pandalib.networking.ClientConfigurationNetworking
import dev.pandasystems.pandalib.networking.payloads.config.ClientboundConfigRequestPayload
import dev.pandasystems.pandalib.networking.payloads.config.CommonConfigPayload
import dev.pandasystems.pandalib.pandalibLogger
import kotlin.jvm.optionals.getOrNull

object ClientConfigSynchronizer {
	val configs get() = ConfigSynchronizer.configs

	internal fun init() {
		pandalibLogger.debug("Client Config Synchronizer is initializing...")

		// Config receiving
		ClientConfigurationNetworking.registerHandler<CommonConfigPayload>(CommonConfigPayload.RESOURCELOCATION) { payload, _ ->
			val resourceLocation = payload.resourceLocation
			val jsonObject = payload.optionObject
			val playerId = payload.playerId
			pandalibLogger.debug("Received config payload for {}: {}", resourceLocation, jsonObject)
			val configObject = ConfigRegistry.get<Config>(resourceLocation)
			configObject?.applyConfigPayload(jsonObject, playerId.getOrNull())
				?: pandalibLogger.error("Received config payload for unknown config object: $resourceLocation")
		}


		// Client Config request
		ClientConfigurationNetworking.registerHandler<ClientboundConfigRequestPayload>(ClientboundConfigRequestPayload.RESOURCELOCATION) { payload, _ ->
			pandalibLogger.debug("Received config request payload")
			// Respond with all client configs
			val payloads = configs.map { (resourceLocation, _) ->
				val configObject = requireNotNull(ConfigRegistry.get<Config>(resourceLocation))
				configObject.createConfigPayload(payload.playerId)
			}
			ClientConfigurationNetworking.send(payloads)
			pandalibLogger.debug("Sent all client configs")
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
			ConfigSynchronizer.configs.forEach { (_, options) ->
				options.forEach { option ->
					option.playerValues.clear()
					option.serverValue = null
				}
			}
		}

		pandalibLogger.debug("Client Config Synchronizer initialized successfully.")
	}
}