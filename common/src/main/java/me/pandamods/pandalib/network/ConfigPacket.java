package me.pandamods.pandalib.network;

import com.google.gson.Gson;
import dev.architectury.networking.NetworkManager;
import io.netty.buffer.Unpooled;
import me.pandamods.pandalib.config.api.PandaLibConfig;
import me.pandamods.pandalib.config.api.holders.ClientConfigHolder;
import me.pandamods.pandalib.config.api.holders.CommonConfigHolder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class ConfigPacket {
	public static void sendToPlayer(ServerPlayer serverPlayer) {
		PandaLibConfig.getConfigs().values().stream()
				.filter(configHolder -> configHolder instanceof CommonConfigHolder<?> && configHolder.getDefinition().synchronize())
				.forEach(configHolder -> {
					configHolder.logger.info("Sending {} server config's", serverPlayer.getDisplayName().getString());
					FriendlyByteBuf byteBuf = new FriendlyByteBuf(Unpooled.buffer());
					byteBuf.writeResourceLocation(new ResourceLocation(configHolder.getDefinition().modId(), configHolder.getDefinition().name()));
					byteBuf.writeByteArray(new Gson().toJson(configHolder.get()).getBytes());
					NetworkManager.sendToPlayer(serverPlayer, PacketHandler.CONFIG_PACKET, byteBuf);
				});
	}

	public static void configReceiver(FriendlyByteBuf buf, NetworkManager.PacketContext context) {
		ResourceLocation resourceLocation = buf.readResourceLocation();
		PandaLibConfig.getConfig(resourceLocation).ifPresent(configHolder -> {
			if (configHolder instanceof ClientConfigHolder<?> clientConfigHolder) {
				configHolder.logger.info("Received config '{}' from {}", configHolder.name(), context.getPlayer().getDisplayName().getString());
				byte[] configBytes = buf.readByteArray();
				context.getPlayer().pandaLib$setConfig(resourceLocation, configBytes);
			}
		});
	}
}
