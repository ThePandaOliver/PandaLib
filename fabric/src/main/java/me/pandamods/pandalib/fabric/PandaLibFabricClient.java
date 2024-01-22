package me.pandamods.pandalib.fabric;

import me.pandamods.pandalib.PandaLibClient;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class PandaLibFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        PandaLibClient.init();
    }
}
