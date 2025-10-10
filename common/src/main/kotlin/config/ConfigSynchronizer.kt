/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.config

import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import dev.pandasystems.pandalib.config.options.SyncableConfigOption
import dev.pandasystems.pandalib.event.serverevents.serverConfigurationConnectionEvent
import dev.pandasystems.pandalib.logger
import dev.pandasystems.pandalib.networking.ClientConfigurationNetworking
import dev.pandasystems.pandalib.networking.PayloadCodecRegistry
import dev.pandasystems.pandalib.networking.ServerConfigurationNetworking
import dev.pandasystems.pandalib.networking.payloads.config.CommonConfigPayload
import dev.pandasystems.pandalib.networking.payloads.config.CommonConfigRequestPayload
import net.minecraft.resources.ResourceLocation
import java.util.*
import kotlin.jvm.optionals.getOrNull

object ConfigSynchronizer {
	// ConfigObjects resourceLocation -> List of SyncableConfigOptions
	private val configs = mutableMapOf<ResourceLocation, MutableList<SyncableConfigOption<Any>>>()

	internal fun init() {
		logger.debug("Config Synchronizer is initializing...")
		PayloadCodecRegistry.register(CommonConfigPayload.TYPE, CommonConfigPayload.CODEC)
		PayloadCodecRegistry.register(CommonConfigRequestPayload.TYPE, CommonConfigRequestPayload.CODEC)


		// Config receiving

		fun CommonConfigPayload.apply() {
			val configObject = ConfigRegistry.get<Config>(resourceLocation)
			configObject?.applyConfigPayload(optionMap, ownerUuid.getOrNull()?.let { UUID.fromString(it) })
				?: logger.error("Received config payload for unknown config object: $resourceLocation")
		}

		ClientConfigurationNetworking.registerHandler(CommonConfigPayload.TYPE) { payload, _ -> payload.apply() }
		ServerConfigurationNetworking.registerHandler(CommonConfigPayload.TYPE) { payload, _ -> payload.apply() }


		// Config sending

		if (configs.isNotEmpty()) {
			serverConfigurationConnectionEvent.register { handler, _ ->
				configs.forEach { (resourceLocation, _) ->
					val configObject = requireNotNull(ConfigRegistry.get<Config>(resourceLocation))
					ServerConfigurationNetworking.send(handler, configObject.createConfigPayload()) // TODO: Bundle all config payloads into one packet
				}

				ServerConfigurationNetworking.send(handler, CommonConfigRequestPayload())
			}
		}

		ClientConfigurationNetworking.registerHandler(CommonConfigRequestPayload.TYPE) { _, context ->
			configs.forEach { (resourceLocation, _) ->
				val configObject = requireNotNull(ConfigRegistry.get<Config>(resourceLocation))
				context.responseSender().sendPacket(configObject.createConfigPayload(context.client().player?.uuid)) // TODO: Bundle all config payloads into one packet
			}
		}

		logger.debug("Config Synchronizer initialized successfully.")
	}

	private fun ConfigObject<*>.createConfigPayload(playerUuid: UUID? = null): CommonConfigPayload {
		val options = requireNotNull(configs[resourceLocation]) { "Config $resourceLocation is not registered" }
		val values = mutableMapOf<String, String>()
		options.forEach { values[it.path] = it.serialize().toString() }
		return CommonConfigPayload(resourceLocation, values, Optional.ofNullable(playerUuid?.toString()))
	}

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
			if (playerUuid != null)
				option.syncedPlayerValues[playerUuid] = option.deserialize(value)
			else
				option.syncedValue = option.deserialize(value)
		}
	}

	internal fun registerSyncableConfigOption(option: SyncableConfigOption<Any>) {
		configs.computeIfAbsent(option.configObject.resourceLocation) { mutableListOf(option) } += option
	}
}