package me.pandamods.pandalib.config;

import me.pandamods.pandalib.PandaLib;
import me.pandamods.pandalib.config.api.Config;
import me.pandamods.pandalib.config.api.ConfigType;

@Config(name = "client", modId = PandaLib.MOD_ID, parentDirectory = PandaLib.MOD_ID, type = ConfigType.CLIENT, synchronize = true)
public class ClientConfig {
	public String test_String = "";
}
