package me.pandamods.pandalib.impl;

import me.pandamods.pandalib.config.api.ConfigData;
import me.pandamods.pandalib.config.api.holders.ConfigHolder;
import net.minecraft.resources.ResourceLocation;

public interface PlayerImpl {
	default void pandaLib$setConfig(ResourceLocation resourceLocation, byte[] configBytes) {}
	default <T extends ConfigData> T pandaLib$getConfig(ConfigHolder<T> holder) {
		return null;
	}
}
