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

#if MC_VER >= MC_1_20_5
package me.pandamods.pandalib.core.network.packets;

import io.netty.buffer.ByteBuf;
import me.pandamods.pandalib.PandaLib;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record CommonConfigPacketData(String resourceLocation, String configJson) implements CustomPacketPayload {
	public static final Type<CommonConfigPacketData> TYPE = new Type<>(PandaLib.LOCATION("common_config_sync"));

	public static final StreamCodec<ByteBuf, CommonConfigPacketData> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.STRING_UTF8,
			CommonConfigPacketData::resourceLocation,
			ByteBufCodecs.STRING_UTF8,
			CommonConfigPacketData::configJson,
			CommonConfigPacketData::new
	);

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
#endif
