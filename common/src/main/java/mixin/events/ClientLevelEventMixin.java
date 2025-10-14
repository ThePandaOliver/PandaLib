/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.mixin.events;

import dev.pandasystems.pandalib.event.client.ClientPlayerEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(ClientLevel.class)
public class ClientLevelEventMixin {
	@Shadow
	@Final
	private Minecraft minecraft;

	@Inject(method = "disconnect", at = @At("HEAD"))
	public void disconnect(CallbackInfo ci) {
		ClientPlayerEvents.getClientPlayerLeaveEvent().getInvoker().invoke(Objects.requireNonNull(this.minecraft.player));
	}
}
