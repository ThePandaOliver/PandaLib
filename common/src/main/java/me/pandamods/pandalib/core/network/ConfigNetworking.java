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

import dev.architectury.event.events.common.PlayerEvent;
import dev.architectury.impl.NetworkAggregator;
import dev.architectury.networking.NetworkManager;
import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import me.pandamods.pandalib.api.config.ConfigData;
import me.pandamods.pandalib.api.config.PandaLibConfig;
import me.pandamods.pandalib.api.config.holders.ClientConfigHolder;
import me.pandamods.pandalib.api.config.holders.CommonConfigHolder;
import me.pandamods.pandalib.api.util.NetworkHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

public class ConfigNetworking {
	public static void registerPackets() {
		NetworkHelper.registerS2C(CommonConfigPacketData.TYPE, CommonConfigPacketData.STREAM_CODEC, ConfigNetworking::CommonConfigReceiver);

		NetworkHelper.registerC2S(ClientConfigPacketData.TYPE, ClientConfigPacketData.STREAM_CODEC, ConfigNetworking::ClientConfigReceiver);
	}

	public static void SyncCommonConfigs(ServerPlayer serverPlayer) {
		PandaLibConfig.getConfigs().values().stream()
				.filter(configHolder -> configHolder instanceof CommonConfigHolder<?> && configHolder.shouldSynchronize())
				.forEach(configHolder -> SyncCommonConfig(serverPlayer, (CommonConfigHolder<?>) configHolder));
	}

	public static void SyncCommonConfig(ServerPlayer serverPlayer, CommonConfigHolder<?> holder) {
		holder.logger.info("Sending {} common config '{}'", serverPlayer.getDisplayName().getString(), holder.resourceLocation().toString());
		NetworkManager.sendToPlayer(serverPlayer, new CommonConfigPacketData(holder.resourceLocation().toString(), holder.getGson().toJson(holder.get())));
	}

	public static void SyncClientConfigs() {
		PandaLibConfig.getConfigs().values().stream()
				.filter(configHolder -> configHolder instanceof ClientConfigHolder<?> && configHolder.shouldSynchronize())
				.forEach(configHolder -> SyncClientConfig((ClientConfigHolder<?>) configHolder));
	}

	public static void SyncClientConfig(ClientConfigHolder<?> holder) {
		holder.logger.info("Sending server client config '{}'", holder.resourceLocation().toString());
		NetworkManager.sendToServer(new ClientConfigPacketData(holder.resourceLocation().toString(), holder.getGson().toJson(holder.get())));
	}

	private static void ClientConfigReceiver(ClientConfigPacketData packetData, NetworkManager.PacketContext packetContext) {
		ResourceLocation resourceLocation = ResourceLocation.tryParse(packetData.resourceLocation());
		PandaLibConfig.getConfig(resourceLocation).ifPresent(configHolder -> {
			if (configHolder instanceof ClientConfigHolder<? extends ConfigData>) {
				configHolder.logger.info("Received config '{}' from {}",
						configHolder.resourceLocation().toString(), packetContext.getPlayer().getDisplayName().getString());
				packetContext.getPlayer().pandaLib$setConfig(configHolder.getGson()
						.fromJson(packetData.configJson(), configHolder.getConfigClass()));
			}
		});
	}

	private static void CommonConfigReceiver(CommonConfigPacketData packetData, NetworkManager.PacketContext packetContext) {
		ResourceLocation resourceLocation = ResourceLocation.tryParse(packetData.resourceLocation());
		PandaLibConfig.getConfig(resourceLocation).ifPresent(configHolder -> {
			if (configHolder instanceof CommonConfigHolder<? extends ConfigData> commonConfigHolder) {
				configHolder.logger.info("Received common config '{}' from server", configHolder.resourceLocation().toString());
				commonConfigHolder.setCommonConfig(configHolder.getGson().fromJson(packetData.configJson(), configHolder.getConfigClass()));
			}
		});
	}
}
