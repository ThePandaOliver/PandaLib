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

package dev.pandasystems.pandalib.fabric.mixin.server;

import dev.pandasystems.pandalib.event.server.ServerLifecycleEventsKt;
import dev.pandasystems.pandalib.mixin.server.MinecraftServerKtImpl;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {
	@Unique
	private MinecraftServerKtImpl pandaLib$impl = new MinecraftServerKtImpl((MinecraftServer) (Object) this);
	
	@Inject(method = "runServer", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;initServer()Z"))
	public void beforeServerInit(CallbackInfo ci) {
		pandaLib$impl.beforeServerInitEvent();
	}

	@Inject(method = "stopServer", at = @At("HEAD"))
	public void beforeServerShutdown(CallbackInfo ci) {
		pandaLib$impl.beforeServerShutdownEvent();
	}
}
