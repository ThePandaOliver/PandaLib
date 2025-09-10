/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib

import com.mojang.logging.LogUtils
import dev.pandasystems.pandalib.config.ConfigRegistry
import dev.pandasystems.pandalib.config.SyncedConfig
import dev.pandasystems.pandalib.event.serverevents.ServerConfigurationConnectionEvents
import dev.pandasystems.pandalib.networking.ClientConfigurationNetworking
import dev.pandasystems.pandalib.networking.PayloadCodecRegistry
import dev.pandasystems.pandalib.networking.ServerConfigurationNetworking
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.chat.Component
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.resources.ResourceLocation
import org.slf4j.Logger

object PandaLib {
	const val MOD_ID = "pandalib"
	
	val config = ConfigRegistry.register<PandaLibConfig>()
	
	init {
		logger.debug("PandaLib Core is initializing...")

		SyncedConfig.init()

		// TODO: Testing code

		PayloadCodecRegistry.register(testingPayloadType, testingPayloadCodec)
		ServerConfigurationNetworking.registerHandler(testingPayloadType) {payload, context ->
			logger.info("Received Server testing payload!")
		}
		ClientConfigurationNetworking.registerHandler(testingPayloadType) {payload, context ->
			logger.info("Received Client testing payload!")
			context.responseSender().sendPacket(payload)
			context.responseSender().disconnect(Component.literal("Received Server testing payload!"))
		}

		println(Thread.currentThread().name)
		ServerConfigurationConnectionEvents.configure.register { handler, server ->
			logger.info("Configuring server...")
			ServerConfigurationNetworking.send(handler, TestingPayload())
		}

		logger.debug("PandaLib Core initialized successfully.")
	}
	
	@JvmStatic
	fun resourceLocation(path: String): ResourceLocation {
		return dev.pandasystems.pandalib.utils.extensions.resourceLocation(MOD_ID, path)
	}
}

val logger: Logger = LogUtils.getLogger()

class TestingPayload: CustomPacketPayload {
	override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> = testingPayloadType
}

val testingPayloadType = CustomPacketPayload.Type<TestingPayload>(PandaLib.resourceLocation("test"))

val testingPayloadCodec = StreamCodec.of<FriendlyByteBuf, TestingPayload>(
	{buf, payload -> },
	{buf -> TestingPayload()}
)