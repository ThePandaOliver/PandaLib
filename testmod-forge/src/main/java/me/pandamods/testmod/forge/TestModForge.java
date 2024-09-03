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

package me.pandamods.testmod.forge;

import me.pandamods.pandalib.forge.platform.EventBuses;
import me.pandamods.testmod.TestMod;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(TestMod.MOD_ID)
public class TestModForge {
    public TestModForge() {
		EventBuses.registerModEventBus(TestMod.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
		TestMod.init();
    }
}
