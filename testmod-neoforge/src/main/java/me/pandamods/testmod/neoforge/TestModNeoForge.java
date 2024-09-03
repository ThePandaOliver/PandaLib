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

package me.pandamods.testmod.neoforge;

import me.pandamods.testmod.TestMod;
import net.neoforged.fml.common.Mod;

@Mod(TestMod.MOD_ID)
public class TestModNeoForge {
    public TestModNeoForge() {
		TestMod.init();
    }
}
