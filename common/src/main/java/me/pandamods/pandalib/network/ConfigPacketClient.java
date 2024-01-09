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
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigPacketClient {
//	public static void requestConfigsFromServer() {
//		ConfigPacket.LOGGER.info("Requesting config's from server");
//		NetworkManager.sendToServer(PacketHandler.CONFIG_REQUEST_PACKET, new FriendlyByteBuf(Unpooled.buffer()));
//	}

	public static void requestReceiver(FriendlyByteBuf buf, NetworkManager.PacketContext context) {
		ConfigPacket.LOGGER.info("Received config request from server");
		sendToServer();
	}

	public static void sendToServer() {
		ConfigPacket.LOGGER.info("Sending server client config's");
		for (ConfigHolder<?> configHolder : ConfigRegistry.getConfigs().values()) {
			if (configHolder.getDefinition().type().equals(ConfigType.CLIENT) && configHolder.getDefinition().synchronize()) {
				FriendlyByteBuf byteBuf = new FriendlyByteBuf(Unpooled.buffer());
				byteBuf.writeResourceLocation(new ResourceLocation(configHolder.getDefinition().modId(), configHolder.getDefinition().name()));
				byteBuf.writeByteArray(new Gson().toJson(configHolder.getLocal()).getBytes());
				NetworkManager.sendToServer(PacketHandler.CONFIG_PACKET, byteBuf);
			}
		}
	}

	public static void configReceiver(FriendlyByteBuf buf, NetworkManager.PacketContext context) {
		ConfigPacket.LOGGER.info("Received config from server");
		ResourceLocation resourceLocation = buf.readResourceLocation();
		ConfigRegistry.getConfig(resourceLocation).ifPresent(configHolder -> {
			byte[] configBytes = buf.readByteArray();
			configHolder.setServerConfig(ConfigHolder.GSON.fromJson(new String(configBytes), JsonObject.class));
		});
	}
}
