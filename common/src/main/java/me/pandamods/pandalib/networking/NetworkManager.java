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

import io.netty.buffer.ByteBuf;
import me.pandamods.pandalib.platform.NetworkHandler;
import me.pandamods.pandalib.platform.Services;
#if MC_VER > MC_1_20_5
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
#endif
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class NetworkManager {
	public static final NetworkHandler INSTANCE = Services.PLATFORM.getNetwork();

	#if MC_VER > MC_1_20_5
	public static <T extends CustomPacketPayload> void sendToServer(T payload) {
		INSTANCE.sendToServer(payload);
	}

	public static <T extends CustomPacketPayload> void sendToPlayer(ServerPlayer player, T payload) {
		INSTANCE.sendToPlayer(player, payload);
	}

	public static <T extends CustomPacketPayload> void sendToPlayers(Iterable<ServerPlayer> players, T payload) {
		for (ServerPlayer player : players) {
			sendToPlayer(player, payload);
		}
	}

	public static <T extends CustomPacketPayload> void registerC2SReceiver(CustomPacketPayload.Type<T> type,
																		   StreamCodec<? super RegistryFriendlyByteBuf, T> codec,
																		   NetworkReceiver<T> receiver) {
		INSTANCE.registerC2SReceiver(type, codec, receiver);
	}

	public static <T extends CustomPacketPayload> void registerS2CReceiver(CustomPacketPayload.Type<T> type,
																		   StreamCodec<? super RegistryFriendlyByteBuf, T> codec,
																		   NetworkReceiver<T> receiver) {
		INSTANCE.registerS2CReceiver(type, codec, receiver);
	}
	#else
	public static void sendToServer(ResourceLocation packetId, ByteBuf buf) {
		INSTANCE.sendToServer(packetId, buf);
	}

	public static void sendToPlayer(ServerPlayer player, ResourceLocation packetId, ByteBuf buf) {
		INSTANCE.sendToPlayer(player, packetId, buf);
	}

	public static void sendToPlayers(Iterable<ServerPlayer> players, ResourceLocation packetId, ByteBuf buf) {
		for (ServerPlayer player : players) {
			sendToPlayer(player, packetId, buf);
		}
	}

	public static void registerC2SReceiver(ResourceLocation packetId, NetworkReceiver receiver) {
		INSTANCE.registerC2SReceiver(packetId, receiver);
	}

	public static void registerS2CReceiver(ResourceLocation packetId, NetworkReceiver receiver) {
		INSTANCE.registerS2CReceiver(packetId, receiver);
	}
	#endif
}