package me.pandamods.pandalib.core.network;

import com.google.gson.Gson;
import dev.architectury.networking.NetworkManager;
import io.netty.buffer.Unpooled;
import me.pandamods.pandalib.api.config.PandaLibConfig;
import me.pandamods.pandalib.api.config.holders.ClientConfigHolder;
import me.pandamods.pandalib.api.config.holders.CommonConfigHolder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import java.nio.charset.StandardCharsets;

public class ConfigPacketClient {
	public static void sendToServer() {
		PandaLibConfig.getConfigs().values().stream()
				.filter(configHolder -> configHolder instanceof ClientConfigHolder<?> && configHolder.shouldSynchronize())
				.forEach(configHolder -> {
					configHolder.logger.info("Sending server client config's");
					FriendlyByteBuf byteBuf = new FriendlyByteBuf(Unpooled.buffer());
					byteBuf.writeResourceLocation(new ResourceLocation(configHolder.getDefinition().modId(), configHolder.getDefinition().name()));
					byteBuf.writeUtf(configHolder.getGson().toJson(configHolder.get()));
					NetworkManager.sendToServer(PacketHandler.CONFIG_PACKET, byteBuf);
				});
	}

	public static void configReceiver(FriendlyByteBuf buf, NetworkManager.PacketContext context) {
		ResourceLocation resourceLocation = buf.readResourceLocation();
		PandaLibConfig.getConfig(resourceLocation).ifPresent(configHolder -> {
			if (configHolder instanceof CommonConfigHolder<?> commonConfigHolder) {
				configHolder.logger.info("Received config '{}' from server", configHolder.resourceLocation().toString());
				commonConfigHolder.setCommonConfig(configHolder.getGson().fromJson(buf.readUtf(), configHolder.getConfigClass()));
			}
		});
	}
}
