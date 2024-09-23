package me.pandamods.pandalib.mixin.networking;

import me.pandamods.pandalib.mixin.networking.accessor.ServerboundCustomPayloadPacketAccessor;
import net.minecraft.network.protocol.game.ServerboundCustomPayloadPacket;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerGamePacketListenerImpl.class)
public class ServerGamePacketListenerImplMixin {
	@Inject(method = "<init>", at = @At("RETURN"))
	public void onInit(CallbackInfo ci) {
	}

	@Inject(method = "handleCustomPayload", at =  @At("HEAD"), cancellable = true)
	public void handleCustomPayload(ServerboundCustomPayloadPacket packet, CallbackInfo ci) {
		ServerboundCustomPayloadPacketAccessor accessor = (ServerboundCustomPayloadPacketAccessor) packet;
		System.out.println(accessor.getIdentifier());
	}
}
