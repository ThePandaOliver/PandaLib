/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.forge.mixin;

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

	@Inject(method = "vanillaStart", at = @At("HEAD"), remap = false)
	public void runConfiguration(CallbackInfo ci) {
		ServerConfigurationConnectionEventsKt.getServerConfigurationConnectionEvent().getInvoker()
				.invoke((ServerConfigurationPacketListenerImpl) (Object) this, this.server);
	}
}
