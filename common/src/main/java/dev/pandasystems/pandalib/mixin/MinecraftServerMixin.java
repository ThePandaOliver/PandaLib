package dev.pandasystems.pandalib.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import dev.pandasystems.pandalib.event.events.server.ServerLevelEventsKt;
import dev.pandasystems.pandalib.event.events.server.ServerLifecycleEventsKt;
import dev.pandasystems.pandalib.event.events.server.ServerTickEventsKt;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BooleanSupplier;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin {
	@Shadow
	private MinecraftServer.ReloadableResources resources;

	@Unique
	protected final AtomicBoolean startupReady = new AtomicBoolean(false);

	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;initServer()Z"), method = "runServer")
	private void beforeSetupServer(CallbackInfo info) {
		ServerLifecycleEventsKt.getServerStarting().getInvoke().invoke((MinecraftServer) (Object) this);
	}

	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;buildServerStatus()Lnet/minecraft/network/protocol/status/ServerStatus;", ordinal = 0), method = "runServer")
	private void afterSetupServer(CallbackInfo info) {
		ServerLifecycleEventsKt.getServerStarted().getInvoke().invoke((MinecraftServer) (Object) this);
		afterServerStartedEvent();
	}

	@Inject(at = @At("HEAD"), method = "stopServer")
	private void beforeShutdownServer(CallbackInfo info) {
		ServerLifecycleEventsKt.getServerStopping().getInvoke().invoke((MinecraftServer) (Object) this);
	}

	@Inject(at = @At("TAIL"), method = "stopServer")
	private void afterShutdownServer(CallbackInfo info) {
		ServerLifecycleEventsKt.getServerStopped().getInvoke().invoke((MinecraftServer) (Object) this);
	}

	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;tickChildren(Ljava/util/function/BooleanSupplier;)V"), method = "tickServer")
	private void onStartTick(BooleanSupplier shouldKeepTicking, CallbackInfo ci) {
		ServerTickEventsKt.getStartServerTick().getInvoke().invoke((MinecraftServer) (Object) this);
	}

	@Inject(at = @At("TAIL"), method = "tickServer")
	private void onEndTick(BooleanSupplier shouldKeepTicking, CallbackInfo info) {
		ServerTickEventsKt.getEndServerTick().getInvoke().invoke((MinecraftServer) (Object) this);
	}

	@WrapOperation(method = "createLevels", at = @At(value = "INVOKE", target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"))
	private <K, V> V onLoadWorld(Map<K, V> levels, K dimension, V level, Operation<V> original) {
		final V result = original.call(levels, dimension, level);
		ServerLevelEventsKt.getServerLevelLoad().getInvoke().invoke((MinecraftServer) (Object) this, (ServerLevel) level);

		return result;
	}

	@Inject(method = "stopServer", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;close()V"))
	private void onUnloadWorldAtShutdown(CallbackInfo ci, @Local(name = "level") ServerLevel level) {
		ServerLevelEventsKt.getServerLevelUnLoad().getInvoke().invoke((MinecraftServer) (Object) this, level);
	}

	@Unique
	protected void afterServerStartedEvent() {
		if (this.startupReady.getAndSet(true)) {
			throw new IllegalStateException("PandaLib: Server is already marked as started");
		}
	}
}
