package me.pandamods.pandalib.client.screen.api.config;

import me.pandamods.pandalib.client.screen.api.PLScreen;
import me.pandamods.pandalib.client.screen.api.Widget;
import me.pandamods.pandalib.config.api.Config;
import me.pandamods.pandalib.config.api.holders.ConfigHolder;
import me.pandamods.pandalib.utils.GuiUtils;
import me.pandamods.pandalib.utils.animation.interpolation.NumberInterpolator;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;

import java.awt.*;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ConfigOptionList extends Widget {
	private final Map<Field, OptionEntry> options = new HashMap<>();

	private final NumberInterpolator hoveredAlphaInterpolator = new NumberInterpolator(0).setDuration(1f);
	private final NumberInterpolator hoveredPositionInterpolator = new NumberInterpolator(0).setDuration(0.25f).skipFirst();
	private final NumberInterpolator hoveredSizeInterpolator = new NumberInterpolator(0).setDuration(0.25f).skipFirst();
	private OptionEntry hoveredOption;

	public ConfigOptionList(PLScreen screen, ConfigHolder<?> configHolder, Object config, Object defaultConfig) {
		super(screen);

		Class<?> configClass = config.getClass();
		Config definition = configHolder.getDefinition();
		for (Field field : configClass.getFields()) {
			options.put(field, new OptionEntry(this, definition, field, config, defaultConfig));
		}
	}

	@Override
	protected void init() {
		setPosition(0, 20);
		setScale(minecraft.getWindow().getGuiScaledWidth(), minecraft.getWindow().getGuiScaledHeight() - 60);

		int oy = 0;
		for (OptionEntry entry : options.values()) {
			entry.setY(oy);
			oy += entry.height();
			this.addWidgetPanel(entry);
		}

		super.init();
	}

	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
		GuiUtils.drawColor(guiGraphics, getX(), getY(), width(), height(), new Color(0, 0, 0, 75));

		hoveredPositionInterpolator.update();
		hoveredSizeInterpolator.update();
		hoveredAlphaInterpolator.update();

		hoveredAlphaInterpolator.setTarget(hoveredOption != null && hoveredOption.isHovered() ? 75 : 0);
		int alpha = hoveredAlphaInterpolator.getAsInt();
		if (alpha != 0) {
			int y = this.getY() + hoveredPositionInterpolator.getAsInt();
			int height = hoveredSizeInterpolator.getAsInt();
			GuiUtils.drawColor(guiGraphics, getX(), y, width(), 2, new Color(175, 175, 175, alpha));
			GuiUtils.drawColor(guiGraphics, getX(), y+2, width(), height-4, new Color(255, 255, 255, alpha));
			GuiUtils.drawColor(guiGraphics, getX(), y+height-2, width(), 2, new Color(75, 75, 75, alpha));
		}
		super.render(guiGraphics, mouseX, mouseY, partialTick);
	}

	@Override
	public void onMouseMove(double mouseX, double mouseY) {
		super.onMouseMove(mouseX, mouseY);
		findHoveredEntry().ifPresentOrElse(
				optionEntry -> {
					hoveredOption = optionEntry;
					hoveredPositionInterpolator.setTarget(optionEntry.getLocalY());
					hoveredSizeInterpolator.setTarget(optionEntry.height());
				}, () -> hoveredOption = null);
	}

	@Override
	public void updateNarration(NarrationElementOutput narrationElementOutput) {
	}

	public Optional<OptionEntry> findHoveredEntry() {
		return options.values().stream().filter(Widget::isHovered).findFirst();
	}

	public static class OptionEntry extends Widget {
		private final Config definition;
		private final Field field;
		private final Object config;
		private final Object defaultConfig;

		public OptionEntry(Widget parent, Config definition, Field field, Object config, Object defaultConfig) {
			super(parent);
			this.definition = definition;
			this.field = field;
			this.config = config;
			this.defaultConfig = defaultConfig;

			setHeight(30);
		}

		@Override
		public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
			super.render(guiGraphics, mouseX, mouseY, partialTick);
			renderName(guiGraphics, getX(), getY(), height());
		}

		@Override
		protected void init() {
			setWidth(minecraft.getWindow().getGuiScaledWidth());
			super.init();
		}

		protected void renderName(GuiGraphics guiGraphics, int x, int y, int height) {
			Font font = this.minecraft.font;
			Component name = getTitle();
			guiGraphics.drawString(font, name, x + 2,  y + (height - font.lineHeight) / 2, Color.white.getRGB());
		}

		protected Component getTitle() {
			return Component.translatable(
					String.format("config.%s.%s.option.%s.%s.title",
							definition.modId(), definition.name(), config.getClass().getSimpleName(), field.getName())
			);
		}

		@Override
		public void updateNarration(NarrationElementOutput narrationElementOutput) {
			narrationElementOutput.add(NarratedElementType.TITLE, getTitle());
		}
	}
}
