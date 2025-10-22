/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.mixin.protocols;

import dev.pandasystems.pandalib.networking.packets.ClientboundPLPayloadPacketKt;
import dev.pandasystems.pandalib.networking.packets.ServerboundPLPayloadPacketKt;
import dev.pandasystems.pandalib.networking.packets.bundle.*;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.BundlerInfo;
import net.minecraft.network.protocol.PacketType;
import net.minecraft.network.protocol.ProtocolInfoBuilder;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ServerGamePacketListener;
import net.minecraft.network.protocol.login.LoginProtocols;
import net.minecraft.util.Unit;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LoginProtocols.class)
public class LoginProtocolsMixin {
	@Inject(method = "method_56018", at = @At("RETURN"))
	private static void addClientPacket(ProtocolInfoBuilder<ClientGamePacketListener, RegistryFriendlyByteBuf, Unit> protocolInfoBuilder, CallbackInfo ci) {
		var type = ClientboundPLBundlePacketKt.getClientboundPLBundleType();
		var bundlerPacket = new ClientboundPLBundleDelimiterPacket();

		StreamCodec<ByteBuf, ClientboundPLBundleDelimiterPacket> streamCodec = StreamCodec.unit(bundlerPacket);
		PacketType<ClientboundPLBundleDelimiterPacket> packetType = bundlerPacket.type();
		protocolInfoBuilder.codecs.add(new ProtocolInfoBuilder.CodecEntry<>(packetType, streamCodec, null));
		protocolInfoBuilder.bundlerInfo = BundlerInfo.createForPacket(type, ClientboundPLBundlePacket::new, bundlerPacket);

		protocolInfoBuilder.addPacket(
				ClientboundPLPayloadPacketKt.getClientboundPLPayloadPacketType(),
				ClientboundPLPayloadPacketKt.getClientboundPLPayloadCodec()
		);
	}

	@Inject(method = "method_56019", at = @At("RETURN"))
	private static void addServerPacket(ProtocolInfoBuilder<ServerGamePacketListener, RegistryFriendlyByteBuf, Unit> protocolInfoBuilder, CallbackInfo ci) {
		var type = ServerboundPLBundlePacketKt.getServerboundPLBundleType();
		var bundlerPacket = new ServerboundPLBundleDelimiterPacket();

		StreamCodec<ByteBuf, ServerboundPLBundleDelimiterPacket> streamCodec = StreamCodec.unit(bundlerPacket);
		PacketType<ServerboundPLBundleDelimiterPacket> packetType = bundlerPacket.type();
		protocolInfoBuilder.codecs.add(new ProtocolInfoBuilder.CodecEntry<>(packetType, streamCodec, null));
		protocolInfoBuilder.bundlerInfo = BundlerInfo.createForPacket(type, ServerboundPLBundlePacket::new, bundlerPacket);

		protocolInfoBuilder.addPacket(
				ServerboundPLPayloadPacketKt.getServerboundPLPayloadPacketType(),
				ServerboundPLPayloadPacketKt.getServerboundPLPayloadCodec()
		);
	}
}
