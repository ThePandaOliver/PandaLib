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

package dev.pandasystems.pandalib.neoforge.mixin.client.multiplayer;

import com.llamalad7.mixinextras.sugar.Local;
import dev.pandasystems.pandalib.mixin.client.multiplayer.MultiPlayerGameModeKtImpl;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MultiPlayerGameMode.class)
public class MultiPlayerGameModeMixin {
    @Inject(
            method = "destroyBlock", 
            at = @At(
                    value = "INVOKE", 
                    target = "Lnet/minecraft/world/level/block/Block;destroy(Lnet/minecraft/world/level/LevelAccessor;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)V"
            )
    )
    void clientOnBlockDestroyedEvent(BlockPos pos, CallbackInfoReturnable<Boolean> cir, @Local(ordinal = 0) Level level, @Local(ordinal = 0) BlockState state) {
        MultiPlayerGameModeKtImpl.INSTANCE.clientOnBlockDestroyedEvent(level, pos, state);
    }
}
