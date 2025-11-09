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

package dev.pandasystems.pandalib.neoforge.mixin.networking;

import dev.pandasystems.pandalib.event.server.ServerConfigurationConnectionEventsKt;
import net.minecraft.network.Connection;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.CommonListenerCookie;
import net.minecraft.server.network.ServerCommonPacketListenerImpl;
import net.minecraft.server.network.ServerConfigurationPacketListenerImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerConfigurationPacketListenerImpl.class)
public abstract class ServerConfigurationPacketListenerMixin extends ServerCommonPacketListenerImpl {
	public ServerConfigurationPacketListenerMixin(MinecraftServer server, Connection connection, CommonListenerCookie cookie) {
		super(server, connection, cookie);
	}

	@Inject(method = "runConfiguration", at = @At("HEAD"))
	public void runConfiguration(CallbackInfo ci) {
		ServerConfigurationConnectionEventsKt.getServerConfigurationConnectionEvent().getInvoker()
				.invoke((ServerConfigurationPacketListenerImpl) (Object) this, this.server);
	}
}
