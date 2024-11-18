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
import dev.architectury.utils.Env;
import me.pandamods.pandalib.networking.NetworkContext;
import me.pandamods.pandalib.networking.NetworkReceiver;
import me.pandamods.pandalib.networking.INetworkHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;

public class NetworkHelperImpl implements INetworkHelper {
	@Override
	public <T extends CustomPacketPayload> void registerC2SReceiver(CustomPacketPayload.Type<T> type,
																	StreamCodec<? super RegistryFriendlyByteBuf, T> codec,
																	NetworkReceiver<T> receiver) {
		PayloadTypeRegistry.playC2S().register(type, codec);
		ServerPlayNetworking.registerGlobalReceiver(type, (t, context) -> receiver.receive(createContext(context.player()), t));
	}

	@Override
	public <T extends CustomPacketPayload> void registerS2CReceiver(CustomPacketPayload.Type<T> type,
																	StreamCodec<? super RegistryFriendlyByteBuf, T> codec,
																	NetworkReceiver<T> receiver) {
		PayloadTypeRegistry.playS2C().register(type, codec);
		ClientPlayNetworking.registerGlobalReceiver(type, (t, context) -> receiver.receive(createContext(context.player()), t));
	}

	@Override
	public <T extends CustomPacketPayload> void registerBothReceiver(CustomPacketPayload.Type<T> type,
																	 StreamCodec<? super RegistryFriendlyByteBuf, T> codec,
																	 NetworkReceiver<T> receiver) {
		registerC2SReceiver(type, codec, receiver);
		if (Platform.getEnvironment() == Env.CLIENT)
			registerS2CReceiver(type, codec, receiver);
	}

	private NetworkContext createContext(Player player) {
		return new NetworkContext(player, Platform.getEnvironment());
	}
}
