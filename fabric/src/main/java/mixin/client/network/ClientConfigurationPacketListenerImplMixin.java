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

package dev.pandasystems.pandalib.fabric.mixin.client.network;

import dev.pandasystems.pandalib.mixin.client.network.ClientConfigurationPacketListenerImplKtImpl;
import dev.pandasystems.pandalib.networking.ClientConfigurationNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientConfigurationPacketListenerImpl;
import net.minecraft.client.multiplayer.CommonListenerCookie;
import net.minecraft.network.Connection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientConfigurationPacketListenerImpl.class)
public class ClientConfigurationPacketListenerImplMixin {
	@Unique
	private ClientConfigurationPacketListenerImplKtImpl pandaLib$impl = new ClientConfigurationPacketListenerImplKtImpl();
	
	@Inject(method = "<init>", at = @At("RETURN"))
	public void onInit(Minecraft minecraft, Connection connection, CommonListenerCookie commonListenerCookie, CallbackInfo ci) {
		pandaLib$impl.onInit(connection);
	}
}
