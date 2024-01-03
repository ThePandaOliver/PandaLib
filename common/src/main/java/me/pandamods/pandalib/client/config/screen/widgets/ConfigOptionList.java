package me.pandamods.pandalib.client.config.screen.widgets;

import me.pandamods.pandalib.client.config.ConfigGuiRegistry;
import me.pandamods.pandalib.client.config.ConfigOptionWidgetProvider;
import me.pandamods.pandalib.client.screen.PandaLibScreen;
import me.pandamods.pandalib.client.screen.widgets.ScrollableWidget;
import me.pandamods.pandalib.client.screen.widgets.Widget;
import me.pandamods.pandalib.config.Config;
import me.pandamods.pandalib.config.ConfigHolder;
import me.pandamods.pandalib.utils.animation.interpolation.FloatInterpolator;
import me.pandamods.pandalib.utils.animation.interpolation.Vector2Interpolator;
import net.minecraft.client.gui.GuiGraphics;
import org.joml.RoundingMode;
import org.joml.Vector2f;
import org.joml.Vector2i;

import java.awt.*;
import java.lang.reflect.Field;
import java.util.*;

public class ConfigOptionList extends ScrollableWidget {
	private final ConfigHolder<?> configHolder;
	private final Class<?> configClass;
	private final Field[] fields;

//	private final Map<Field, ConfigOptionWidget<?>> optionWidgets = new LinkedHashMap<>();
	private final Map<String, Map<Field, ConfigOptionWidget>> optionWidgetCategories = new HashMap<>();
	private String currentCategory = "default";

	private final FloatInterpolator highlightInterpolation;
	private final Vector2Interpolator minHighlightInterpolation;
	private final Vector2Interpolator maxHighlightInterpolation;
	private int listHeight = 0;

	public ConfigOptionList(PandaLibScreen screen, Widget parent, ConfigHolder<?> configHolder) {
		super(screen, parent);
		this.configHolder = configHolder;
		this.configClass = configHolder.getConfigClass();
		this.fields = configClass.getDeclaredFields();

		for (Field field : fields) {
			Optional<ConfigOptionWidgetProvider> widgetProvider = ConfigGuiRegistry.getByClass(field.getType());
			widgetProvider.ifPresent(provider -> {
				ConfigOptionWidget widget = provider.create(screen, this, new ConfigOptionWidget.Data(field, configHolder));
				Config.Gui.Category category = field.getAnnotation(Config.Gui.Category.class);
				if (category != null) {
					addToCategory(category.value(), field, widget);
				} else {
					addToCategory("default", field, widget);
				}
//				optionWidgets.put(field, provider.create(screen, this, new ConfigOptionWidget.Data(field, configHolder)))
			});
		}

		float interpolationTime = .2f;
		highlightInterpolation = new FloatInterpolator(interpolationTime, 0f, 1f);
		minHighlightInterpolation = new Vector2Interpolator(interpolationTime, new Vector2f(0, 0), new Vector2f(0, 0));
		maxHighlightInterpolation = new Vector2Interpolator(
				interpolationTime,
				new Vector2f(window.getGuiScaledWidth(), 0),
				new Vector2f(window.getGuiScaledWidth(), 0)
		);
	}

	private void addToCategory(String name, Field field, ConfigOptionWidget configOptionWidget) {
		optionWidgetCategories.putIfAbsent(name, new LinkedHashMap<>());
		optionWidgetCategories.get(name).put(field, configOptionWidget);
	}

	public Map<Field, ConfigOptionWidget> getOptions() {
		return this.optionWidgetCategories.getOrDefault(this.currentCategory, new HashMap<>());
	}

	public void setCategory(String category) {
		this.currentCategory = category;
	}

	public String getCategory() {
		return currentCategory;
	}

	public Set<String> getCategories() {
		return optionWidgetCategories.keySet();
	}

	@Override
	public void init() {
		listHeight = 0;
		for (ConfigOptionWidget widget : this.getOptions().values()) {
			widget.setBounds(0, listHeight, this.getWidth(), widget.getHeight());
			this.addWidget(widget);
			listHeight += widget.getHeight();
		}

		super.init();
	}

	@Override
	public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
		renderHighlight(guiGraphics, mouseX, mouseY);
		super.renderWidget(guiGraphics, mouseX, mouseY, partialTick);
	}

	public void renderHighlight(GuiGraphics guiGraphics, double mouseX, double mouseY) {
		ConfigOptionWidget widget = this.getHoveredOption(mouseX, mouseY);
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

	public ConfigOptionWidget getHoveredOption(double mouseX, double mouseY) {
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
