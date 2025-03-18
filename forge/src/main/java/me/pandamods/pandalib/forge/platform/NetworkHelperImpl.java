/*
 * Copyright (C) 2025 Oliver Froberg (The Panda Oliver)
 *
 * This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 * You should have received a copy of the GNU Lesser General Public License
 *  along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package me.pandamods.pandalib.forge.platform;

import me.pandamods.pandalib.networking.NetworkContext;
import me.pandamods.pandalib.networking.NetworkReceiver;
import me.pandamods.pandalib.platform.services.NetworkHelper;
import me.pandamods.pandalib.utils.Env;
import me.pandamods.pandalib.utils.EnvRunner;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.*;
import net.minecraftforge.network.payload.PayloadConnection;

import java.util.HashMap;
import java.util.Map;

public class NetworkHelperImpl implements NetworkHelper {
	public static final Map<CustomPacketPayload.Type<?>, Channel<CustomPacketPayload>> CHANNELS = new HashMap<>();
	private int id = 0;

	@Override
	public <T extends CustomPacketPayload> void registerClientReceiver(CustomPacketPayload.Type<T> type,
	                                                                   StreamCodec<? super RegistryFriendlyByteBuf, T> codec,
	                                                                   NetworkReceiver<T> receiver) {
		EnvRunner.runIf(Env.CLIENT, () -> () -> registerChannel(type, codec, receiver));
	}

	@Override
	public <T extends CustomPacketPayload> void registerServerReceiver(CustomPacketPayload.Type<T> type,
	                                                                   StreamCodec<? super RegistryFriendlyByteBuf, T> codec,
	                                                                   NetworkReceiver<T> receiver) {
		registerChannel(type, codec, receiver);
	}

	@Override
	public <T extends CustomPacketPayload> void registerBiDirectionalReceiver(CustomPacketPayload.Type<T> type,
	                                                                          StreamCodec<? super RegistryFriendlyByteBuf, T> codec,
	                                                                          NetworkReceiver<T> clientReceiver,
	                                                                          NetworkReceiver<T> serverReceiver) {
		registerChannel(type, codec, (ctx, payload) -> {
			switch (ctx.getDirection()) {
				case CLIENT -> clientReceiver.receive(ctx, payload);
				case SERVER -> serverReceiver.receive(ctx, payload);
				default -> throw new IllegalStateException("Unexpected value: " + ctx.getDirection());
			}
		});
	}

	@SuppressWarnings("unchecked")
	private <T extends CustomPacketPayload> void registerChannel(CustomPacketPayload.Type<T> type,
	                                                             StreamCodec<? super RegistryFriendlyByteBuf, T> codec,
	                                                             NetworkReceiver<T> receiver) {
		if (CHANNELS.containsKey(type))
			throw new IllegalArgumentException(String.format("Networking channel '%s' has already been registered", type.id()));

		PayloadConnection<CustomPacketPayload> channelBuilder = ChannelBuilder.named(type.id())
				.optional()
				.payloadChannel();

		Channel<CustomPacketPayload> channel = channelBuilder.play()
				.bidirectional()
				.addMain(type, (StreamCodec<RegistryFriendlyByteBuf, T>) codec, (msg, ctx) -> receiver.receive(new NetworkContext(
						ctx.getSender(),
						ctx.isClientSide() ? Env.CLIENT : Env.SERVER
				), (T) msg))
				.build();
		CHANNELS.put(type, channel);
	}

	@Override
	public <T extends CustomPacketPayload> void sendToServer(T payload) {
		CHANNELS.get(payload.type()).send(payload, PacketDistributor.SERVER.noArg());
	}

	@Override
	public <T extends CustomPacketPayload> void sendToPlayer(ServerPlayer player, T payload) {
		CHANNELS.get(payload.type()).send(payload, PacketDistributor.PLAYER.with(player));
	}

	@Override
	public <T extends CustomPacketPayload> void sendToAllPlayers(T payload) {
		CHANNELS.get(payload.type()).send(payload, PacketDistributor.ALL.noArg());
	}
}
