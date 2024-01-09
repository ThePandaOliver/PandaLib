package me.pandamods.pandalib.client.config.screen.widgets;

import me.pandamods.pandalib.client.screen.widgets.ScrollableWidget;
import me.pandamods.pandalib.client.screen.widgets.WidgetImpl;
import me.pandamods.pandalib.utils.animation.interpolation.FloatInterpolator;
import me.pandamods.pandalib.utils.animation.interpolation.Vector2Interpolator;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.worldselection.CreateWorldScreen;
import org.joml.RoundingMode;
import org.joml.Vector2f;
import org.joml.Vector2i;

import java.awt.*;
import java.lang.reflect.Field;
import java.util.*;

public class ConfigEntryList extends ScrollableWidget {
	private Map<Field, ConfigEntry> options;

	private FloatInterpolator highlightInterpolation;
	private Vector2Interpolator minHighlightInterpolation;
	private Vector2Interpolator maxHighlightInterpolation;
	private int listHeight = 0;

	public ConfigEntryList(WidgetImpl parent) {
		super(parent);
	}

	public void setOptions(Map<Field, ConfigEntry> options) {
		this.options = options;
		this.rebuildWidgets();
	}

	public Map<Field, ConfigEntry> getOptions() {
		return options;
	}

	@Override
	public void initWidget() {
		float interpolationTime = .2f;
		highlightInterpolation = new FloatInterpolator(interpolationTime, 0f, 1f);
		minHighlightInterpolation = new Vector2Interpolator(interpolationTime, new Vector2f(0, 0), new Vector2f(0, 0));
		maxHighlightInterpolation = new Vector2Interpolator(
				interpolationTime,
				new Vector2f(window.getGuiScaledWidth(), 0),
				new Vector2f(window.getGuiScaledWidth(), 0)
		);

		this.listHeight = 0;
		for (ConfigEntry option : getOptions().values()) {
			option.setBounds(0, listHeight, this.getWidth(), option.getHeight());
			this.addWidget(option);
			listHeight += option.getHeight();
		}
	}

	@Override
	public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
		renderHighlight(guiGraphics, mouseX, mouseY);
		super.renderWidget(guiGraphics, mouseX, mouseY, partialTick);
	}

	@Override
	public void renderWidgetOverlay(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
		guiGraphics.blit(CreateWorldScreen.FOOTER_SEPERATOR, getX(), getMaxY(),
				0.0F, 0.0F, getWidth(), 2, 32, 2);
	}

	public void renderHighlight(GuiGraphics guiGraphics, double mouseX, double mouseY) {
		ConfigEntry widget = this.getHoveredOption(mouseX, mouseY);
		minHighlightInterpolation.update();
		maxHighlightInterpolation.update();
		if (widget != null) {
			highlightInterpolation.update();

			minHighlightInterpolation.setTarget(new Vector2f(widget.getMinX(), widget.getMinY() + getScrollDistanceYFloat()));
			maxHighlightInterpolation.setTarget(new Vector2f(widget.getMaxX(), widget.getMaxY() + getScrollDistanceYFloat()));
		}  else {
			highlightInterpolation.updateReverse();
		}

		Vector2i minHighlightVector = new Vector2i(minHighlightInterpolation.getValue().sub(0, getScrollDistanceYFloat()), RoundingMode.TRUNCATE);
		Vector2i maxHighlightVector = new Vector2i(maxHighlightInterpolation.getValue().sub(0, getScrollDistanceYFloat()), RoundingMode.TRUNCATE);

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

	public ConfigEntry getHoveredOption(double mouseX, double mouseY) {
		if (!this.isMouseOver(mouseX, mouseY)) {
			return null;
		}
		return getOptions().values().stream()
				.filter(widget -> widget.isMouseOver(mouseX, mouseY))
				.findFirst().orElse(null);
	}

	@Override
	public int getMaxScrollDistanceX() {
		return 0;
	}

	@Override
	public int getMaxScrollDistanceY() {
		return listHeight;
	}
}
