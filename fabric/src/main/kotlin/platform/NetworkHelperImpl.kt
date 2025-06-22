/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.fabric.platform

import dev.architectury.utils.Env
import dev.pandasystems.pandalib.fabric.PandaLibFabric
import dev.pandasystems.pandalib.fabric.platform.utils.ClientPlayPayloadHandler
import dev.pandasystems.pandalib.impl.networking.NetworkContext
import dev.pandasystems.pandalib.impl.networking.PacketHandler
import dev.pandasystems.pandalib.api.platform.NetworkHelper
import dev.pandasystems.pandalib.utils.EnvRunner.runIf
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.server.level.ServerPlayer

class NetworkHelperImpl : NetworkHelper {
	/* Register Packet handlers */

	override fun <T : CustomPacketPayload> registerClientPacketHandler(
		type: CustomPacketPayload.Type<T>,
		codec: StreamCodec<in RegistryFriendlyByteBuf, T>,
		receiver: PacketHandler<T>
	) {
		PayloadTypeRegistry.playS2C().register<T>(type, codec)
		runIf(Env.CLIENT) {
			Runnable {
				ClientPlayNetworking.registerGlobalReceiver<T>(type) { payload: T, context: ClientPlayNetworking.Context ->
					ClientPlayPayloadHandler.receivePlay(receiver, payload, context)
				}
			}
		}
	}

	override fun <T : CustomPacketPayload> registerServerPacketHandler(
		type: CustomPacketPayload.Type<T>,
		codec: StreamCodec<in RegistryFriendlyByteBuf, T>,
		receiver: PacketHandler<T>
	) {
		PayloadTypeRegistry.playC2S().register<T>(type, codec)
		ServerPlayNetworking.registerGlobalReceiver<T>(type) { payload: T, context: ServerPlayNetworking.Context ->
			receiver.handler(NetworkContext(context.player(), Env.SERVER), payload)
		}
	}

	override fun <T : CustomPacketPayload> registerBiDirectionalPacketHandler(
		type: CustomPacketPayload.Type<T>,
		codec: StreamCodec<in RegistryFriendlyByteBuf, T>,
		clientReceiver: PacketHandler<T>, serverReceiver: PacketHandler<T>
	) {
		registerServerPacketHandler(type, codec, serverReceiver)
		registerClientPacketHandler(type, codec, clientReceiver)
	}
}
