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

package me.pandamods.test.config;

import me.pandamods.pandalib.PandaLib;
import me.pandamods.pandalib.api.config.Config;
import me.pandamods.pandalib.api.config.ConfigData;

@Config(name = "pandalib_test", modId = PandaLib.MOD_ID, synchronize = true)
public class TestConfig implements ConfigData {
	public CategoryA categoryA = new CategoryA();
	public CategoryB categoryB = new CategoryB();

	public static class CategoryA {
		public String string = "test";
		public boolean aBoolean = false;
		public int anInt = 0;
		public float aFloat = 0f;
		public double aDouble = 0f;
		public TestEnum anEnum = TestEnum.OPTION1;
	}

	public static class CategoryB extends CategoryA {
		public CategoryA categoryA = new CategoryA();
	}

	public enum TestEnum {
		OPTION1,
		OPTION2,
		OPTION3
	}
}
