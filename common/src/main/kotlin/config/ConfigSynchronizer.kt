/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.config

import com.google.gson.JsonObject
import dev.pandasystems.pandalib.config.options.SyncableConfigOption
import dev.pandasystems.pandalib.config.serializer.ConfigSerialization
import dev.pandasystems.pandalib.event.server.serverPlayerJoinEvent
import dev.pandasystems.pandalib.logger
import dev.pandasystems.pandalib.networking.PayloadCodecRegistry
import dev.pandasystems.pandalib.networking.ServerPlayNetworking
import dev.pandasystems.pandalib.networking.payloads.config.ClientboundConfigRequestPayload
import dev.pandasystems.pandalib.networking.payloads.config.CommonConfigPayload
import net.minecraft.resources.ResourceLocation
import java.util.*
import kotlin.jvm.optionals.getOrNull

object ConfigSynchronizer {
	// ConfigObjects resourceLocation -> List of SyncableConfigOptions
	internal val configs = mutableMapOf<ResourceLocation, MutableList<SyncableConfigOption<Any>>>()

	internal fun init() {
		logger.debug("Config Synchronizer is initializing...")
		PayloadCodecRegistry.register(CommonConfigPayload.RESOURCELOCATION, CommonConfigPayload.CODEC)
		PayloadCodecRegistry.register(ClientboundConfigRequestPayload.RESOURCELOCATION, ClientboundConfigRequestPayload.CODEC)


		// Config receiving
		ServerPlayNetworking.registerHandler<CommonConfigPayload>(CommonConfigPayload.RESOURCELOCATION) { payload, _ ->
			val resourceLocation = payload.resourceLocation
			val jsonObject = payload.optionObject
			val playerId = payload.playerId
			logger.debug("Received config payload for {}: {}", resourceLocation, jsonObject)
			val configObject = ConfigRegistry.get<Config>(resourceLocation)
			configObject?.applyConfigPayload(jsonObject, playerId.getOrNull())
				?: logger.error("Received config payload for unknown config object: $resourceLocation")

			// Provide the new player config to all already connected clients
			playerId.ifPresent {
				logger.debug("Sending config payload to player {}", it)
				ServerPlayNetworking.sendToAll(payload)
			}
		}


		// Config sending

		if (configs.isNotEmpty()) {
			serverPlayerJoinEvent += { player ->
				// Send all server configs
				logger.debug("Sending all server configs to {}", player.name)
				val payloads = configs.map { (resourceLocation, _) ->
					val configObject = requireNotNull(ConfigRegistry.get<Config>(resourceLocation))
					configObject.createConfigPayload()
				}
				ServerPlayNetworking.send(player, payloads)

				// Send all previous client configs to the new client
				logger.debug("Sending previous client configs to {}", player.name)
				createConfigPayloadsWithClientConfigs()
					.takeIf { it.isNotEmpty() }
					?.let { ServerPlayNetworking.send(player, it) }

				// Send request for all client's configs
				logger.debug("Sending config request to {}", player.name)
				ServerPlayNetworking.send(player, ClientboundConfigRequestPayload(player.uuid))
			}
		}

		logger.debug("Config Synchronizer initialized successfully.")
	}

	/**
	 * Creates a CommonConfigPayload for the given player UUID.
	 *
	 * If the player UUID is null, the payload will be created for the server.
	 */
	fun ConfigObject<*>.createConfigPayload(playerUuid: UUID? = null): CommonConfigPayload {
		return CommonConfigPayload(resourceLocation, createJsonObject(resourceLocation), Optional.ofNullable(playerUuid))
	}

	/**
	 * Creates a list of CommonConfigPayloads for all client configs.
	 *
	 * Should be called on the server when a new client connects to provide the client with all connected client configs.
	 */
	fun createConfigPayloadsWithClientConfigs(): Collection<CommonConfigPayload> {
		val payloadValues = mutableMapOf<UUID, Pair<ResourceLocation, JsonObject>>()
		configs.forEach { (resourceLocation, options) ->
			options.forEach { option ->
				option.playerValues.forEach { (playerUuid, value) ->
					payloadValues.computeIfAbsent(playerUuid) { resourceLocation to JsonObject() }
						.second.add(option.pathName, ConfigSerialization.serialize(value))
				}
			}
		}
		return payloadValues.map { (uuid, pair) -> CommonConfigPayload(pair.first, pair.second, Optional.of(uuid)) }
	}

	/**
	 * Applies the given values to the given config object.
	 */
	fun ConfigObject<*>.applyConfigPayload(jsonObj: JsonObject, playerUuid: UUID?) {
		val options = requireNotNull(configs[resourceLocation]) { "Config $resourceLocation is not registered" }
		for (option in options) {
			val deserialized = requireNotNull(ConfigSerialization.deserialize(jsonObj.get(option.pathName), option.type)) {
				"Failed to deserialize value for option ${option.pathName}"
			}
			if (playerUuid != null) // Set synced value for the player
				option.playerValues[playerUuid] = deserialized
			else // Set synced value for the server
				option.serverValue = deserialized
		}
	}

	fun createJsonObject(resourceLocation: ResourceLocation): JsonObject {
		val options = requireNotNull(configs[resourceLocation]) { "Config $resourceLocation is not registered" }
		val jsonObject = JsonObject()
		for (option in options) {
			jsonObject.add(option.pathName, ConfigSerialization.serialize(option.initialValue))
		}
		return jsonObject
	}

	internal fun registerSyncableConfigOption(option: SyncableConfigOption<Any>) {
		configs.computeIfAbsent(option.configObject.resourceLocation) { mutableListOf(option) } += option
	}
}