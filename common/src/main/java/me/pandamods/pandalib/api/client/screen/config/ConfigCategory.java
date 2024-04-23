package me.pandamods.pandalib.api.client.screen.config;

import me.pandamods.pandalib.api.client.screen.config.option.AbstractConfigOption;
import net.minecraft.client.gui.layouts.FrameLayout;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

public class ConfigCategory extends AbstractConfigCategory {
	private final Component name;
	private final List<AbstractConfigOption<?>> options;

	public ConfigCategory(Component name, List<AbstractConfigOption<?>> options) {
		this.name = name;
		this.options = options;
	}

	@Override
	public Component getName() {
		return name;
	}

	@Override
	protected void init() {
		GridLayout gridLayout = new GridLayout();
		gridLayout.defaultCellSetting().alignHorizontallyCenter();

		int y = 0;
		for (AbstractConfigOption<?> option : this.options) {
			gridLayout.addChild(option, y++, 0);
		}

		gridLayout.arrangeElements();
		FrameLayout.alignInRectangle(gridLayout, 0, 0, this.getWidth(), this.getHeight(), 0, 0);
		gridLayout.visitChildren(this::addElement);
		super.init();
	}

	@Override
	public void save() {
		this.options.forEach(AbstractConfigOption::save);
	}

	@Override
	public void load() {
		this.options.forEach(AbstractConfigOption::load);
	}

	@Override
	public void reset() {
		this.options.forEach(AbstractConfigOption::reset);
	}

	public static Builder builder(Component name) {
		return new Builder(name);
	}

	public static class Builder {
		private final Component name;
		private final List<AbstractConfigOption<?>> options = new ArrayList<>();

		public Builder(Component name) {
			this.name = name;
		}

		public void addOption(AbstractConfigOption<?> configOption) {
			this.options.add(configOption);
		}

		public ConfigCategory build() {
			return new ConfigCategory(this.name, this.options);
		}
	}
}