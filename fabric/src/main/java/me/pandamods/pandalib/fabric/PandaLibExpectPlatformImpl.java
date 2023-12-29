package me.pandamods.pandalib.fabric;

import com.terraformersmc.modmenu.util.mod.fabric.FabricIconHandler;
import me.pandamods.pandalib.PandaLib;
import me.pandamods.pandalib.PandaLibExpectPlatform;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.ResourceLocation;

import java.nio.file.Path;
import java.util.Optional;

public class PandaLibExpectPlatformImpl {
    /**
     * This is our actual method to {@link PandaLibExpectPlatform#getConfigDirectory()}.
     */
    public static Path getConfigDirectory() {
        return FabricLoader.getInstance().getConfigDir();
    }
}
