/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.mixin.events;

import dev.pandasystems.pandalib.event.server.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public class MinecraftServerEventMixin {
	@Inject(method = "runServer", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;initServer()Z"))
	public void beforeServerInit(CallbackInfo ci) {
		var server = (MinecraftServer) (Object) this;
		ServerLifecycleEvents.getServerStartingEvent().getInvoker().invoke(server);
	}

	@Inject(method = "stopServer", at = @At("HEAD"))
	public void beforeServerShutdown(CallbackInfo ci) {
		var server = (MinecraftServer) (Object) this;
		ServerLifecycleEvents.getServerStoppingEvent().getInvoker().invoke(server);
	}
}
