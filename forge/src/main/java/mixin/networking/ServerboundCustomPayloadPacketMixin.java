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

package dev.pandasystems.pandalib.forge.mixin.networking;

import dev.pandasystems.pandalib.networking.CustomPacketPayload;
import dev.pandasystems.pandalib.networking.PayloadCodecRegistry;
import dev.pandasystems.pandalib.networking.ServerPlayNetworking;
import dev.pandasystems.pandalib.utils.codecs.StreamCodec;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ServerGamePacketListener;
import net.minecraft.network.protocol.game.ServerboundCustomPayloadPacket;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerboundCustomPayloadPacket.class)
public class ServerboundCustomPayloadPacketMixin {
	@Shadow
	@Final
	private ResourceLocation identifier;

	@Shadow
	@Final
	private FriendlyByteBuf data;

	@Inject(method = "handle(Lnet/minecraft/network/protocol/game/ServerGamePacketListener;)V", at = @At("HEAD"), cancellable = true)
	public void handle(ServerGamePacketListener handler, CallbackInfo ci) {
		if (ServerPlayNetworking.INSTANCE.getPacketHandlers$pandalib_common().containsKey(this.identifier)) {
			StreamCodec<@NotNull FriendlyByteBuf, @NotNull CustomPacketPayload> codec = PayloadCodecRegistry.INSTANCE.getPacketCodecs$pandalib_common().get(identifier);
			if (codec == null) throw new IllegalStateException("No codec registered for packet " + identifier);
			ServerPlayNetworking.INSTANCE.handlePayload(handler, codec.decode(this.data));
			ci.cancel();
		}
	}
}
