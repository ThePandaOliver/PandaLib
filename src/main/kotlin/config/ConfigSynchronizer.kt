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

package dev.pandasystems.pandalib.config

import dev.pandasystems.pandalib.config.options.SyncableConfigOption
import dev.pandasystems.pandalib.event.server.serverConfigurationConnectionEvent
import dev.pandasystems.pandalib.networking.PayloadCodecRegistry
import dev.pandasystems.pandalib.networking.ServerConfigurationNetworking
import dev.pandasystems.pandalib.networking.ServerPlayNetworking
import dev.pandasystems.pandalib.networking.payloads.config.ClientboundConfigRequestPayload
import dev.pandasystems.pandalib.networking.payloads.config.CommonConfigPayload
import dev.pandasystems.pandalib.pandalibLogger
import dev.pandasystems.universalserializer.elements.TreeObject
import net.minecraft.resources.ResourceLocation
import java.util.*
import kotlin.jvm.optionals.getOrNull

object ConfigSynchronizer {
	// ConfigObjects resourceLocation -> List of SyncableConfigOptions
	internal val configs = mutableMapOf<ResourceLocation, MutableList<SyncableConfigOption<Any>>>()

	internal fun init() {
		pandalibLogger.debug("Config Synchronizer is initializing...")
		PayloadCodecRegistry.register(CommonConfigPayload.TYPE, CommonConfigPayload.CODEC)
		PayloadCodecRegistry.register(ClientboundConfigRequestPayload.TYPE, ClientboundConfigRequestPayload.CODEC)


		// Config receiving
		ServerConfigurationNetworking.registerHandler(CommonConfigPayload.TYPE) { payload, _ ->
			val resourceLocation = payload.resourceLocation
			val jsonObject = payload.optionObject
			val playerId = payload.playerId
			pandalibLogger.debug("Received config payload for {}: {}", resourceLocation, jsonObject)
			val configObject = ConfigRegistry.get<Config>(resourceLocation)
			configObject?.applyConfigPayload(jsonObject, playerId.getOrNull())
				?: pandalibLogger.error("Received config payload for unknown config object: $resourceLocation")

			// Provide the new player config to all already connected clients
			playerId.ifPresent {
				pandalibLogger.debug("Sending config payload to player {}", it)
				ServerPlayNetworking.sendToAll(payload)
			}
		}


		// Config sending

		if (configs.isNotEmpty()) {
			serverConfigurationConnectionEvent.register { handler, _ ->
				// Send all server configs
				pandalibLogger.debug("Sending all server configs to {}", handler.owner.name)
				val payloads = configs.map { (resourceLocation, _) ->
					val configObject = requireNotNull(ConfigRegistry.get<Config>(resourceLocation))
					configObject.createConfigPayload()
				}
				ServerConfigurationNetworking.send(handler, payloads)

				// Send all previous client configs to the new client
				pandalibLogger.debug("Sending previous client configs to {}", handler.owner.name)
				createConfigPayloadsWithClientConfigs()
					.takeIf { it.isNotEmpty() }
					?.let { ServerConfigurationNetworking.send(handler, it) }

				// Send request for all client's configs
				pandalibLogger.debug("Sending config request to {}", handler.owner.name)
				ServerConfigurationNetworking.send(handler, ClientboundConfigRequestPayload(handler.owner.id))
			}
		}

		pandalibLogger.debug("Config Synchronizer initialized successfully.")
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
		val payloadValues = mutableMapOf<UUID, Pair<ResourceLocation, TreeObject>>()
		configs.forEach { (resourceLocation, options) ->
			options.forEach { option ->
				val configObject = option.configObject
				option.playerValues.forEach { (playerUuid, value) ->
					payloadValues.computeIfAbsent(playerUuid) { resourceLocation to TreeObject() }
						.second[option.pathName] = configObject.serializer.toTree(value, option.type)
				}
			}
		}
		return payloadValues.map { (uuid, pair) -> CommonConfigPayload(pair.first, pair.second, Optional.of(uuid)) }
	}

	/**
	 * Applies the given values to the given config object.
	 */
	fun ConfigObject<*>.applyConfigPayload(tree: TreeObject, playerUuid: UUID?) {
		val options = requireNotNull(configs[resourceLocation]) { "Config $resourceLocation is not registered" }
		for (option in options) {
			val deserialized = serializer.fromTree(tree[option.pathName]!!, option.type)
			requireNotNull(deserialized) { "Failed to deserialize value for option ${option.pathName}" }
			if (playerUuid != null) // Set synced value for the player
				option.playerValues[playerUuid] = deserialized
			else // Set synced value for the server
				option.serverValue = deserialized
		}
	}

	fun createJsonObject(resourceLocation: ResourceLocation): TreeObject {
		val options = requireNotNull(configs[resourceLocation]) { "Config $resourceLocation is not registered" }
		val tree = TreeObject()
		for (option in options) {
			val configObject = option.configObject
			tree[option.pathName] = configObject.serializer.toTree(option.initialValue, option.type)
		}
		return tree
	}

	internal fun registerSyncableConfigOption(option: SyncableConfigOption<Any>) {
		configs.computeIfAbsent(option.configObject.resourceLocation) { mutableListOf(option) } += option
	}
}