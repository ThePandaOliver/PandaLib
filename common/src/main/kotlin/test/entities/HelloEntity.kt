/*
 * Copyright (c) 2025. Oliver Froberg
 *
 * This code is licensed under the GNU Lesser General Public License v3.0
 * See: https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 */

package dev.pandasystems.pandalib.test.entities

import net.minecraft.nbt.CompoundTag
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.level.Level

class HelloEntity(entityType: EntityType<out HelloEntity>, level: Level) : Entity(entityType, level) {
	override fun defineSynchedData(builder: SynchedEntityData.Builder) {
	}

	override fun hurtServer(
		level: ServerLevel,
		damageSource: DamageSource,
		amount: Float
	): Boolean {
		return false
	}

	override fun readAdditionalSaveData(tag: CompoundTag) {
	}

	override fun addAdditionalSaveData(tag: CompoundTag) {
	}
}