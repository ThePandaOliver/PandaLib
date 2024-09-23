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

package me.pandamods.pandalib.platform;

import me.pandamods.pandalib.networking.NetworkReceiver;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
#if MC_VER > MC_1_20_5
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
#endif
import net.minecraft.server.level.ServerPlayer;

public abstract class NetworkHandler {
	#if MC_VER > MC_1_20_5
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
	#else
	public abstract void sendToServer(ResourceLocation packetId, ByteBuf buf);
	public abstract void sendToPlayer(ServerPlayer player, ResourceLocation packetId, ByteBuf buf);

	public abstract void registerC2SReceiver(ResourceLocation packetId, NetworkReceiver receiver);

	@Environment(EnvType.CLIENT)
	public abstract void registerS2CReceiver(ResourceLocation packetId, NetworkReceiver receiver);
	#endif
}