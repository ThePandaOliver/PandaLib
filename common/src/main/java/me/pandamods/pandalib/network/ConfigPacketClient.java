package me.pandamods.pandalib.network;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dev.architectury.networking.NetworkManager;
import io.netty.buffer.Unpooled;
import me.pandamods.pandalib.config.api.holders.CommonConfigHolder;
import me.pandamods.pandalib.config.api.holders.ConfigHolder;
import me.pandamods.pandalib.config.api.ConfigRegistry;
import me.pandamods.pandalib.config.api.ConfigType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public class ConfigPacketClient {
//	public static void requestConfigsFromServer() {
//		ConfigPacket.LOGGER.info("Requesting config's from server");
//		NetworkManager.sendToServer(PacketHandler.CONFIG_REQUEST_PACKET, new FriendlyByteBuf(Unpooled.buffer()));
//	}
//
//	public static void requestReceiver(FriendlyByteBuf buf, NetworkManager.PacketContext context) {
//		ConfigPacket.LOGGER.info("Received config request from server");
//		sendToServer();
//	}

	public static void sendToServer() {
		for (ConfigHolder<?> configHolder : ConfigRegistry.getConfigs().values()) {
			if (configHolder.getDefinition().type().equals(ConfigType.CLIENT) && configHolder.getDefinition().synchronize()) {
				ConfigPacket.LOGGER.info("Sending server client config's");
				FriendlyByteBuf byteBuf = new FriendlyByteBuf(Unpooled.buffer());
				byteBuf.writeResourceLocation(new ResourceLocation(configHolder.getDefinition().modId(), configHolder.getDefinition().name()));
				byteBuf.writeByteArray(new Gson().toJson(configHolder.get()).getBytes());
				NetworkManager.sendToServer(PacketHandler.CONFIG_PACKET, byteBuf);
			}
		}
	}

	public static void configReceiver(FriendlyByteBuf buf, NetworkManager.PacketContext context) {
		ResourceLocation resourceLocation = buf.readResourceLocation();
		ConfigRegistry.getConfig(resourceLocation).ifPresent(configHolder -> {
			if (configHolder instanceof CommonConfigHolder<?> commonConfigHolder) {
				ConfigPacket.LOGGER.info("Received config from server");
				byte[] configBytes = buf.readByteArray();
				commonConfigHolder.setCommonConfig(configBytes);
			}
		});
	}
}
