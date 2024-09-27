package me.pandamods.pandalib.mixin.networking.client;

import net.minecraft.client.multiplayer.ClientCommonPacketListenerImpl;
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientCommonPacketListenerImpl.class)
public class ClientCommonPacketListenerMixin {
	@Inject(method = "handleCustomPayload(Lnet/minecraft/network/protocol/common/ClientboundCustomPayloadPacket;)V", at = @At("HEAD"))
	public void handleCustomPayload(ClientboundCustomPayloadPacket packet, CallbackInfo ci) {
		System.out.println("Client Packet");
		System.out.println(packet.payload().type().id());
	}
}
