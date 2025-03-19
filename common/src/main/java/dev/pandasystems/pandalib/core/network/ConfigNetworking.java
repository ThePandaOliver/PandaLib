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

package dev.pandasystems.pandalib.core.network;

import dev.pandasystems.pandalib.config.ConfigData;
import dev.pandasystems.pandalib.config.PandaLibConfig;
import dev.pandasystems.pandalib.config.holders.ClientConfigHolder;
import dev.pandasystems.pandalib.config.holders.CommonConfigHolder;
import dev.pandasystems.pandalib.core.network.packets.ConfigPacketData;
import dev.pandasystems.pandalib.networking.NetworkContext;
import dev.pandasystems.pandalib.networking.NetworkRegistry;
import dev.pandasystems.pandalib.networking.PacketDistributor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class ConfigNetworking {
	public static void registerPackets(NetworkRegistry registry) {
		registry.registerBiDirectionalReceiver(ConfigPacketData.TYPE, ConfigCodec.INSTANCE,
				ConfigNetworking::CommonConfigReceiver, ConfigNetworking::ClientConfigReceiver);
	}

	public static void SyncCommonConfigs(ServerPlayer serverPlayer) {
		PandaLibConfig.getConfigs().values().stream()
				.filter(configHolder -> configHolder instanceof CommonConfigHolder<?> && configHolder.shouldSynchronize())
				.forEach(configHolder -> SyncCommonConfig(serverPlayer, (CommonConfigHolder<?>) configHolder));
	}

	public static void SyncCommonConfig(ServerPlayer serverPlayer, CommonConfigHolder<?> holder) {
		holder.logger.info("Sending common config '{}' to {}", holder.resourceLocation().toString(), serverPlayer.getDisplayName().getString());
		PacketDistributor.sendToPlayer(serverPlayer, new ConfigPacketData(holder.resourceLocation(), holder.getGson().toJsonTree(holder.get())));
	}

	public static void SyncClientConfigs() {
		PandaLibConfig.getConfigs().values().stream()
				.filter(configHolder -> configHolder instanceof ClientConfigHolder<?> && configHolder.shouldSynchronize())
				.forEach(configHolder -> SyncClientConfig((ClientConfigHolder<?>) configHolder));
	}

	public static void SyncClientConfig(ClientConfigHolder<?> holder) {
		holder.logger.info("Sending client config '{}' to server", holder.resourceLocation().toString());
		PacketDistributor.sendToServer(new ConfigPacketData(holder.resourceLocation(), holder.getGson().toJsonTree(holder.get())));
	}

	private static void ClientConfigReceiver(NetworkContext ctx, ConfigPacketData packetData) {
		ResourceLocation resourceLocation = packetData.resourceLocation();
		PandaLibConfig.getConfig(resourceLocation).ifPresent(configHolder -> {
			if (configHolder instanceof ClientConfigHolder<? extends ConfigData> clientConfigHolder) {
				configHolder.logger.info("Received client config '{}' from {}",
						configHolder.resourceLocation().toString(), ctx.getPlayer().getDisplayName().getString());
				clientConfigHolder.putConfig(ctx.getPlayer(), configHolder.getGson()
						.fromJson(packetData.data(), configHolder.getConfigClass()));
			}
		});
	}

	private static void CommonConfigReceiver(NetworkContext ctx, ConfigPacketData packetData) {
		ResourceLocation resourceLocation = packetData.resourceLocation() ;
		PandaLibConfig.getConfig(resourceLocation).ifPresent(configHolder -> {
			if (configHolder instanceof CommonConfigHolder<? extends ConfigData> commonConfigHolder) {
				configHolder.logger.info("Received common config '{}' from server", configHolder.resourceLocation().toString());
				commonConfigHolder.setCommonConfig(configHolder.getGson().fromJson(packetData.data(), configHolder.getConfigClass()));
			}
		});
	}
}
