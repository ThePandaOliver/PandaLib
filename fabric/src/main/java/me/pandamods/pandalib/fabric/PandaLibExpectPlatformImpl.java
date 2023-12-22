package me.pandamods.pandalib.fabric;

import me.pandamods.pandalib.PandaLibExpectPlatform;
import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Path;

public class PandaLibExpectPlatformImpl {
    /**
     * This is our actual method to {@link PandaLibExpectPlatform#getConfigDirectory()}.
     */
    public static Path getConfigDirectory() {
        return FabricLoader.getInstance().getConfigDir();
    }
}
