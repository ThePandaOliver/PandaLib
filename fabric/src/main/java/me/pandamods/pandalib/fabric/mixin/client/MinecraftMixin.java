/*
 * Copyright (C) 2024 Oliver Froberg (The Panda Oliver)
 *
 * This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 * You should have received a copy of the GNU General Public License
 *  along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package me.pandamods.pandalib.fabric.mixin.client;

import me.pandamods.pandalib.event.events.common.ClientPlayerEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftMixin {
	@Shadow @Nullable
	public LocalPlayer player;

	@Inject(method = "disconnect(Lnet/minecraft/client/gui/screens/Screen;Z)V",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/client/GameNarrator;clear()V"))
	private void handleLogout(Screen nextScreen, boolean keepResourcePacks, CallbackInfo ci) {
		ClientPlayerEvents.PLAYER_QUIT.invoker().quit(player);
	}
}