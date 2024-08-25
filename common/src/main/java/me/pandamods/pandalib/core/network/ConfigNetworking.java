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

import me.pandamods.pandalib.config.ConfigData;
import me.pandamods.pandalib.config.PandaLibConfig;
import me.pandamods.pandalib.config.holders.ClientConfigHolder;
import me.pandamods.pandalib.config.holders.CommonConfigHolder;
#if MC_VER >= MC_1_20_5
import me.pandamods.pandalib.core.network.packets.ClientConfigPacketData;
import me.pandamods.pandalib.core.network.packets.CommonConfigPacketData;
#else
import net.minecraft.network.FriendlyByteBuf;
import io.netty.buffer.Unpooled;
#endif
import me.pandamods.pandalib.networking.NetworkManager;
import me.pandamods.pandalib.networking.PacketContext;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class ConfigNetworking {
	#if MC_VER <= MC_1_20
		public static final ResourceLocation CONFIG_PACKET = PandaLib.LOCATION("config_sync");
	#endif

	public static void registerPackets() {
		#if MC_VER >= MC_1_20_5
			NetworkManager.registerS2CReceiver(CommonConfigPacketData.TYPE, CommonConfigPacketData.STREAM_CODEC, ConfigNetworking::CommonConfigReceiver);

		NetworkManager.registerC2SReceiver(ClientConfigPacketData.TYPE, ClientConfigPacketData.STREAM_CODEC, ConfigNetworking::ClientConfigReceiver);
		#else
			NetworkHelper.registerS2C(CONFIG_PACKET, ConfigNetworking::CommonConfigReceiver);

			NetworkHelper.registerC2S(CONFIG_PACKET, ConfigNetworking::ClientConfigReceiver);
		#endif
	}

	public static void SyncCommonConfigs(ServerPlayer serverPlayer) {
		PandaLibConfig.getConfigs().values().stream()
				.filter(configHolder -> configHolder instanceof CommonConfigHolder<?> && configHolder.shouldSynchronize())
				.forEach(configHolder -> SyncCommonConfig(serverPlayer, (CommonConfigHolder<?>) configHolder));
	}

	public static void SyncCommonConfig(ServerPlayer serverPlayer, CommonConfigHolder<?> holder) {
		holder.logger.info("Sending common config '{}' to {}", holder.resourceLocation().toString(), serverPlayer.getDisplayName().getString());
		#if MC_VER >= MC_1_20_5
			NetworkManager.sendToPlayer(serverPlayer, new CommonConfigPacketData(holder.resourceLocation().toString(), holder.getGson().toJson(holder.get())));
		#else
			FriendlyByteBuf byteBuf = new FriendlyByteBuf(Unpooled.buffer());
			byteBuf.writeResourceLocation(holder.resourceLocation());
			byteBuf.writeUtf(holder.getGson().toJson(holder.get()));
			NetworkManager.sendToPlayer(serverPlayer, CONFIG_PACKET, byteBuf);
		#endif
	}

	public static void SyncClientConfigs() {
		PandaLibConfig.getConfigs().values().stream()
				.filter(configHolder -> configHolder instanceof ClientConfigHolder<?> && configHolder.shouldSynchronize())
				.forEach(configHolder -> SyncClientConfig((ClientConfigHolder<?>) configHolder));
	}

	public static void SyncClientConfig(ClientConfigHolder<?> holder) {
		holder.logger.info("Sending client config '{}' to server", holder.resourceLocation().toString());
		#if MC_VER >= MC_1_20_5
			NetworkManager.sendToServer(new ClientConfigPacketData(holder.resourceLocation().toString(), holder.getGson().toJson(holder.get())));
		#else
			FriendlyByteBuf byteBuf = new FriendlyByteBuf(Unpooled.buffer());
			byteBuf.writeResourceLocation(holder.resourceLocation());
			byteBuf.writeUtf(holder.getGson().toJson(holder.get()));
			NetworkManager.sendToServer(CONFIG_PACKET, byteBuf);
		#endif
	}

	#if MC_VER >= MC_1_20_5
		private static void ClientConfigReceiver(ClientConfigPacketData packetData, PacketContext packetContext) {
			ResourceLocation resourceLocation = ResourceLocation.tryParse(packetData.resourceLocation());
			PandaLibConfig.getConfig(resourceLocation).ifPresent(configHolder -> {
				if (configHolder instanceof ClientConfigHolder<? extends ConfigData> clientConfigHolder) {
					configHolder.logger.info("Received client config '{}' from {}",
							configHolder.resourceLocation().toString(), packetContext.getPlayer().getDisplayName().getString());
					clientConfigHolder.putConfig(packetContext.getPlayer(), configHolder.getGson()
							.fromJson(packetData.configJson(), configHolder.getConfigClass()));
				}
			});
		}

		private static void CommonConfigReceiver(CommonConfigPacketData packetData, PacketContext packetContext) {
			ResourceLocation resourceLocation = ResourceLocation.tryParse(packetData.resourceLocation());
			PandaLibConfig.getConfig(resourceLocation).ifPresent(configHolder -> {
				if (configHolder instanceof CommonConfigHolder<? extends ConfigData> commonConfigHolder) {
					configHolder.logger.info("Received common config '{}' from server", configHolder.resourceLocation().toString());
					commonConfigHolder.setCommonConfig(configHolder.getGson().fromJson(packetData.configJson(), configHolder.getConfigClass()));
				}
			});
		}
	#else
		private static void ClientConfigReceiver(FriendlyByteBuf buf, NetworkManager.PacketContext context) {
			ResourceLocation resourceLocation = buf.readResourceLocation();
			PandaLibConfig.getConfig(resourceLocation).ifPresent(configHolder -> {
				if (configHolder instanceof ClientConfigHolder<? extends ConfigData> clientConfigHolder) {
					configHolder.logger.info("Received client config '{}' from {}",
							configHolder.resourceLocation().toString(), context.getPlayer().getDisplayName().getString());
					clientConfigHolder.putConfig(context.getPlayer(), configHolder.getGson().fromJson(buf.readUtf(), configHolder.getConfigClass()));
				}
			});
		}

		private static void CommonConfigReceiver(FriendlyByteBuf buf, NetworkManager.PacketContext context) {
			ResourceLocation resourceLocation = buf.readResourceLocation();
			PandaLibConfig.getConfig(resourceLocation).ifPresent(configHolder -> {
				if (configHolder instanceof CommonConfigHolder<? extends ConfigData> commonConfigHolder) {
					configHolder.logger.info("Received common config '{}' from server", configHolder.resourceLocation().toString());
					commonConfigHolder.setCommonConfig(configHolder.getGson().fromJson(buf.readUtf(), configHolder.getConfigClass()));
				}
			});
		}
	#endif
}
