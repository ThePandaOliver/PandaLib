package me.pandamods.pandalib.core.extensions;

import com.google.gson.JsonObject;
import me.pandamods.pandalib.api.config.ConfigData;
import me.pandamods.pandalib.api.config.holders.ConfigHolder;
import net.minecraft.resources.ResourceLocation;

public interface PlayerExtension {
	default void pandaLib$setConfig(ResourceLocation resourceLocation, String configJson) {}
	default <T extends ConfigData> T pandaLib$getConfig(ConfigHolder<T> holder) {
		return null;
	}
}
