package me.pandamods.test.config;

import me.pandamods.pandalib.PandaLib;
import me.pandamods.pandalib.config.api.Config;
import me.pandamods.pandalib.config.api.ConfigData;

@Config(name = "client_test", modId = PandaLib.MOD_ID, synchronize = true)
public class TestClientConfig implements ConfigData {
	public String text = "";
	public boolean aBoolean = false;
	public int anInt = 0;
	public float aFloat = 0f;
	public double aDouble = 0f;
	public TestEnum anEnum = TestEnum.OPTION1;

	public enum TestEnum {
		OPTION1,
		OPTION2,
		OPTION3
	}
}
