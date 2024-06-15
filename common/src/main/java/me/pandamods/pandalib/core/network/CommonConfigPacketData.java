package me.pandamods.pandalib.core.network;

import io.netty.buffer.ByteBuf;
import me.pandamods.pandalib.PandaLib;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record CommonConfigPacketData(String resourceLocation, String configJson) implements CustomPacketPayload {
	public static final Type<CommonConfigPacketData> TYPE =
			new Type<>(ResourceLocation.fromNamespaceAndPath(PandaLib.MOD_ID, "common_config_sync"));

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
