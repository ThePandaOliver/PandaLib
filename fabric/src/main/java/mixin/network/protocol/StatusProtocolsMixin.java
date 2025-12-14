/*
 * Copyright (C) 2025 Oliver Froberg (The Panda Oliver)
 *
 * This program is free software: you can redistribute it and/or modify 
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 * You should have received a copy of the GNU Lesser General Public License
 *  along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package dev.pandasystems.pandalib.fabric.mixin.network.protocol;

import dev.pandasystems.pandalib.mixin.network.protocol.StatusProtocolsKtImpl;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.ProtocolInfoBuilder;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ServerGamePacketListener;
import net.minecraft.network.protocol.status.StatusProtocols;
import net.minecraft.util.Unit;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(StatusProtocols.class)
public class StatusProtocolsMixin {
	@Unique
	private static StatusProtocolsKtImpl pandaLib$impl = new StatusProtocolsKtImpl();
	
	@Inject(method = "method_56029", at = @At("RETURN"))
	private static void addClientPacket(ProtocolInfoBuilder<ClientGamePacketListener, RegistryFriendlyByteBuf, Unit> protocolInfoBuilder, CallbackInfo ci) {
		pandaLib$impl.addClientPacket(protocolInfoBuilder);
	}

	@Inject(method = "method_56030", at = @At("RETURN"))
	private static void addServerPacket(ProtocolInfoBuilder<ServerGamePacketListener, RegistryFriendlyByteBuf, Unit> protocolInfoBuilder, CallbackInfo ci) {
		pandaLib$impl.addServerPacket(protocolInfoBuilder);
	}
}
