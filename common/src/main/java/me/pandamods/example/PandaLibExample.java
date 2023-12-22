package me.pandamods.example;

import me.pandamods.example.config.ClientConfig;
import me.pandamods.pandalib.config.ConfigHolder;
import me.pandamods.pandalib.config.ConfigRegistry;

public class PandaLibExample {
    public static final String MOD_ID = "pandalib_example";

	public static ConfigHolder<ClientConfig> CLIENT_CONFIG = ConfigRegistry.register(ClientConfig.class);
    
    public static void init() {
    }
}
