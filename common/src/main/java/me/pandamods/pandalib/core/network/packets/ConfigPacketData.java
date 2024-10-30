/*
 * Copyright (C) 2024 Oliver Froberg (The Panda Oliver)
 *
 * This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 * You should have received a copy of the GNU Lesser General Public License
 *  along with this program. If not, see <https://www.gnu.org/licenses/>.
 */
#if MC_VER >= MC_1_20_5
package me.pandamods.pandalib.core.network.packets;

import com.google.gson.JsonElement;
import me.pandamods.pandalib.PandaLib;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public record ConfigPacketData(ResourceLocation resourceLocation, JsonElement data) implements CustomPacketPayload {
	public static final Type<ConfigPacketData> TYPE = new Type<>(PandaLib.resourceLocation("config_sync"));

	@Override
	public @NotNull Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
#endif