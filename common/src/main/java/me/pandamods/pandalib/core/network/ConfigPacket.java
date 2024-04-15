package me.pandamods.pandalib.core.network;

import dev.architectury.networking.NetworkManager;
import io.netty.buffer.Unpooled;
import me.pandamods.pandalib.api.config.ConfigData;
import me.pandamods.pandalib.api.config.PandaLibConfig;
import me.pandamods.pandalib.api.config.holders.ClientConfigHolder;
import me.pandamods.pandalib.api.config.holders.CommonConfigHolder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class ConfigPacket {
	public static void sendToPlayer(ServerPlayer serverPlayer) {
		PandaLibConfig.getConfigs().values().stream()
				.filter(configHolder -> configHolder instanceof CommonConfigHolder<?> && configHolder.shouldSynchronize())
				.forEach(configHolder -> {
					configHolder.logger.info("Sending {} server config's", serverPlayer.getDisplayName().getString());
					FriendlyByteBuf byteBuf = new FriendlyByteBuf(Unpooled.buffer());
					byteBuf.writeResourceLocation(new ResourceLocation(configHolder.getDefinition().modId(), configHolder.getDefinition().name()));
					byteBuf.writeUtf(configHolder.getGson().toJson(configHolder.get()));
					NetworkManager.sendToPlayer(serverPlayer, PacketHandler.CONFIG_PACKET, byteBuf);
				});
	}

	public static void configReceiver(FriendlyByteBuf buf, NetworkManager.PacketContext context) {
		ResourceLocation resourceLocation = buf.readResourceLocation();
		PandaLibConfig.getConfig(resourceLocation).ifPresent(configHolder -> {
			if (configHolder instanceof ClientConfigHolder<? extends ConfigData> clientConfigHolder) {
				configHolder.logger.info("Received config '{}' from {}",
						configHolder.resourceLocation().toString(), context.getPlayer().getDisplayName().getString());
//				String configJson = buf.readUtf();
//				context.getPlayer().pandaLib$setConfig(resourceLocation, configJson);
				clientConfigHolder.putConfig(context.getPlayer(), configHolder.getGson().fromJson(buf.readUtf(), configHolder.getConfigClass()));
			}
		});
	}
}
