package me.pandamods.pandalib;

import com.mojang.logging.LogUtils;
import me.pandamods.example.PandaLibExample;
import org.slf4j.Logger;

public class PandaLib {
    public static final String MOD_ID = "pandalib";
	public static final Logger LOGGER = LogUtils.getLogger();
    
    public static void init() {
		PandaLibExample.init();
    }
}
