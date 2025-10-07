/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.neoforge.mixin;

import dev.pandasystems.pandalib.PandaLibKt;
import dev.pandasystems.pandalib.event.serverevents.ServerConfigurationConnectionEvents;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.configuration.ServerConfigurationPacketListener;
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
		System.out.println(Thread.currentThread().getName());
		ServerConfigurationConnectionEvents.configure.getInvoker().invoke((ServerConfigurationPacketListenerImpl) (Object) this, this.server);
	}
}
