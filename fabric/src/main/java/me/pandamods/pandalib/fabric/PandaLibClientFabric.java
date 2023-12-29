package me.pandamods.pandalib.fabric;

import me.pandamods.pandalib.PandaLib;
import me.pandamods.pandalib.PandaLibClient;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.ModInitializer;

@Environment(EnvType.CLIENT)
public class PandaLibClientFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        PandaLibClient.init();
    }
}
