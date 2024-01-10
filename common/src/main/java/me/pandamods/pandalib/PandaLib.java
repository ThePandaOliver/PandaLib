package me.pandamods.pandalib;

import com.mojang.logging.LogUtils;
import me.pandamods.pandalib.config.ClientConfig;
import me.pandamods.pandalib.config.CommonConfig;
import me.pandamods.pandalib.config.api.holders.ClientConfigHolder;
import me.pandamods.pandalib.config.api.holders.CommonConfigHolder;
import me.pandamods.pandalib.config.api.holders.ConfigHolder;
import me.pandamods.pandalib.config.api.ConfigRegistry;
import me.pandamods.pandalib.event.EventHandler;
import me.pandamods.pandalib.network.PacketHandler;
import org.slf4j.Logger;

public class PandaLib {
    public static final String MOD_ID = "pandalib";
	public static final Logger LOGGER = LogUtils.getLogger();

	public static final ClientConfigHolder<ClientConfig> CLIENT_CONFIG = ConfigRegistry.register(ClientConfig.class);
	public static final CommonConfigHolder<CommonConfig> COMMON_CONFIG = ConfigRegistry.register(CommonConfig.class);

    public static void init() {
		PacketHandler.init();
		EventHandler.init();
    }
}
