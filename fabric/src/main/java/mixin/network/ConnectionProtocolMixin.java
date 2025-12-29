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

package dev.pandasystems.pandalib.fabric.mixin.network;

import dev.pandasystems.pandalib.mixin.network.ConnectionProtocolKtImpl;
import net.minecraft.network.ConnectionProtocol;
import net.minecraft.network.PacketListener;
import net.minecraft.network.protocol.PacketFlow;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ConnectionProtocol.ProtocolBuilder.class)
public class ConnectionProtocolMixin {
	@Inject(method = "addFlow", at = @At("HEAD"))
	public <T extends PacketListener> void addFlow(PacketFlow packetFlow, ConnectionProtocol.PacketSet<T> packetSet, CallbackInfoReturnable<ConnectionProtocol.ProtocolBuilder> cir) {
		ConnectionProtocolKtImpl.INSTANCE.addFlow(packetFlow, packetSet);
	}
}