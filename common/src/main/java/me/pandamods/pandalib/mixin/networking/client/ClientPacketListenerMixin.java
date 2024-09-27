package me.pandamods.pandalib.mixin.networking.client;

import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ClientPacketListener.class, priority = 1001)
public class ClientPacketListenerMixin {
	@Inject(method = "handleCustomPayload", at = @At("HEAD"))
	public void handleCustomPayload(CustomPacketPayload payload, CallbackInfo ci) {
		System.out.println("Client Payload");
		System.out.println(payload.type().id());
	}
}
