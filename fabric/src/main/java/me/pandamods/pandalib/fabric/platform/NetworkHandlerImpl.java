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

package me.pandamods.pandalib.fabric.platform;

import me.pandamods.pandalib.networking.NetworkHandler;
import me.pandamods.pandalib.networking.NetworkReceiver;
import me.pandamods.pandalib.networking.PacketContext;
import me.pandamods.pandalib.utils.Env;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.thread.BlockableEventLoop;
import net.minecraft.world.entity.player.Player;

public class NetworkHandlerImpl extends NetworkHandler {
	@Override
	public <T extends CustomPacketPayload> void sendToServer(T payload) {
		ClientPlayNetworking.send(payload);
	}

	@Override
	public <T extends CustomPacketPayload> void sendToPlayer(ServerPlayer player, T payload) {
		ServerPlayNetworking.send(player, payload);
	}

	@Override
	public <T extends CustomPacketPayload> void registerC2SReceiver(CustomPacketPayload.Type<T> type,
																	StreamCodec<? super RegistryFriendlyByteBuf, T> codec,
																	NetworkReceiver<T> receiver) {
		PayloadTypeRegistry.playC2S().register(type, codec);
		ServerPlayNetworking.registerGlobalReceiver(type, (payload, fabricContext) -> {
			PacketContext context = context(fabricContext.player(), fabricContext.player().server, false);
			receiver.receive(payload, context);
		});
	}

	@Override
	public <T extends CustomPacketPayload> void registerS2CReceiver(CustomPacketPayload.Type<T> type,
																	StreamCodec<? super RegistryFriendlyByteBuf, T> codec,
																	NetworkReceiver<T> receiver) {
		PayloadTypeRegistry.playS2C().register(type, codec);
		ClientPlayNetworking.registerGlobalReceiver(type, new ClientPlayPayloadHandler<>(receiver));
	}

	@Environment(EnvType.CLIENT)
	class ClientPlayPayloadHandler<T extends CustomPacketPayload> implements ClientPlayNetworking.PlayPayloadHandler<T> {
		private final NetworkReceiver<T> receiver;

		ClientPlayPayloadHandler(NetworkReceiver<T> receiver) {
			this.receiver = receiver;
		}

		@Override
		public void receive(T payload, ClientPlayNetworking.Context fabricContext) {
			var context = context(fabricContext.player(), fabricContext.client(), true);
			receiver.receive(payload, context);
		}
	}

	private PacketContext context(Player player, BlockableEventLoop<?> taskQueue, boolean client) {
		return new PacketContext() {
			@Override
			public Player getPlayer() {
				return player;
			}

			@Override
			public void queue(Runnable runnable) {
				taskQueue.execute(runnable);
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
}
