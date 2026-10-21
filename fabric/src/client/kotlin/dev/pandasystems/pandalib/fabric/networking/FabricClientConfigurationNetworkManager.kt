package dev.pandasystems.pandalib.fabric.networking

import com.google.auto.service.AutoService
import dev.pandasystems.pandalib.networking.*
import net.fabricmc.fabric.api.client.networking.v1.ClientConfigurationNetworking

@AutoService(ClientConfigurationNetworkManager::class)
class FabricClientConfigurationNetworkManager : ClientConfigurationNetworkManager {
	override fun <T> sendToServer(type: PacketType<T>, value: T) {
		FabricConfigurationNetworkManager.checkRegistered(type, PacketDirection.CLIENT_TO_SERVER)
		ClientConfigurationNetworking.send(FabricConfigurationNetworkManager.payload(type, value))
	}

	override fun <T> register(
		type: PacketType<T>,
		handler: ClientConfigurationPacketHandler<T>,
	) {
		FabricConfigurationNetworkManager.checkRegistered(type, PacketDirection.SERVER_TO_CLIENT)
		val payloadType = FabricPacketPayload.type(type.id)
		check(ClientConfigurationNetworking.registerGlobalReceiver(payloadType) { payload, context ->
			handler.handle(
				ClientConfigurationPacketContextImpl(
					executor = { task -> context.client().execute(task) },
					sender = this,
				),
				type.codec.decode(payload.data),
			)
		}) {
			"Fabric already has a clientbound configuration receiver for packet '${type.id}'."
		}
	}
}
