package me.pandamods.pandalib.mixin.networking;

import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ServerGamePacketListenerImpl.class, priority = 1001)
public class ServerGamePacketListenerImplMixin {
	@Inject(method = "handleCustomPayload", at = @At("HEAD"))
	public void handleCustomPayload(ServerboundCustomPayloadPacket packet, CallbackInfo ci) {
		System.out.println("Server Payload");
		System.out.println(packet.payload().type().id());
	}
}
