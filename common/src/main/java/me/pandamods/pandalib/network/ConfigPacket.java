package me.pandamods.pandalib.network;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mojang.logging.LogUtils;
import dev.architectury.networking.NetworkManager;
import io.netty.buffer.Unpooled;
import me.pandamods.pandalib.PandaLib;
import me.pandamods.pandalib.config.ConfigHolder;
import me.pandamods.pandalib.config.ConfigRegistry;
import me.pandamods.pandalib.config.ConfigType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigPacket {
	public static final Logger LOGGER = LoggerFactory.getLogger("PandaLib | Config");

	public static void requestConfigsFromPlayer(ServerPlayer player) {
		LOGGER.info("Requesting config's from {}", player.getDisplayName().getString());
		NetworkManager.sendToPlayer(player, PacketHandler.CONFIG_REQUEST_PACKET, new FriendlyByteBuf(Unpooled.buffer()));
	}

	public static void requestReceiver(FriendlyByteBuf buf, NetworkManager.PacketContext context) {
		LOGGER.info("Received config request from {}", context.getPlayer().getDisplayName().getString());
		if (!(context.getPlayer() instanceof ServerPlayer)) return;
		sendToPlayer((ServerPlayer) context.getPlayer());
	}

	public static void sendToPlayer(ServerPlayer serverPlayer) {
		LOGGER.info("Sending {} server config's", serverPlayer.getDisplayName().getString());
		for (ConfigHolder<?> configHolder : ConfigRegistry.getConfigs().values()) {
			if (configHolder.getDefinition().type().equals(ConfigType.COMMON) && configHolder.getDefinition().synchronize()) {
				FriendlyByteBuf byteBuf = new FriendlyByteBuf(Unpooled.buffer());
				byteBuf.writeResourceLocation(new ResourceLocation(configHolder.getDefinition().modId(), configHolder.getDefinition().name()));
				byteBuf.writeByteArray(new Gson().toJson(configHolder.getLocal()).getBytes());
				NetworkManager.sendToPlayer(serverPlayer, PacketHandler.CONFIG_PACKET, byteBuf);
			}
		}
	}

	public static void configReceiver(FriendlyByteBuf buf, NetworkManager.PacketContext context) {
		LOGGER.info("Received config from {}", context.getPlayer().getDisplayName().getString());
		ResourceLocation resourceLocation = buf.readResourceLocation();
		ConfigRegistry.getConfig(resourceLocation).ifPresent(configHolder -> {
			byte[] configBytes = buf.readByteArray();
			configHolder.setPlayersConfig(context.getPlayer(), ConfigHolder.GSON.fromJson(new String(configBytes), JsonObject.class));
		});
	}
}
