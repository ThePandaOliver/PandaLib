package me.pandamods.pandalib.api.client.screen.config.category;

import me.pandamods.pandalib.api.client.screen.config.option.AbstractConfigOption;
import me.pandamods.pandalib.api.utils.screen.PLGridLayout;
import me.pandamods.pandalib.api.utils.screen.PLGuiGraphics;
import me.pandamods.pandalib.core.utils.animation.interpolation.NumberAnimator;
import net.minecraft.network.chat.Component;
import org.joml.Math;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ConfigCategory extends AbstractConfigCategory {
	private final Component name;
	private final List<AbstractConfigOption<?>> options;
	private final List<AbstractConfigCategory> categories;

	private final NumberAnimator optionHighlightAlpha;
	private final NumberAnimator optionHighlightPosition;
	private final NumberAnimator optionHighlightSize;

	public ConfigCategory(Component name, List<AbstractConfigOption<?>> options, List<AbstractConfigCategory> categories) {
		this.name = name;
		this.options = options;
		this.categories = categories;
		this.categories.forEach(category -> category.setParentCategory(this));

		this.optionHighlightAlpha = new NumberAnimator(0).setDuration(0.5f);
		this.optionHighlightPosition = new NumberAnimator(options.isEmpty() ? 0 : options.get(0).getY()).setDuration(0.25f);
		this.optionHighlightSize = new NumberAnimator(options.isEmpty() ? 0 : options.get(0).getHeight()).setDuration(0.25f);
	}

	@Override
	public List<AbstractConfigCategory> getCategories() {
		return categories;
	}

	@Override
	public Component getName() {
		return name;
	}

	@Override
	protected void init() {
		PLGridLayout grid = new PLGridLayout();
		PLGridLayout.RowHelper rowHelper = grid.createRowHelper(1);
		for (AbstractConfigOption<?> option : this.options) {
			rowHelper.addChild(option);
		}
		grid.quickArrange(this::addElement, 0, 0, this.getWidth(), this.getHeight());
		super.init();
	}

	@Override
	public void render(PLGuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
		renderHighlight(guiGraphics);
		super.render(guiGraphics, mouseX, mouseY, partialTick);
	}

	private static final int OPTION_HIGHLIGHT_COLOR_TOP = new Color(255, 255, 255, 75).getRGB();
	private static final int OPTION_HIGHLIGHT_COLOR = new Color(200, 200, 200, 75).getRGB();
	private static final int OPTION_HIGHLIGHT_COLOR_BOTTOM = new Color(150, 150, 150, 75).getRGB();

	private void renderHighlight(PLGuiGraphics guiGraphics) {
		optionHighlightAlpha.update();
		optionHighlightPosition.update();
		optionHighlightSize.update();

		int minY = this.getY() + optionHighlightPosition.getAsInt();
		int maxY = minY + optionHighlightSize.getAsInt();

		guiGraphics.setColor(1, 1, 1, Math.min(1, optionHighlightAlpha.getAsFloat()));
		guiGraphics.fill(minX(), minY, maxX(), minY + 2, OPTION_HIGHLIGHT_COLOR_TOP);
		guiGraphics.fill(minX(), minY + 2, maxX(), maxY - 2, OPTION_HIGHLIGHT_COLOR);
		guiGraphics.fill(minX(), maxY - 2, maxX(), maxY, OPTION_HIGHLIGHT_COLOR_BOTTOM);
		guiGraphics.setColor(1, 1, 1, 1);
	}

	@Override
	public void mouseMoved(double mouseX, double mouseY) {
		AbstractConfigOption<?> option = getOptionAt((int) mouseX, (int) mouseY);
		if (option != null) {
			optionHighlightAlpha.setTarget(1);
			optionHighlightPosition.setTarget(option.getY());
			optionHighlightSize.setTarget(option.getHeight());
		} else optionHighlightAlpha.setTarget(0);
		super.mouseMoved(mouseX, mouseY);
	}

	public AbstractConfigOption<?> getOptionAt(int mouseX, int mouseY) {
		mouseX -= this.getX();
		mouseY -= this.getY();
		for (AbstractConfigOption<?> option : this.options) {
    		if (option.isMouseOver(mouseX, mouseY)) {
    			return option;
    		}
    	}
    	return null;
	}

	@Override
	public void save() {
		this.categories.forEach(AbstractConfigCategory::save);
		this.options.forEach(AbstractConfigOption::save);
	}

	@Override
	public void load() {
		this.categories.forEach(AbstractConfigCategory::load);
		this.options.forEach(AbstractConfigOption::load);
	}

	@Override
	public void reset() {
		this.categories.forEach(AbstractConfigCategory::reset);
		this.options.forEach(AbstractConfigOption::reset);
	}

	public static Builder builder(Component name) {
		return new Builder(name);
	}

	public static class Builder {
		private final Component name;
		private final List<AbstractConfigOption<?>> options = new ArrayList<>();
		private final List<AbstractConfigCategory> categories = new ArrayList<>();

		public Builder(Component name) {
			this.name = name;
		}

		public void addOption(AbstractConfigOption<?> configOption) {
			this.options.add(configOption);
		}

		public void addCategory(AbstractConfigCategory category) {
			this.categories.add(category);
		}

		public ConfigCategory build() {
			return new ConfigCategory(this.name, this.options, this.categories);
		}
	}
}