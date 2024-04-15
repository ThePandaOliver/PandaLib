package me.pandamods.pandalib.core.extensions;

import com.google.gson.JsonObject;
import me.pandamods.pandalib.api.config.ConfigData;
import me.pandamods.pandalib.api.config.PandaLibConfig;
import me.pandamods.pandalib.api.config.holders.ClientConfigHolder;
import me.pandamods.pandalib.api.config.holders.ConfigHolder;
import net.minecraft.resources.ResourceLocation;

public interface PlayerExtension {
	default <T extends ConfigData> void pandaLib$setConfig(T config) {}
	default <T extends ConfigData> T pandaLib$getConfig(Class<T> configClass) {
		return PandaLibConfig.getConfig(configClass).get();
	}

	@Deprecated(forRemoval = true, since = "0.2.1")
	default <T extends ConfigData> void pandaLib$setConfig(ResourceLocation resourceLocation, T config) {
		pandaLib$setConfig(config);
	}
	@Deprecated(forRemoval = true, since = "0.2.1")
	default <T extends ConfigData> T pandaLib$getConfig(ConfigHolder<T> configClass) {
		return pandaLib$getConfig(configClass.getConfigClass());
	}
}
