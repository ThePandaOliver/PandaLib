package me.pandamods.pandalib.config;

import me.pandamods.pandalib.PandaLib;
import me.pandamods.pandalib.config.api.Config;
import me.pandamods.pandalib.config.api.ConfigType;

@Config(name = "common", modId = PandaLib.MOD_ID, parentDirectory = PandaLib.MOD_ID, synchronize = true)
public class CommonConfig {
	public String test_String = "";
}
