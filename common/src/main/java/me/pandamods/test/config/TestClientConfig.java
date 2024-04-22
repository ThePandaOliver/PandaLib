package me.pandamods.test.config;

import me.pandamods.pandalib.PandaLib;
import me.pandamods.pandalib.api.annotation.Category;
import me.pandamods.pandalib.api.annotation.ConfigGUI;
import me.pandamods.pandalib.api.config.Config;
import me.pandamods.pandalib.api.config.ConfigData;

@Config(name = "client_test", modId = PandaLib.MOD_ID, synchronize = true)
public class TestClientConfig implements ConfigData {
	public String text = "test";
	public String text2 = "test";
	@Category("test")
	public String text3 = "test";
	@Category("test2")
	public String text4 = "test";
	public boolean aBoolean = false;
	public int anInt = 0;
	public float aFloat = 0f;
	public double aDouble = 0f;
	public TestEnum anEnum = TestEnum.OPTION1;
	public TestObject testObject = new TestObject();

	public static class TestObject {
		public String text = "test";
		public String text2 = "test";
		@Category("test")
		public String text3 = "test";
		@Category("test2")
		public String text4 = "test";
	}

	public enum TestEnum {
		OPTION1,
		OPTION2,
		OPTION3
	}
}
