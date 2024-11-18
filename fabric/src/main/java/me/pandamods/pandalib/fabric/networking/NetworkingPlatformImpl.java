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

package me.pandamods.pandalib.fabric.networking;

import dev.architectury.platform.Platform;
import me.pandamods.pandalib.networking.NetworkContext;
import me.pandamods.pandalib.networking.NetworkReceiver;
import me.pandamods.pandalib.networking.NetworkingPlatform;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public class NetworkingPlatformImpl implements NetworkingPlatform {
	@Override
	public <T extends CustomPacketPayload> void registerC2SReceiver(CustomPacketPayload.Type<T> type,
																	StreamCodec<RegistryFriendlyByteBuf, T> codec,
																	NetworkReceiver<T> receiver) {
		PayloadTypeRegistry.playC2S().register(type, codec);
		ServerPlayNetworking.registerReceiver(type, (t, context) -> receiver.receive(createContext(null), t));
	}

	@Override
	public <T extends CustomPacketPayload> void registerS2CReceiver(CustomPacketPayload.Type<T> type,
																	StreamCodec<RegistryFriendlyByteBuf, T> codec,
																	NetworkReceiver<T> receiver) {
		PayloadTypeRegistry.playS2C().register(type, codec);
		ClientPlayNetworking.registerReceiver(type, (t, context) -> receiver.receive(createContext(context.player()), t));
	}

	@Override
	public <T extends CustomPacketPayload> void registerBothReceiver(CustomPacketPayload.Type<T> type,
																	 StreamCodec<RegistryFriendlyByteBuf, T> codec,
																	 NetworkReceiver<T> receiver) {

	}

	private NetworkContext createContext(LocalPlayer player) {
		return new NetworkContext(player, Platform.getEnvironment());
	}
}
