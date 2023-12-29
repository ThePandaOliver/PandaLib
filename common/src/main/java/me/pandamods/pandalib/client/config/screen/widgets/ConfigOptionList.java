package me.pandamods.pandalib.client.config.screen.widgets;

import com.mojang.blaze3d.platform.Window;
import me.pandamods.pandalib.client.config.ConfigGuiRegistry;
import me.pandamods.pandalib.client.config.ConfigOptionWidgetProvider;
import me.pandamods.pandalib.client.screen.widgets.ScrollableParentWidget;
import me.pandamods.pandalib.client.screen.widgets.Widget;
import me.pandamods.pandalib.config.ConfigHolder;
import me.pandamods.pandalib.utils.animation.interpolation.FloatInterpolator;
import me.pandamods.pandalib.utils.animation.interpolation.Vector2Interpolator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import org.jetbrains.annotations.Nullable;
import org.joml.RoundingMode;
import org.joml.Vector2f;
import org.joml.Vector2i;

import java.awt.*;
import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class ConfigOptionList extends ScrollableParentWidget {
	private final Class<?> configClass;
	private final Field[] fields;
	private final Map<Field, ConfigOptionWidget> optionWidgets = new LinkedHashMap<>();

	private final FloatInterpolator highlightInterpolation = new FloatInterpolator(.125f, 0f, 0f, 1f);
	private final Vector2Interpolator minHighlightInterpolation = new Vector2Interpolator(.125f, new Vector2f(), new Vector2f(), new Vector2f());
	private final Vector2Interpolator maxHighlightInterpolation = new Vector2Interpolator(.125f, new Vector2f(), new Vector2f(), new Vector2f());
	private int listHeight = 0;

	public ConfigOptionList(@Nullable Widget parentWidget, Screen screen, ConfigHolder<?> configHolder) {
		super(parentWidget, screen);

		this.configClass = configHolder.getConfigClass();
		fields = configClass.getDeclaredFields();

		for (Field field : fields) {
			ConfigOptionWidgetProvider widgetProvider = ConfigGuiRegistry.getByClass(field.getType());
			if (widgetProvider != null) {
				optionWidgets.put(field, widgetProvider.create(this, screen, field, configHolder));
			}
		}

		this.getWidgets().values().stream().findFirst().ifPresent(widget -> {
			minHighlightInterpolation.setBounds(new Vector2f(), new Vector2f());
			maxHighlightInterpolation.setBounds(new Vector2f(widget.getWidth(), widget.getHeight()), new Vector2f(widget.getWidth(), widget.getHeight()));
		});
	}

	@Override
	public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
		renderHighlight(guiGraphics, mouseX, mouseY);
		super.renderWidget(guiGraphics, mouseX, mouseY, partialTick);
	}

	@Override
	public int getMaxScrollDistance() {
		return listHeight;
	}

	public void init() {
		listHeight = 0;
		for (ConfigOptionWidget widget : this.optionWidgets.values()) {
			widget.setBounds(this.getX(), listHeight, this.getWidth(), widget.getHeight());
			this.addRenderableWidget(widget);
			listHeight += widget.getHeight();
		}

		super.init();
	}

	public void renderHighlight(GuiGraphics guiGraphics, double mouseX, double mouseY) {
		ConfigOptionWidget widget = this.getHoveredOption(mouseX, mouseY);
		minHighlightInterpolation.update();
		maxHighlightInterpolation.update();
		if (widget != null) {
			highlightInterpolation.update();

			minHighlightInterpolation.setTarget(new Vector2f(widget.getMinX(), widget.getMinY() + this.getScrollDistanceFloat()));
			maxHighlightInterpolation.setTarget(new Vector2f(widget.getMaxX(), widget.getMaxY() + this.getScrollDistanceFloat()));
		}  else {
			highlightInterpolation.updateReverse();
		}

		Vector2i minHighlightVector = new Vector2i(minHighlightInterpolation.getValue().sub(0, this.getScrollDistanceFloat()), RoundingMode.TRUNCATE);
		Vector2i maxHighlightVector = new Vector2i(maxHighlightInterpolation.getValue().sub(0, this.getScrollDistanceFloat()), RoundingMode.TRUNCATE);

		int alpha = (int) (50 * highlightInterpolation.getValue());
		int shadingOffset = 75;
		int lightness = 125;
		Color highlight = new Color(lightness, lightness, lightness, alpha);
		Color highlightTop = new Color(lightness + shadingOffset, lightness + shadingOffset, lightness + shadingOffset, alpha);
		Color highlightBottom = new Color(lightness - shadingOffset, lightness - shadingOffset, lightness - shadingOffset, alpha);
		int size = 2;
		guiGraphics.fill(minHighlightVector.x, minHighlightVector.y,
				maxHighlightVector.x, minHighlightVector.y + size,
				highlightTop.getRGB());
		guiGraphics.fill(minHighlightVector.x, minHighlightVector.y + size,
				maxHighlightVector.x, maxHighlightVector.y - size,
				highlight.getRGB());
		guiGraphics.fill(minHighlightVector.x, maxHighlightVector.y - size,
				maxHighlightVector.x, maxHighlightVector.y,
				highlightBottom.getRGB());
	}

	public Map<Field, ConfigOptionWidget> getWidgets() {
		return optionWidgets;
	}

	public ConfigOptionWidget getHoveredOption(double mouseX, double mouseY) {
		if (!this.isMouseOver(mouseX, mouseY)) {
			return null;
		}
		return optionWidgets.values().stream()
				.filter(widget -> widget.isMouseOver(mouseX, mouseY))
				.findFirst().orElse(null);
	}
}
