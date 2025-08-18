/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */
package dev.pandasystems.pandalib.core.interfaces

import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.resources.ResourceLocation

/*
 * The following code were copied from the Fabric API project
 * Copyright (c) 2016, 2017, 2018, 2019 FabricMC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Original source: https://github.com/FabricMC/fabric/blob/1.21.6/fabric-networking-api-v1/src/main/java/net/fabricmc/fabric/impl/networking/CustomPayloadTypeProvider.java
 */
interface CustomPayloadTypeProvider<B : FriendlyByteBuf> {
	fun get(byteBuf: B, resourceLocation: ResourceLocation): CustomPacketPayload.TypeAndCodec<B, out CustomPacketPayload>?
}
