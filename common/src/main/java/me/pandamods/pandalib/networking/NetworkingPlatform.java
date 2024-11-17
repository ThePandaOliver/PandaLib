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

package me.pandamods.pandalib.networking;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public interface NetworkingPlatform {
	<T extends CustomPacketPayload> void registerC2SReceiver(CustomPacketPayload.Type<T> type, NetworkReceiver<T> receiver);
	<T extends CustomPacketPayload> void registerS2CReceiver(CustomPacketPayload.Type<T> type, NetworkReceiver<T> receiver);
	<T extends CustomPacketPayload> void registerBothReceiver(CustomPacketPayload.Type<T> type, NetworkReceiver<T> receiver);
}
