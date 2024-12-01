/*
 * Copyright (C) 2024 Oliver Froberg (The Panda Oliver)
 *
 * This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 * You should have received a copy of the GNU Lesser General Public License
 *  along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package me.pandamods.test;

import com.mojang.blaze3d.platform.InputConstants;
import dev.architectury.event.EventResult;
import dev.architectury.event.events.client.ClientLifecycleEvent;
import dev.architectury.event.events.client.ClientScreenInputEvent;
import dev.architectury.event.events.client.ClientTickEvent;
import dev.architectury.registry.client.keymappings.KeyMappingRegistry;
import me.pandamods.pandalib.config.PandaLibConfig;
import me.pandamods.pandalib.config.holders.ClientConfigHolder;
import me.pandamods.pandalib.config.holders.CommonConfigHolder;
import me.pandamods.test.client.screen.TestScreen;
import me.pandamods.test.config.ClientTestConfig;
import me.pandamods.test.config.CommonTestConfig;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;

public class TestMod {
	public static final String MOD_ID = "testmod";
	private static TestMod instance;

	private static final CommonConfigHolder<CommonTestConfig> COMMON_TEST_CONFIG = PandaLibConfig.registerCommon(CommonTestConfig.class);
	private static final ClientConfigHolder<ClientTestConfig> CLIENT_TEST_CONFIG = PandaLibConfig.registerClient(ClientTestConfig.class);

	public static final KeyMapping TEST_SCREEN_KEYMAPPING = new KeyMapping(
			"key.testmod.test_screen",
			InputConstants.Type.KEYSYM,
			InputConstants.KEY_M,
			"key.categories.testmod"
	);

	public TestMod() {
		instance = this;

		KeyMappingRegistry.register(TEST_SCREEN_KEYMAPPING);
		ClientScreenInputEvent.KEY_PRESSED_POST.register((minecraft, screen, i, i1, i2) -> {
			if (TEST_SCREEN_KEYMAPPING.matches(i, i1)) {
				minecraft.setScreen(new TestScreen());
			}
			return EventResult.pass();
		});
	}

	public static ResourceLocation resourceLocation(String path) {
		return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
	}

	public static TestMod getInstance() {
		return instance;
	}
}
