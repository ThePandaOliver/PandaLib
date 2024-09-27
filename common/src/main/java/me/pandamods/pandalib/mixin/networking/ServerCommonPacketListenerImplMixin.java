package me.pandamods.pandalib.mixin.networking;

import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket;
import net.minecraft.server.network.ServerCommonPacketListenerImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerCommonPacketListenerImpl.class)
public class ServerCommonPacketListenerImplMixin {
	@Inject(method = "handleCustomPayload", at = @At("HEAD"))
	public void handleCustomPayload(ServerboundCustomPayloadPacket packet, CallbackInfo ci) {
		System.out.println("Server Packet");
		System.out.println(packet.payload().type().id());
	}
}
