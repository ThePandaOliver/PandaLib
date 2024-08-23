package me.pandamods.pandalib.client;

import dev.architectury.registry.ReloadListenerRegistry;
import me.pandamods.pandalib.PandaLib;
import me.pandamods.pandalib.api.model.resource.AssimpResources;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.server.packs.PackType;


@Environment(EnvType.CLIENT)
public class PandaLibClient {
    public static void init() {
		ReloadListenerRegistry.register(PackType.CLIENT_RESOURCES, new AssimpResources(), PandaLib.LOCATION("assimp_loader"));
    }
}