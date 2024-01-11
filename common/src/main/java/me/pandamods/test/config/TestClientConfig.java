package me.pandamods.test.config;

import me.pandamods.pandalib.PandaLib;
import me.pandamods.pandalib.config.api.Config;
import me.pandamods.pandalib.config.api.ConfigData;
import me.pandamods.pandalib.config.api.ConfigType;

@Config(name = "client_test", modId = PandaLib.MOD_ID, synchronize = true)
public class TestClientConfig implements ConfigData {
	public String testString = "";
}
