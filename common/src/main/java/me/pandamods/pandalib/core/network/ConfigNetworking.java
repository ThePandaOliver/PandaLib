package me.pandamods.pandalib.core.network;

import dev.architectury.event.events.client.ClientPlayerEvent;
import dev.architectury.event.events.common.PlayerEvent;
import dev.architectury.networking.NetworkManager;
import dev.architectury.platform.Platform;
import io.netty.buffer.Unpooled;
import me.pandamods.pandalib.PandaLib;
import me.pandamods.pandalib.api.config.ConfigData;
import me.pandamods.pandalib.api.config.PandaLibConfig;
import me.pandamods.pandalib.api.config.holders.ClientConfigHolder;
import me.pandamods.pandalib.api.config.holders.CommonConfigHolder;
import me.pandamods.pandalib.core.event.EventHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class ConfigNetworking {
	public static final ResourceLocation CONFIG_PACKET = new ResourceLocation(PandaLib.MOD_ID, "config_sync");

	public static void SyncCommonConfigs(ServerPlayer serverPlayer) {
		PandaLibConfig.getConfigs().values().stream()
				.filter(configHolder -> configHolder instanceof CommonConfigHolder<?> && configHolder.shouldSynchronize())
				.forEach(configHolder -> SyncCommonConfig(serverPlayer, (CommonConfigHolder<?>) configHolder));
	}

	public static void SyncCommonConfig(ServerPlayer serverPlayer, CommonConfigHolder<?> holder) {
		holder.logger.info("Sending {} common config '{}'", serverPlayer.getDisplayName().getString(), holder.resourceLocation().toString());
		FriendlyByteBuf byteBuf = new FriendlyByteBuf(Unpooled.buffer());
		byteBuf.writeResourceLocation(holder.resourceLocation());
		byteBuf.writeUtf(holder.getGson().toJson(holder.get()));
		NetworkManager.sendToPlayer(serverPlayer, CONFIG_PACKET, byteBuf);
	}

	public static void SyncClientConfigs() {
		PandaLibConfig.getConfigs().values().stream()
				.filter(configHolder -> configHolder instanceof ClientConfigHolder<?> && configHolder.shouldSynchronize())
				.forEach(configHolder -> SyncClientConfig((ClientConfigHolder<?>) configHolder));
	}

	public static void SyncClientConfig(ClientConfigHolder<?> holder) {
		holder.logger.info("Sending server client config '{}'", holder.resourceLocation().toString());
		FriendlyByteBuf byteBuf = new FriendlyByteBuf(Unpooled.buffer());
		byteBuf.writeResourceLocation(holder.resourceLocation());
		byteBuf.writeUtf(holder.getGson().toJson(holder.get()));
		NetworkManager.sendToServer(CONFIG_PACKET, byteBuf);
	}

	public static void RegisterReceivers() {
		switch (Platform.getEnvironment()) {
			case SERVER -> NetworkManager.registerReceiver(NetworkManager.clientToServer(), CONFIG_PACKET, ConfigNetworking::ClientConfigReceiver);
			case CLIENT -> NetworkManager.registerReceiver(NetworkManager.serverToClient(), CONFIG_PACKET, ConfigNetworking::CommonConfigReceiver);
		}
	}

	private static void ClientConfigReceiver(FriendlyByteBuf buf, NetworkManager.PacketContext context) {
		ResourceLocation resourceLocation = buf.readResourceLocation();
		PandaLibConfig.getConfig(resourceLocation).ifPresent(configHolder -> {
			if (configHolder instanceof ClientConfigHolder<? extends ConfigData>) {
				configHolder.logger.info("Received config '{}' from {}",
						configHolder.resourceLocation().toString(), context.getPlayer().getDisplayName().getString());
				context.getPlayer().pandaLib$setConfig(configHolder.getGson().fromJson(buf.readUtf(), configHolder.getConfigClass()));
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
}
