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

package me.pandamods.test.register;

import me.pandamods.pandalib.registry.DeferredObject;
import me.pandamods.pandalib.registry.DeferredRegister;
import me.pandamods.test.TestMod;
import me.pandamods.test.entities.SandWorm;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public class TestEntityRegister {
	public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(TestMod.MOD_ID, Registries.ENTITY_TYPE);

	public static final DeferredObject<EntityType<SandWorm>> SAND_WORM = ENTITIES.register("sand_worm", resourceKey -> EntityType.Builder
			.of(SandWorm::new, MobCategory.MONSTER)
			.sized(0.75F, 0.75F)
			.build(resourceKey));
}
