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

package me.pandamods.pandalib.neoforge.platform;

import me.pandamods.pandalib.PandaLib;
import me.pandamods.pandalib.neoforge.platform.hooks.EventBusesHooks;
import me.pandamods.pandalib.networking.NetworkHandler;
import me.pandamods.pandalib.networking.NetworkReceiver;
import me.pandamods.pandalib.networking.PacketContext;
import me.pandamods.pandalib.utils.Env;
import net.minecraft.client.Minecraft;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class NetworkHandlerImpl extends NetworkHandler {
	@Override
	public <T extends CustomPacketPayload> void sendToServer(T payload) {
		PacketDistributor.sendToServer(payload);
	}

	@Override
	public <T extends CustomPacketPayload> void sendToPlayer(ServerPlayer player, T payload) {
		PacketDistributor.sendToPlayer(player, payload);
	}

	@Override
	public <T extends CustomPacketPayload> void registerC2SReceiver(CustomPacketPayload.Type<T> type,
																	StreamCodec<? super RegistryFriendlyByteBuf, T> codec,
																	NetworkReceiver<T> receiver) {
		EventBusesHooks.whenAvailable(PandaLib.MOD_ID, bus ->
				bus.<RegisterPayloadHandlersEvent>addListener(event ->
						event.registrar(type.id().getNamespace()).optional().playToServer(type, codec, (arg, context) ->
								receiver.receive(arg, context(context.player(), context, false))
						)
				)
		);
	}

	@Override
	public <T extends CustomPacketPayload> void registerS2CReceiver(CustomPacketPayload.Type<T> type,
									StreamCodec<? super RegistryFriendlyByteBuf, T> codec,
									NetworkReceiver<T> receiver) {
		EventBusesHooks.whenAvailable(PandaLib.MOD_ID, bus ->
				bus.<RegisterPayloadHandlersEvent>addListener(event ->
						event.registrar(type.id().getNamespace()).optional().playToClient(type, codec, (arg, context) ->
								receiver.receive(arg, context(context.player(), context, true))
						)
				)
		);
	}

	private PacketContext context(Player player, IPayloadContext taskQueue, boolean client) {
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
}
