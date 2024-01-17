package me.pandamods.pandalib.fabric;

import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Path;

public class PandaLibPlatformImpl {
    public static Path getConfigDirectory() {
        return FabricLoader.getInstance().getConfigDir();
    }
}
