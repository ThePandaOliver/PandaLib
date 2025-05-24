/*
 * Copyright (C) 2024 Oliver Froberg (The Panda Oliver)
 *
 * This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 * You should have received a copy of the GNU General Public License
 *  along with this program. If not, see <https://www.gnu.org/licenses/>.
 */
package me.pandamods.pandalib.core.network

import me.pandamods.pandalib.config.ConfigData
import me.pandamods.pandalib.config.PandaLibConfig.configs
import me.pandamods.pandalib.config.PandaLibConfig.getConfig
import me.pandamods.pandalib.config.holders.ClientConfigHolder
import me.pandamods.pandalib.config.holders.CommonConfigHolder
import me.pandamods.pandalib.config.holders.ConfigHolder
import me.pandamods.pandalib.core.network.packets.ConfigPacketData
import me.pandamods.pandalib.networking.NetworkContext
import me.pandamods.pandalib.networking.NetworkReceiver
import me.pandamods.pandalib.networking.NetworkRegistry
import me.pandamods.pandalib.networking.PacketDistributor
import net.minecraft.server.level.ServerPlayer
import java.util.function.Consumer

object ConfigNetworking {
	fun registerPackets(registry: NetworkRegistry) {
		registry.registerBiDirectionalReceiver(
			ConfigPacketData.TYPE,
			ConfigCodec.INSTANCE,
			{ ctx: NetworkContext, data: ConfigPacketData -> CommonConfigReceiver(ctx, data) },
			{ ctx: NetworkContext, data: ConfigPacketData -> ClientConfigReceiver(ctx, data) })
	}

	fun SyncCommonConfigs(serverPlayer: ServerPlayer) {
		configs.values.stream()
			.filter { configHolder: ConfigHolder<out ConfigData> -> configHolder is CommonConfigHolder<*> && configHolder.shouldSynchronize() }
			.forEach { configHolder: ConfigHolder<out ConfigData> -> SyncCommonConfig(serverPlayer, configHolder as CommonConfigHolder<*>) }
	}

	fun SyncCommonConfig(serverPlayer: ServerPlayer, holder: CommonConfigHolder<*>) {
		holder.logger.info("Sending common config '{}' to {}", holder.resourceLocation().toString(), serverPlayer.getDisplayName()!!.getString())
		PacketDistributor.sendToPlayer(serverPlayer, ConfigPacketData(holder.resourceLocation(), holder.gson.toJsonTree(holder.get())))
	}

	fun SyncClientConfigs() {
		configs.values.stream()
			.filter { configHolder: ConfigHolder<out ConfigData> -> configHolder is ClientConfigHolder<*> && configHolder.shouldSynchronize() }
			.forEach { configHolder: ConfigHolder<out ConfigData> -> SyncClientConfig(configHolder as ClientConfigHolder<*>) }
	}

	fun SyncClientConfig(holder: ClientConfigHolder<*>) {
		holder.logger.info("Sending client config '{}' to server", holder.resourceLocation().toString())
		PacketDistributor.sendToServer(ConfigPacketData(holder.resourceLocation(), holder.gson.toJsonTree(holder.get())))
	}

	private fun ClientConfigReceiver(ctx: NetworkContext, packetData: ConfigPacketData) {
		val resourceLocation = packetData.resourceLocation
		getConfig(resourceLocation).ifPresent(Consumer { configHolder: ConfigHolder<out ConfigData> ->
			if (configHolder is ClientConfigHolder<out ConfigData>) {
				configHolder.logger.info(
					"Received client config '{}' from {}",
					configHolder.resourceLocation().toString(), ctx.player.getDisplayName()!!.getString()
				)
				configHolder.putConfig(
					ctx.player, configHolder.gson.fromJson(packetData.data, configHolder.configClass)
				)
			}
		})
	}

	private fun CommonConfigReceiver(ctx: NetworkContext, packetData: ConfigPacketData) {
		val resourceLocation = packetData.resourceLocation
		getConfig(resourceLocation).ifPresent(Consumer { configHolder: ConfigHolder<out ConfigData> ->
			if (configHolder is CommonConfigHolder<out ConfigData>) {
				configHolder.logger.info("Received common config '{}' from server", configHolder.resourceLocation().toString())
				configHolder.setCommonConfig(configHolder.gson.fromJson(packetData.data, configHolder.configClass))
			}
		})
	}
}
