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

package me.pandamods.pandalib.api.util;

import dev.architectury.networking.NetworkManager;
import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import net.minecraft.resources.ResourceLocation;
#if MC_VER >= MC_1_20_5
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
#endif

public class NetworkHelper {
	#if MC_VER >= MC_1_20_5
		public static <T extends CustomPacketPayload> void registerS2C(CustomPacketPayload.Type<T> packetType,
																	   StreamCodec<? super RegistryFriendlyByteBuf, T> codec,
																	   NetworkManager.NetworkReceiver<T> receiver) {
			if (Platform.getEnvironment().equals(Env.SERVER)) {
				NetworkManager.registerS2CPayloadType(packetType, codec);
			} else {
				NetworkManager.registerReceiver(NetworkManager.s2c(), packetType, codec, receiver);
			}
		}

		public static <T extends CustomPacketPayload> void registerC2S(CustomPacketPayload.Type<T> packetType,
																	   StreamCodec<? super RegistryFriendlyByteBuf, T> codec,
																	   NetworkManager.NetworkReceiver<T> receiver) {
			NetworkManager.registerReceiver(NetworkManager.c2s(), packetType, codec, receiver);
		}
	#else
		public static void registerS2C(ResourceLocation id, NetworkManager.NetworkReceiver receiver) {
			if (Platform.getEnvironment().equals(Env.CLIENT))
				NetworkManager.registerReceiver(NetworkManager.serverToClient(), id, receiver);
		}

		public static void registerC2S(ResourceLocation id, NetworkManager.NetworkReceiver receiver) {
			NetworkManager.registerReceiver(NetworkManager.clientToServer(), id, receiver);
		}
	#endif
}