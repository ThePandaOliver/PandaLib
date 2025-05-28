package dev.pandasystems.pandalib.fabric.platform

import dev.architectury.utils.Env
import dev.pandasystems.pandalib.fabric.PandaLibFabric
import dev.pandasystems.pandalib.fabric.platform.utils.ClientPlayPayloadHandler
import dev.pandasystems.pandalib.impl.networking.NetworkContext
import dev.pandasystems.pandalib.impl.networking.NetworkReceiver
import dev.pandasystems.pandalib.impl.platform.services.NetworkHelper
import dev.pandasystems.pandalib.core.utils.EnvRunner.runIf
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.server.level.ServerPlayer

class NetworkHelperImpl : NetworkHelper {
	override fun <T : CustomPacketPayload> registerClientReceiver(
		type: CustomPacketPayload.Type<T>,
		codec: StreamCodec<in RegistryFriendlyByteBuf, T>,
		receiver: NetworkReceiver<T>
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

	override fun <T : CustomPacketPayload> registerServerReceiver(
		type: CustomPacketPayload.Type<T>,
		codec: StreamCodec<in RegistryFriendlyByteBuf, T>,
		receiver: NetworkReceiver<T>
	) {
		PayloadTypeRegistry.playC2S().register<T>(type, codec)
		ServerPlayNetworking.registerGlobalReceiver<T>(type) { payload: T, context: ServerPlayNetworking.Context ->
			receiver.receive(NetworkContext(context.player(), Env.SERVER), payload)
		}
	}

	override fun <T : CustomPacketPayload> registerBiDirectionalReceiver(
		type: CustomPacketPayload.Type<T>,
		codec: StreamCodec<in RegistryFriendlyByteBuf, T>,
		clientReceiver: NetworkReceiver<T>, serverReceiver: NetworkReceiver<T>
	) {
		registerServerReceiver(type, codec, serverReceiver)
		registerClientReceiver(type, codec, clientReceiver)
	}

	override fun <T : CustomPacketPayload> sendToServer(payload: T) {
		ClientPlayNetworking.send(payload)
	}

	override fun <T : CustomPacketPayload> sendToPlayer(player: ServerPlayer, payload: T) {
		ServerPlayNetworking.send(player, payload)
	}

	override fun <T : CustomPacketPayload> sendToAllPlayers(payload: T) {
		requireNotNull(PandaLibFabric.server) { "PandaLibFabric.server is null!" }
		PandaLibFabric.server!!.playerList.players.forEach { sendToPlayer(it, payload) }
	}
}
