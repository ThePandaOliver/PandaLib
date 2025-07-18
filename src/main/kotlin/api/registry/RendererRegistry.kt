/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */
@file:JvmName("RendererRegistry")

package dev.pandasystems.pandalib.api.registry

import dev.pandasystems.pandalib.core.platform.rendererRegistrationHelper
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType

@JvmName("entityRenderer")
fun <R : Entity> registerEntityRenderer(type: EntityType<R>, provider: EntityRendererProvider<R>) {
	rendererRegistrationHelper.registerEntityRenderer(type, provider)
}

@JvmName("blockEntityRenderer")
fun <R : BlockEntity> registerBlockEntityRenderer(type: BlockEntityType<R>, provider: BlockEntityRendererProvider<R>) {
	rendererRegistrationHelper.registerBlockEntityRenderer(type, provider)
}