/*
 * Copyright (C) 2024 Oliver Froberg (The Panda Oliver)
 *
 * This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 * You should have received a copy of the GNU General Public License
 *  along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package me.pandamods.test.client;

import dev.architectury.registry.client.level.entity.EntityRendererRegistry;
import me.pandamods.test.client.render.SandWormRenderer;
import me.pandamods.test.register.TestEntityRegister;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class TestModClient {
	private static TestModClient instance;

    public TestModClient() {
		instance = this;
		EntityRendererRegistry.register(TestEntityRegister.SAND_WORM, SandWormRenderer::new);
    }

	public static TestModClient getInstance() {
		return instance;
	}
}