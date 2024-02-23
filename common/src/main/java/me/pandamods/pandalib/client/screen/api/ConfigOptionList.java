package me.pandamods.pandalib.client.screen.api;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;

import java.awt.*;
import java.lang.reflect.Field;

public class ConfigOptionList extends ContainerObjectSelectionList<ConfigOptionList.OptionEntry> {
	public ConfigOptionList(ConfigScreen screen, Minecraft minecraft) {
		super(minecraft, 0, 0, screen.width, screen.height, 40);
	}

	public static abstract class OptionEntry extends Entry<OptionEntry> {
		public final Minecraft minecraft;
		private final ResourceLocation nameLocation;
		private final Field field;
		private final Object config;
		private final Object defaultConfig;

		public OptionEntry(ResourceLocation nameLocation, Field field, Object config, Object defaultConfig) {
			this.nameLocation = nameLocation;
			this.field = field;
			this.config = config;
			this.defaultConfig = defaultConfig;
			this.minecraft = Minecraft.getInstance();
		}

		@Override
		public void render(GuiGraphics guiGraphics, int index, int top, int left, int width, int height, int mouseX, int mouseY,
						   boolean hovering, float partialTick) {
			renderName(guiGraphics);
		}

		protected void renderName(GuiGraphics guiGraphics, int top, int left, int width, int height) {
			Font font = this.minecraft.font;
			Component name = Component.translatable(getName() + ".title");
			guiGraphics.drawString(font, name, (height - font.lineHeight) / 2, left, Color.white.getRGB());
		}

		protected String getName() {
			String modId = nameLocation.getNamespace();
			String path = nameLocation.getPath();
			return "config." + modId + "." + path.replace('/', '.');
		}
	}
}
