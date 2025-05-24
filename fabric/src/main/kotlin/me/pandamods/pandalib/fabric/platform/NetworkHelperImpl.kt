/*
 * Copyright (C) 2024 Oliver Froberg (The Panda Oliver)
 *
 * This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 * You should have received a copy of the GNU Lesser General Public License
 *  along with this program. If not, see <https://www.gnu.org/licenses/>.
 */
package me.pandamods.pandalib.fabric.platform

import dev.architectury.utils.Env
import me.pandamods.pandalib.fabric.PandaLibFabric
import me.pandamods.pandalib.fabric.platform.utils.ClientPlayPayloadHandler
import me.pandamods.pandalib.networking.NetworkContext
import me.pandamods.pandalib.networking.NetworkReceiver
import me.pandamods.pandalib.platform.services.NetworkHelper
import me.pandamods.pandalib.utils.EnvRunner.runIf
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.server.level.ServerPlayer
import java.util.function.Supplier

class NetworkHelperImpl : NetworkHelper {
	override fun <T : CustomPacketPayload> registerClientReceiver(
		type: CustomPacketPayload.Type<T>,
		codec: StreamCodec<in RegistryFriendlyByteBuf, T>,
		receiver: NetworkReceiver<T>
	) {
		PayloadTypeRegistry.playS2C().register<T>(type, codec)
		runIf(
			Env.CLIENT,
			Supplier {
				Runnable {
					ClientPlayNetworking.registerGlobalReceiver<T>(
						type,
						ClientPlayNetworking.PlayPayloadHandler { payload: T, context: ClientPlayNetworking.Context ->
							ClientPlayPayloadHandler.receivePlay<T>(
								receiver,
								payload!!,
								context!!
							)
						})
				}
			}
		)
	}

	override fun <T : CustomPacketPayload> registerServerReceiver(
		type: CustomPacketPayload.Type<T>,
		codec: StreamCodec<in RegistryFriendlyByteBuf, T>,
		receiver: NetworkReceiver<T>
	) {
		PayloadTypeRegistry.playC2S().register<T>(type, codec)
		ServerPlayNetworking.registerGlobalReceiver<T>(
			type,
			ServerPlayNetworking.PlayPayloadHandler { t: T, context: ServerPlayNetworking.Context ->
				receiver.receive(
					NetworkContext(
						context!!.player(),
						Env.SERVER
					), t
				)
			})
	}

	override fun <T : CustomPacketPayload> registerBiDirectionalReceiver(
		type: CustomPacketPayload.Type<T>,
		codec: StreamCodec<in RegistryFriendlyByteBuf, T>,
		clientReceiver: NetworkReceiver<T>, serverReceiver: NetworkReceiver<T>
	) {
		registerServerReceiver<T>(type, codec, serverReceiver)
		registerClientReceiver<T>(type, codec, clientReceiver)
	}

	override fun <T : CustomPacketPayload> sendToServer(payload: T) {
		ClientPlayNetworking.send(payload)
	}

	override fun <T : CustomPacketPayload> sendToPlayer(player: ServerPlayer, payload: T) {
		ServerPlayNetworking.send(player, payload)
	}

	override fun <T : CustomPacketPayload> sendToAllPlayers(payload: T) {
		for (player in PandaLibFabric.server!!.getPlayerList().getPlayers()) {
			sendToPlayer<T>(player, payload)
		}
	}
}
