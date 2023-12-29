package me.pandamods.pandalib.config;

import dev.architectury.platform.Mod;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public record ModConfigData(
		ResourceLocation iconLocation,
		Component title,
		Component description,
		Mod mod
) {
}
