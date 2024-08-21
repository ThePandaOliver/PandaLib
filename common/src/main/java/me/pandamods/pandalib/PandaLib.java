package me.pandamods.pandalib;

import com.mojang.logging.LogUtils;
import dev.architectury.event.events.client.ClientReloadShadersEvent;
import dev.architectury.registry.ReloadListenerRegistry;
import me.pandamods.pandalib.api.model.resource.AssimpResources;
import me.pandamods.pandalib.event.EventHandler;
import me.pandamods.pandalib.network.ConfigNetworking;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import org.slf4j.Logger;

public class PandaLib {
    public static final String MOD_ID = "pandalib";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static void init() {
		ReloadListenerRegistry.register(PackType.CLIENT_RESOURCES, new AssimpResources(), LOCATION("assimp_loader"));

		ConfigNetworking.registerPackets();
		EventHandler.Register();
    }

	public static ResourceLocation LOCATION(String path) {
		#if MC_VER >= MC_1_21
			return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
		#else
			return new ResourceLocation(MOD_ID, path);
		#endif
	}
}