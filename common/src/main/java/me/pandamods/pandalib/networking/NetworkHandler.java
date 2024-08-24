/*
 * Copyright (C) 2024 Oliver Froberg (The Panda Oliver)
 *
 * This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 * You should have received a copy of the GNU General Public License
 *  along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package me.pandamods.pandalib.networking;

import me.pandamods.pandalib.platform.Services;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public abstract class NetworkHandler {
	@Environment(EnvType.CLIENT)
	public abstract <T extends CustomPacketPayload> void sendToServer(T payload);
	public abstract <T extends CustomPacketPayload> void sendToPlayer(ServerPlayer player, T payload);

	public abstract <T extends CustomPacketPayload> void registerC2SReceiver(CustomPacketPayload.Type<T> type,
																			 StreamCodec<? super RegistryFriendlyByteBuf, T> codec,
																			 NetworkReceiver<T> receiver);

	@Environment(EnvType.CLIENT)
	public abstract <T extends CustomPacketPayload> void registerS2CReceiver(CustomPacketPayload.Type<T> type,
																			 StreamCodec<? super RegistryFriendlyByteBuf, T> codec,
																			 NetworkReceiver<T> receiver);
}