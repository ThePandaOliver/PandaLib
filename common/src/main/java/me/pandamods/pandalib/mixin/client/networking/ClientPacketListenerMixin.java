package me.pandamods.pandalib.mixin.client.networking;

import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundCustomPayloadPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public class ClientPacketListenerMixin {
	@Inject(method = "<init>", at = @At("RETURN"))
	public void onInit(CallbackInfo ci) {
	}

	@Inject(method = "handleCustomPayload", at =  @At("HEAD"), cancellable = true)
	public void handleCustomPayload(ClientboundCustomPayloadPacket packet, CallbackInfo ci) {
	}
}
