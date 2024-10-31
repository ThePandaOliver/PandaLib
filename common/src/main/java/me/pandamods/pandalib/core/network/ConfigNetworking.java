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

package me.pandamods.pandalib.core.network;

import dev.architectury.networking.NetworkManager;
import me.pandamods.pandalib.config.ConfigData;
import me.pandamods.pandalib.config.PandaLibConfig;
import me.pandamods.pandalib.config.holders.ClientConfigHolder;
import me.pandamods.pandalib.config.holders.CommonConfigHolder;
import me.pandamods.pandalib.core.network.packets.ConfigPacketData;
import me.pandamods.pandalib.utils.NetworkHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class ConfigNetworking {
	public static void registerPackets() {
		NetworkHelper.registerS2C(ConfigPacketData.TYPE, ConfigCodec.INSTANCE, ConfigNetworking::CommonConfigReceiver);

		NetworkHelper.registerC2S(ConfigPacketData.TYPE, ConfigCodec.INSTANCE, ConfigNetworking::ClientConfigReceiver);
	}

	public static void SyncCommonConfigs(ServerPlayer serverPlayer) {
		PandaLibConfig.getConfigs().values().stream()
				.filter(configHolder -> configHolder instanceof CommonConfigHolder<?> && configHolder.shouldSynchronize())
				.forEach(configHolder -> SyncCommonConfig(serverPlayer, (CommonConfigHolder<?>) configHolder));
	}

	public static void SyncCommonConfig(ServerPlayer serverPlayer, CommonConfigHolder<?> holder) {
		holder.logger.info("Sending common config '{}' to {}", holder.resourceLocation().toString(), serverPlayer.getDisplayName().getString());
		NetworkManager.sendToPlayer(serverPlayer, new ConfigPacketData(holder.resourceLocation(), holder.getGson().toJsonTree(holder.get())));
	}

	public static void SyncClientConfigs() {
		PandaLibConfig.getConfigs().values().stream()
				.filter(configHolder -> configHolder instanceof ClientConfigHolder<?> && configHolder.shouldSynchronize())
				.forEach(configHolder -> SyncClientConfig((ClientConfigHolder<?>) configHolder));
	}

	public static void SyncClientConfig(ClientConfigHolder<?> holder) {
		holder.logger.info("Sending client config '{}' to server", holder.resourceLocation().toString());
		NetworkManager.sendToServer(new ConfigPacketData(holder.resourceLocation(), holder.getGson().toJsonTree(holder.get())));
	}

	private static void ClientConfigReceiver(ConfigPacketData packetData, NetworkManager.PacketContext packetContext) {
		ResourceLocation resourceLocation = packetData.resourceLocation();
		PandaLibConfig.getConfig(resourceLocation).ifPresent(configHolder -> {
			if (configHolder instanceof ClientConfigHolder<? extends ConfigData> clientConfigHolder) {
				configHolder.logger.info("Received client config '{}' from {}",
						configHolder.resourceLocation().toString(), packetContext.getPlayer().getDisplayName().getString());
				clientConfigHolder.putConfig(packetContext.getPlayer(), configHolder.getGson()
						.fromJson(packetData.data(), configHolder.getConfigClass()));
			}
		});
	}

	private static void CommonConfigReceiver(ConfigPacketData packetData, NetworkManager.PacketContext packetContext) {
		ResourceLocation resourceLocation = packetData.resourceLocation() ;
		PandaLibConfig.getConfig(resourceLocation).ifPresent(configHolder -> {
			if (configHolder instanceof CommonConfigHolder<? extends ConfigData> commonConfigHolder) {
				configHolder.logger.info("Received common config '{}' from server", configHolder.resourceLocation().toString());
				commonConfigHolder.setCommonConfig(configHolder.getGson().fromJson(packetData.data(), configHolder.getConfigClass()));
			}
		});
	}
}
