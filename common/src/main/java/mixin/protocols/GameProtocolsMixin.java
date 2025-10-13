/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.mixin.protocols;

import dev.pandasystems.pandalib.networking.packets.*;
import dev.pandasystems.pandalib.networking.packets.bundle.*;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.ProtocolInfoBuilder;
import net.minecraft.network.protocol.game.*;
import net.minecraft.util.Unit;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameProtocols.class)
public class GameProtocolsMixin {
	@Inject(method = "method_55958", at = @At("RETURN"))
	private static void addClientPacket(ProtocolInfoBuilder<ClientGamePacketListener, RegistryFriendlyByteBuf, Unit> protocolInfoBuilder, CallbackInfo ci) {
		protocolInfoBuilder.addPacket(
				ClientboundPLPayloadPacketKt.getClientboundPLPayloadPacketType(),
				ClientboundPLPayloadPacketKt.getClientboundPLPayloadCodec()
		);
	}

	@Inject(method = "method_55959", at = @At("RETURN"))
	private static void addServerPacket(ProtocolInfoBuilder<ServerGamePacketListener, RegistryFriendlyByteBuf, Unit> protocolInfoBuilder, CallbackInfo ci) {
		protocolInfoBuilder.withBundlePacket(
				ServerboundBundlePacketKt.getServerboundPLBundleType(), ServerboundPLBundlePacket::new, new ServerboundPLBundleDelimiterPacket()
		);
		protocolInfoBuilder.addPacket(
				ServerboundPLPayloadPacketKt.getServerboundPLPayloadPacketType(),
				ServerboundPLPayloadPacketKt.getServerboundPLPayloadCodec()
		);
	}
}
