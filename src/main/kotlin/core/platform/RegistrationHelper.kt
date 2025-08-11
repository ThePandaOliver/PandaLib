/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.core.platform

import dev.pandasystems.pandalib.api.registry.deferred.DeferredObject
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.core.Registry
import net.minecraft.network.syncher.EntityDataSerializer
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.packs.PackType
import net.minecraft.server.packs.resources.PreparableReloadListener
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import java.util.function.Supplier

interface DeferredRegisterHelper {
	fun <T> registerObject(deferredObject: DeferredObject<out T>, supplier: Supplier<out T>)
	fun <T> registerNewRegistry(registry: Registry<T>)
}

interface ResourceLoaderHelper {
	fun registerReloadListener(packType: PackType, listener: PreparableReloadListener, id: ResourceLocation, dependencies: Collection<ResourceLocation>)
}

interface RendererRegistrationHelper {
	fun <R : Entity> registerEntityRenderer(type: EntityType<R>, provider: EntityRendererProvider<R>)
	fun <R : BlockEntity> registerBlockEntityRenderer(type: BlockEntityType<R>, provider: BlockEntityRendererProvider<R>)
}

interface RegistryRegistrations {
	val entityDataSerializers: Registry<EntityDataSerializer<*>>
}