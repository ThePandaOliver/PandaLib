package me.pandamods.pandalib.client.screen.api.config;

import me.pandamods.pandalib.client.screen.api.PLScreen;
import me.pandamods.pandalib.client.screen.api.Widget;
import me.pandamods.pandalib.config.api.Config;
import me.pandamods.pandalib.config.api.holders.ConfigHolder;
import me.pandamods.pandalib.utils.animation.interpolation.FloatInterpolator;
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

	private FloatInterpolator hoveredPositionInterpolator = new FloatInterpolator(0);
	private boolean hoveringOption = false;

	public ConfigOptionList(PLScreen screen, ConfigHolder<?> configHolder, Object config, Object defaultConfig) {
		super(screen);

		hoveredPositionInterpolator.setDuration(3);

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

		int oy = getLocalY();
		for (OptionEntry entry : options.values()) {
			entry.setY(oy);
			oy += entry.height();
			this.addWidgetPanel(entry);
		}

		super.init();
	}

	@Override
	protected void renderWidget(GuiGraphics guiGraphics, float partialTick) {
		guiGraphics.fill(minX(), minY(), maxX(), maxY(), new Color(0, 0, 0, 125).getRGB());
		hoveredPositionInterpolator.update();
		guiGraphics.pose().pushPose();
		guiGraphics.pose().translate(0, hoveredPositionInterpolator.getAsDouble(), 0);
		guiGraphics.fill(minX(), 0, maxX(), 5, new Color(255, 255, 255, 125).getRGB());
		guiGraphics.pose().popPose();
		super.renderWidget(guiGraphics, partialTick);
	}

	@Override
	public void onMouseMove(double mouseX, double mouseY) {
		super.onMouseMove(mouseX, mouseY);
//		findHoveredEntry().ifPresentOrElse(
//				optionEntry -> {
//					System.out.println(optionEntry.getTitle().getString());
//					hoveredPositionInterpolator.setTarget(optionEntry.getLocalY());
//					hoveringOption = true;
//				},
//				() -> hoveringOption = false);
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
		protected void renderWidget(GuiGraphics guiGraphics, float partialTick) {
			super.renderWidget(guiGraphics, partialTick);
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
