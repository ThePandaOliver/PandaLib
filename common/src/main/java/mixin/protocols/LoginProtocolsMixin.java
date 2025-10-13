/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.mixin.protocols;

import dev.pandasystems.pandalib.networking.packets.ClientboundPLPayloadPacketKt;
import dev.pandasystems.pandalib.networking.packets.ServerboundPLPayloadPacketKt;
import net.minecraft.network.RegistryFriendlyByteBuf;
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
	private static void addClientPacket(ProtocolInfoBuilder<ClientGamePacketListener, RegistryFriendlyByteBuf> protocolInfoBuilder, CallbackInfo ci) {
		protocolInfoBuilder.addPacket(
				ClientboundPLPayloadPacketKt.getClientboundPLPayloadPacketType(),
				ClientboundPLPayloadPacketKt.getClientboundPLPayloadCodec()
		);
	}

	@Inject(method = "method_56019", at = @At("RETURN"))
	private static void addServerPacket(ProtocolInfoBuilder<ServerGamePacketListener, RegistryFriendlyByteBuf> protocolInfoBuilder, CallbackInfo ci) {
		protocolInfoBuilder.addPacket(
				ServerboundPLPayloadPacketKt.getServerboundPLPayloadPacketType(),
				ServerboundPLPayloadPacketKt.getServerboundPLPayloadCodec()
		);
	}
}
