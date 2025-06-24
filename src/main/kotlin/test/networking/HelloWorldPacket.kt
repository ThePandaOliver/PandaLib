/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.test.networking

import dev.pandasystems.pandalib.core.PandaLib
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload


val helloWorldPacketType = CustomPacketPayload.Type<HelloWorldPacket>(PandaLib.resourceLocation("hello_world"))

class HelloWorldPacket(val name: String) : CustomPacketPayload {
	override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> = helloWorldPacketType
}

val helloWorldPacketCodec: StreamCodec<FriendlyByteBuf, HelloWorldPacket> = StreamCodec.composite(ByteBufCodecs.STRING_UTF8, HelloWorldPacket::name, ::HelloWorldPacket)