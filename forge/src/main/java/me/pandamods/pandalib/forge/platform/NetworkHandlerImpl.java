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

package me.pandamods.pandalib.forge.platform;

import io.netty.buffer.Unpooled;
import me.pandamods.pandalib.PandaLib;
import me.pandamods.pandalib.forge.platform.hooks.EventBusesHooks;
import me.pandamods.pandalib.networking.NetworkHandler;
import me.pandamods.pandalib.networking.NetworkReceiver;
import me.pandamods.pandalib.networking.PacketContext;
import me.pandamods.pandalib.utils.Env;
import net.minecraft.client.Minecraft;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.Connection;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.network.ChannelBuilder;
import net.minecraftforge.network.EventNetworkChannel;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class NetworkHandlerImpl extends NetworkHandler {
	private final Map<CustomPacketPayload.Type<?>, Message<?>> CHANNELS = new HashMap<>();

	@SuppressWarnings("unchecked")
	@Override
	public <T extends CustomPacketPayload> void sendToServer(T payload) {
		Message<T> message = (Message<T>) CHANNELS.get(payload.type());
		if (message != null) {
			EventNetworkChannel channel = message.channel();
			Connection connection = Minecraft.getInstance().getConnection().getConnection();

			if (channel.isRemotePresent(connection)) {
				RegistryFriendlyByteBuf buf = new RegistryFriendlyByteBuf(Unpooled.buffer(), Minecraft.getInstance().level.registryAccess());
				message.encoder().accept(payload, buf);
				channel.send(buf, connection);
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends CustomPacketPayload> void sendToPlayer(ServerPlayer player, T payload) {
		Message<T> message = (Message<T>) CHANNELS.get(payload.type());
		if (message != null) {
			EventNetworkChannel channel = message.channel();
			Connection connection = player.connection.getConnection();

			if (channel.isRemotePresent(connection)) {
				RegistryFriendlyByteBuf buf = new RegistryFriendlyByteBuf(Unpooled.buffer(), Minecraft.getInstance().level.registryAccess());
				message.encoder().accept(payload, buf);
				channel.send(buf, connection);
			}
		}
	}

	@Override
	public <T extends CustomPacketPayload> void registerC2SReceiver(CustomPacketPayload.Type<T> type,
																	StreamCodec<? super RegistryFriendlyByteBuf, T> codec,
																	NetworkReceiver<T> receiver) {
		registerReceiver(type, codec, receiver);
	}

	@Override
	public <T extends CustomPacketPayload> void registerS2CReceiver(CustomPacketPayload.Type<T> type,
																	StreamCodec<? super RegistryFriendlyByteBuf, T> codec,
																	NetworkReceiver<T> receiver) {
		registerReceiver(type, codec, receiver);
	}

	private <T extends CustomPacketPayload> void registerReceiver(CustomPacketPayload.Type<T> type,
																  StreamCodec<? super RegistryFriendlyByteBuf, T> codec,
																  NetworkReceiver<T> receiver) {
		if (!CHANNELS.containsKey(type)) {
			var channel = ChannelBuilder.named(type.id()).optional().eventNetworkChannel()
					.addListener(event -> {
						RegistryFriendlyByteBuf buf = new RegistryFriendlyByteBuf(event.getPayload(), Minecraft.getInstance().level.registryAccess());
						T message = codec.decode(buf);
						receiver.receive(message, context(event.getSource().getSender(), event.getSource(), event.getSource().isClientSide()));
					});
			CHANNELS.put(type, new Message<>(channel, (payload, byteBuf) -> codec.encode(byteBuf, (T) payload)));
		}
	}

	private PacketContext context(Player player, CustomPayloadEvent.Context taskQueue, boolean client) {
		return new PacketContext() {
			@Override
			public Player getPlayer() {
				return player;
			}

			@Override
			public void queue(Runnable runnable) {
				taskQueue.enqueueWork(runnable);
			}

			@Override
			public Env getEnvironment() {
				return client ? Env.CLIENT : Env.SERVER;
			}

			@Override
			public RegistryAccess registryAccess() {
				return player.registryAccess();
			}
		};
	}

	public record Message<T extends CustomPacketPayload>(EventNetworkChannel channel, BiConsumer<T, RegistryFriendlyByteBuf> encoder) {}
}
