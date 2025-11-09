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

package dev.pandasystems.pandalib.neoforge.mixin.events;

import dev.pandasystems.pandalib.event.server.ServerLifecycleEventsKt;
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
		ServerLifecycleEventsKt.getServerStartingEvent().getInvoker().invoke(server);
	}

	@Inject(method = "stopServer", at = @At("HEAD"))
	public void beforeServerShutdown(CallbackInfo ci) {
		var server = (MinecraftServer) (Object) this;
		ServerLifecycleEventsKt.getServerStoppingEvent().getInvoker().invoke(server);
	}
}
