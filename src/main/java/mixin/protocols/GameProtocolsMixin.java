/*
 * Copyright (C) 2025-2025 Oliver Froberg (The Panda Oliver)
 *
 * This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 * You should have received a copy of the GNU Lesser General Public License
 *  along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package dev.pandasystems.pandalib.mixin.protocols;

import dev.pandasystems.pandalib.networking.packets.ClientboundPLPayloadPacketKt;
import dev.pandasystems.pandalib.networking.packets.ServerboundPLPayloadPacketKt;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.BundlerInfo;
import net.minecraft.network.protocol.PacketType;
import net.minecraft.network.protocol.ProtocolInfoBuilder;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.GameProtocols;
import net.minecraft.network.protocol.game.ServerGamePacketListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameProtocols.class)
public class GameProtocolsMixin {
	@Inject(method = "method_55958", at = @At("RETURN"))
	private static void addClientPacket(ProtocolInfoBuilder<ClientGamePacketListener, RegistryFriendlyByteBuf> protocolInfoBuilder, CallbackInfo ci) {
		protocolInfoBuilder.addPacket(
				ClientboundPLPayloadPacketKt.getClientboundPLPayloadPacketType(),
				ClientboundPLPayloadPacketKt.getClientboundPLPayloadCodec()
		);
	}

	@Inject(method = "method_55959", at = @At("RETURN"))
	private static void addServerPacket(ProtocolInfoBuilder<ServerGamePacketListener, RegistryFriendlyByteBuf> protocolInfoBuilder, CallbackInfo ci) {
		protocolInfoBuilder.addPacket(
				ServerboundPLPayloadPacketKt.getServerboundPLPayloadPacketType(),
				ServerboundPLPayloadPacketKt.getServerboundPLPayloadCodec()
		);
	}
}
