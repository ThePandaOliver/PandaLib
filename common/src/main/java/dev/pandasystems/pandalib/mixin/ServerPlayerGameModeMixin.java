package dev.pandasystems.pandalib.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import dev.pandasystems.pandalib.core.handles.player.PlayerHandleKt;
import dev.pandasystems.pandalib.event.events.ServerPlayerEventsKt;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayerGameMode;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

@Mixin(ServerPlayerGameMode.class)
public class ServerPlayerGameModeMixin {
	@Final
	@Shadow
	protected ServerPlayer player;

	@Shadow
	protected ServerLevel level;

	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Block;playerWillDestroy(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/entity/player/Player;)Lnet/minecraft/world/level/block/state/BlockState;"), method = "destroyBlock", cancellable = true)
	private void breakBlock(BlockPos pos, CallbackInfoReturnable<Boolean> cir, @Local(name = "blockEntity") BlockEntity blockEntity, @Local(name = "state") BlockState state) {
		boolean result = ServerPlayerEventsKt.getPlayerBlockBreakBefore().getInvoke().invoke(this.level, PlayerHandleKt.handle(this.player), pos, state, blockEntity);

		if (!result) {
			ServerPlayerEventsKt.getPlayerBlockBreakCanceled().getInvoke().invoke(this.level, PlayerHandleKt.handle(this.player), pos, state, blockEntity);

			cir.setReturnValue(false);
		}
	}

	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Block;destroy(Lnet/minecraft/world/level/LevelAccessor;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)V"), method = "destroyBlock")
	private void onBlockBroken(BlockPos pos, CallbackInfoReturnable<Boolean> cir, @Local(name = "blockEntity") BlockEntity blockEntity, @Local(name = "adjustedState") BlockState adjustedState) {
		ServerPlayerEventsKt.getPlayerBlockBreakAfter().getInvoke().invoke(this.level, PlayerHandleKt.handle(this.player), pos, adjustedState, blockEntity);
	}
}
