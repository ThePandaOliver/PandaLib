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

package me.pandamods.pandalib.neoforge.networking;

import com.mojang.datafixers.util.Pair;
import dev.architectury.platform.Platform;
import me.pandamods.pandalib.networking.NetworkContext;
import me.pandamods.pandalib.networking.NetworkReceiver;
import me.pandamods.pandalib.networking.INetworkHelper;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.DirectionalPayloadHandler;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NetworkHelperImpl implements INetworkHelper {
	private final Map<CustomPacketPayload.Type<? super CustomPacketPayload>, NetworkRegistryEntry<? super CustomPacketPayload>> clientReceivers = new HashMap<>();
	private final Map<CustomPacketPayload.Type<? super CustomPacketPayload>, NetworkRegistryEntry<? super CustomPacketPayload>> serverReceivers = new HashMap<>();
	private final Map<CustomPacketPayload.Type<? super CustomPacketPayload>, NetworkRegistryEntry<? super CustomPacketPayload>> biReceivers = new HashMap<>();

	@Override
	public <T extends CustomPacketPayload> void registerC2SReceiver(CustomPacketPayload.Type<T> type,
																	StreamCodec<? super RegistryFriendlyByteBuf, T> codec,
																	NetworkReceiver<T> receiver) {
		serverReceivers.put(type, new NetworkRegistryEntry<>(type, codec, receiver));
	}

	@Override
	public <T extends CustomPacketPayload> void registerS2CReceiver(CustomPacketPayload.Type<T> type,
																	StreamCodec<? super RegistryFriendlyByteBuf, T> codec,
																	NetworkReceiver<T> receiver) {
		clientReceivers.put(type, new NetworkRegistryEntry<>(type, codec, receiver));
	}

	@Override
	public <T extends CustomPacketPayload> void registerBothReceiver(CustomPacketPayload.Type<T> type,
																	 StreamCodec<? super RegistryFriendlyByteBuf, T> codec,
																	 NetworkReceiver<T> receiver) {
		biReceivers.put(type, new NetworkRegistryEntry<>(type, codec, receiver));
	}

	public void registerPackets(final RegisterPayloadHandlersEvent event) {
		PayloadRegistrar registrar = event.registrar("1");

		clientReceivers.forEach((type, entry) -> {
			registrar.playToClient(entry.type(), entry.codec(), new DirectionalPayloadHandler<>(
					(arg, ctx) -> entry.receiver().receive(createContext(ctx.player()), arg),
					(arg, ctx) -> entry.receiver().receive(createContext(ctx.player()), arg)
			));
		});
	}

	private NetworkContext createContext(Player player) {
		return new NetworkContext(player, Platform.getEnvironment());
	}

	private record NetworkRegistryEntry<T extends CustomPacketPayload>(CustomPacketPayload.Type<T> type,
																	   StreamCodec<? super RegistryFriendlyByteBuf, T> codec,
																	   NetworkReceiver<T> receiver) {
	}
}
