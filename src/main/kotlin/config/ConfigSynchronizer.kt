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

import dev.pandasystems.pandalib.PandaLib
import dev.pandasystems.pandalib.config.ConfigSynchronizer.configs
import dev.pandasystems.pandalib.event.server.serverPlayerJoinEvent
import dev.pandasystems.pandalib.networking.PayloadCodecRegistry
import dev.pandasystems.pandalib.networking.ServerPlayNetworking
import dev.pandasystems.pandalib.networking.payloads.config.ClientboundConfigRequestPayload
import dev.pandasystems.pandalib.networking.payloads.config.CommonConfigPayload
import dev.pandasystems.universalserializer.elements.TreeObject
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.player.Player
import java.util.*
import kotlin.jvm.optionals.getOrNull
import kotlin.reflect.KProperty
import kotlin.reflect.KProperty0
import kotlin.reflect.KType

object ConfigSynchronizer {
	// ConfigObjects resourceLocation -> List of SyncableConfigOptions
	internal val configs = mutableMapOf<ResourceLocation, MutableList<SyncableOption<Any?>>>()

	internal fun init() {
		PandaLib.logger.debug("Config Synchronizer is initializing...")
		PayloadCodecRegistry.register<CommonConfigPayload>(CommonConfigPayload.RESOURCELOCATION, CommonConfigPayload.CODEC)
		PayloadCodecRegistry.register<ClientboundConfigRequestPayload>(ClientboundConfigRequestPayload.RESOURCELOCATION, ClientboundConfigRequestPayload.CODEC)


		// Config receiving
		ServerPlayNetworking.registerHandler<CommonConfigPayload>(CommonConfigPayload.RESOURCELOCATION) { payload, _ ->
			val resourceLocation = payload.resourceLocation
			val jsonObject = payload.optionObject
			val playerId = payload.playerId
			PandaLib.logger.debug("Received config payload for {}: {}", resourceLocation, jsonObject)
			val configObject = ConfigRegistry.get<Any>(resourceLocation)
			configObject?.applyConfigPayload(jsonObject, playerId.getOrNull())
				?: PandaLib.logger.error("Received config payload for unknown config object: $resourceLocation")

			// Provide the new player config to all already connected clients
			playerId.ifPresent {
				PandaLib.logger.debug("Sending config payload to player {}", it)
				ServerPlayNetworking.sendToAll(payload)
			}
		}


		// Config sending

		if (configs.isNotEmpty()) {
			serverPlayerJoinEvent += { player ->
				// Send all server configs
				PandaLib.logger.debug("Sending all server configs to {}", player.name)
				val payloads = configs.map { (resourceLocation, _) ->
					val configObject = requireNotNull(ConfigRegistry.get<Any>(resourceLocation))
					configObject.createConfigPayload()
				}
				ServerPlayNetworking.send(player, payloads)

				// Send all previous client configs to the new client
				PandaLib.logger.debug("Sending previous client configs to {}", player.name)
				createConfigPayloadsWithClientConfigs()
					.takeIf { it.isNotEmpty() }
					?.let { ServerPlayNetworking.send(player, it) }

				// Send request for all client's configs
				PandaLib.logger.debug("Sending config request to {}", player.name)
				ServerPlayNetworking.send(player, ClientboundConfigRequestPayload(player.uuid))
			}
		}

		PandaLib.logger.debug("Config Synchronizer initialized successfully.")
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
						.second[option.id] = configObject.serializer.toTree(value, option.valueType)
				}
			}
		}
		return payloadValues.map { (uuid, pair) -> CommonConfigPayload(pair.first, pair.second, Optional.of(uuid)) }
	}

	/**
	 * Applies the given values to the given config object.
	 */
	fun ConfigObject<*>.applyConfigPayload(tree: TreeObject, playerUuid: UUID?) {
		println(tree)
		val options = requireNotNull(configs[resourceLocation]) { "Config $resourceLocation is not registered" }
		for (option in options) {
			val deserialized = serializer.fromTree(tree[option.id]!!, option.valueType)
			requireNotNull(deserialized) { "Failed to deserialize value for option ${option.property.name}" }
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
			tree[option.id] = configObject.serializer.toTree(option.initialValue, option.valueType)
		}
		return tree
	}

	class SyncableOption<T : Any?>(val configObject: ConfigObject<*>, val property: KProperty0<T>) {
		val id: String = "${configObject.resourceLocation}#${property.name}.${configs[configObject.resourceLocation]!!.size}"
		val valueType: KType = property.returnType
		val initialValue: T get() = property.get()

		internal val playerValues = mutableMapOf<UUID, T>()
		var serverValue: T? = null
			get() = field ?: initialValue

		operator fun get(player: UUID): T {
			val playerValue = playerValues[player]
			if (playerValue != null) return playerValue
			PandaLib.logger.warn("No synced value for player $player in config option ${property.name}")
			return initialValue
		}

		operator fun get(player: Player): T = this[player.uuid]
	}
}

fun ConfigObject<*>.syncOption(property: KProperty0<*>) {
	configs.computeIfAbsent(this.resourceLocation) { mutableListOf() } +=
		ConfigSynchronizer.SyncableOption(this, property)
}

@Suppress("UNCHECKED_CAST")
fun <T : Any?> ConfigObject<*>.getSynced(property: KProperty<T>): ConfigSynchronizer.SyncableOption<T> {
	return requireNotNull(configs[this.resourceLocation]?.find { it.property == property }) {
		"Synced property $property is not registered for config ${this.resourceLocation}"
	} as ConfigSynchronizer.SyncableOption<T>
}