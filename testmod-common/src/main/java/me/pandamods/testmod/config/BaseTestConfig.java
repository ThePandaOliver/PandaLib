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

package me.pandamods.testmod.config;

import java.util.List;

public class BaseTestConfig {
	public String testString = "Hello World!";

	public boolean testBoolean = true;

	public double testDouble = 64d;
	public float testFloat = 32f;
	public long testLong = 16L;
	public int testInt = 8;
	public short testShort = 4;
	public byte testByte = 2;

	public TestObject testObject = new TestObject("Hello Again!", 32f);

	public String[] testStringArray = new String[] {
			"Hello",
			"World!"
	};
	public List<TestObject> testObjectList = List.of(
			new TestObject("List entry 1", 1.5f),
			new TestObject("List entry 2", 87.5f)
	);

	public static class TestObject {
		public TestObject(String string, float value) {
			testString = string;
			testFloat = value;
		}

		public String testString;
		public float testFloat;
	}
}
