/*
 * Copyright (C) 2025 Oliver Froberg (The Panda Oliver)
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
import dev.pandasystems.pandalib.event.client.clientPlayerJoinEvent
import dev.pandasystems.pandalib.event.client.clientPlayerLeaveEvent
import dev.pandasystems.pandalib.networking.ClientPlayNetworking
import dev.pandasystems.pandalib.networking.payloads.config.ClientboundConfigRequestPayload
import dev.pandasystems.pandalib.networking.payloads.config.CommonConfigPayload
import kotlin.jvm.optionals.getOrNull

object ClientConfigSynchronizer {
	val configs get() = ConfigSynchronizer.configs

	internal fun init() {
		PandaLib.logger.debug("Client Config Synchronizer is initializing...")

		// Config receiving
		ClientPlayNetworking.registerHandler<CommonConfigPayload>(CommonConfigPayload.RESOURCELOCATION) { payload, _ ->
			val resourceLocation = payload.resourceLocation
			val jsonObject = payload.optionObject
			val playerId = payload.playerId
			PandaLib.logger.debug("Received config payload for {}: {}", resourceLocation, jsonObject)
			val configObject = ConfigRegistry.get<Any>(resourceLocation)
			configObject?.applyConfigPayload(jsonObject, playerId.getOrNull())
				?: PandaLib.logger.error("Received config payload for unknown config object: $resourceLocation")
		}

		// Client Config request
		ClientPlayNetworking.registerHandler<ClientboundConfigRequestPayload>(ClientboundConfigRequestPayload.RESOURCELOCATION) { payload, context ->
			PandaLib.logger.debug("Received config request payload")
			// Respond with all client configs
			val payloads = configs.map { (resourceLocation, _) ->
				val configObject = requireNotNull(ConfigRegistry.get<Any>(resourceLocation))
				configObject.createConfigPayload(payload.playerId)
			}
			context.responseSender().sendPacket(payloads)
			PandaLib.logger.debug("Sent all client configs")
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

		PandaLib.logger.debug("Client Config Synchronizer initialized successfully.")
	}
}