/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.config

import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import dev.pandasystems.pandalib.PandaLibConfig
import dev.pandasystems.pandalib.client.PandaLibClient
import dev.pandasystems.pandalib.config.options.SyncableConfigOption
import dev.pandasystems.pandalib.event.client.clientPlayerJoinEvent
import dev.pandasystems.pandalib.event.client.clientPlayerLeaveEvent
import dev.pandasystems.pandalib.event.server.serverConfigurationConnectionEvent
import dev.pandasystems.pandalib.logger
import dev.pandasystems.pandalib.networking.ClientConfigurationNetworking
import dev.pandasystems.pandalib.networking.PayloadCodecRegistry
import dev.pandasystems.pandalib.networking.ServerConfigurationNetworking
import dev.pandasystems.pandalib.networking.ServerPlayNetworking
import dev.pandasystems.pandalib.networking.payloads.config.ClientboundConfigRequestPayload
import dev.pandasystems.pandalib.networking.payloads.config.CommonConfigPayload
import dev.pandasystems.pandalib.platform.game
import net.minecraft.resources.ResourceLocation
import java.util.*
import kotlin.jvm.optionals.getOrNull

object ConfigSynchronizer {
	// ConfigObjects resourceLocation -> List of SyncableConfigOptions
	internal val configs = mutableMapOf<ResourceLocation, MutableList<SyncableConfigOption<Any>>>()

	internal fun init() {
		logger.debug("Config Synchronizer is initializing...")
		PayloadCodecRegistry.register(CommonConfigPayload.TYPE, CommonConfigPayload.CODEC)
		PayloadCodecRegistry.register(ClientboundConfigRequestPayload.TYPE, ClientboundConfigRequestPayload.CODEC)


		// Config receiving

		fun CommonConfigPayload.apply(isServer: Boolean) {
			logger.debug("Received config payload for {}: {}", resourceLocation, optionMap)
			val configObject = ConfigRegistry.get<Config>(resourceLocation)
			configObject?.applyConfigPayload(optionMap, playerId.getOrNull())
				?: logger.error("Received config payload for unknown config object: $resourceLocation")

			// Provide the new player config to all already connected clients
			if (isServer) {
				playerId.ifPresent {
					logger.debug("Sending config payload to player {}", it)
					ServerPlayNetworking.sendToAll(this)
				}
			}
		}

		ClientConfigurationNetworking.registerHandler(CommonConfigPayload.TYPE) { payload, _ -> payload.apply(false) }
		ServerConfigurationNetworking.registerHandler(CommonConfigPayload.TYPE) { payload, _ -> payload.apply(true) }


		// Config sending

		if (configs.isNotEmpty()) {
			serverConfigurationConnectionEvent.register { handler, _ ->
				// Send all server configs
				logger.debug("Sending all server configs to {}", handler.owner.name)
				val payloads = configs.map { (resourceLocation, _) ->
					val configObject = requireNotNull(ConfigRegistry.get<Config>(resourceLocation))
					configObject.createConfigPayload()
				}
				ServerConfigurationNetworking.send(handler, payloads)

				// Send all previous client configs to the new client
				logger.debug("Sending previous client configs to {}", handler.owner.name)
				createConfigPayloadsWithClientConfigs()
					.takeIf { it.isNotEmpty() }
					?.let { ServerConfigurationNetworking.send(handler, it) }

				// Send request for all client's configs
				logger.debug("Sending config request to {}", handler.owner.name)
				ServerConfigurationNetworking.send(handler, ClientboundConfigRequestPayload(handler.owner.id))
			}
		}

		ClientConfigurationNetworking.registerHandler(ClientboundConfigRequestPayload.TYPE) { payload, _ ->
			logger.debug("Received config request payload")
			// Respond with all client configs
			val payloads = configs.map { (resourceLocation, _) ->
				val configObject = requireNotNull(ConfigRegistry.get<Config>(resourceLocation))
				configObject.createConfigPayload(payload.playerId)
			}
			ClientConfigurationNetworking.send(payloads)
			logger.debug("Sent all client configs")
		}

		if (game.isClient) {
			PandaLibClient.configSynchronizerClientInit()
		}

		logger.debug("Config Synchronizer initialized successfully.")
	}

	/**
	 * Creates a CommonConfigPayload for the given player UUID.
	 *
	 * If the player UUID is null, the payload will be created for the server.
	 */
	private fun ConfigObject<*>.createConfigPayload(playerUuid: UUID? = null): CommonConfigPayload {
		val options = requireNotNull(configs[resourceLocation]) { "Config $resourceLocation is not registered" }
		val serializedOptionMap = options.associate { it.path to it.getAndSerialize().toString() }
		return CommonConfigPayload(resourceLocation, serializedOptionMap, Optional.ofNullable(playerUuid))
	}

	/**
	 * Creates a list of CommonConfigPayloads for all client configs.
	 *
	 * Should be called on the server when a new client connects to provide the client with all connected client configs.
	 */
	private fun createConfigPayloadsWithClientConfigs(): Collection<CommonConfigPayload> {
		val payloads = mutableMapOf<UUID, Pair<ResourceLocation, MutableMap<String, String>>>()
		configs.forEach { (resourceLocation, options) ->
			options.forEach { option ->
				option.playerValues.forEach { (playerUuid, value) ->
					payloads.computeIfAbsent(playerUuid) { resourceLocation to mutableMapOf() }
						.second[option.path] = option.serialize(value).toString()
				}
			}
		}
		return payloads.map { (uuid, pair) -> CommonConfigPayload(pair.first, pair.second, Optional.of(uuid)) }
	}

	/**
	 * Applies the given values to the given config object.
	 */
	private fun ConfigObject<*>.applyConfigPayload(values: Map<String, String>, playerUuid: UUID?) {
		val options = requireNotNull(configs[resourceLocation]) { "Config $resourceLocation is not registered" }
		for (option in options) {
			val stringValue = values[option.path] ?: continue
			val value: JsonElement = when (val type = option.type) {
				java.lang.Boolean::class.java -> JsonPrimitive(stringValue.toBoolean())
				java.lang.Number::class.java -> JsonPrimitive(stringValue.toDouble())
				java.lang.String::class.java -> JsonPrimitive(stringValue)
				else -> throw IllegalArgumentException("Cannot deserialize unknown type: ${type.typeName}")
			}
			if (playerUuid != null) // Set synced value for the player
				option.playerValues[playerUuid] = option.deserialize(value)
			else // Set synced value for the server
				option.serverValue = option.deserialize(value)
		}
	}

	internal fun registerSyncableConfigOption(option: SyncableConfigOption<Any>) {
		configs.computeIfAbsent(option.configObject.resourceLocation) { mutableListOf(option) } += option
	}
}