package me.pandamods.pandalib.api.client.screen.config;

import me.pandamods.pandalib.api.client.screen.config.option.ConfigOption;
import me.pandamods.pandalib.api.config.ConfigData;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ConfigCategory<T extends ConfigData> extends AbstractConfigCategory {
	private final Component name;
	private final List<ConfigOption<?, T>> options;

	public ConfigCategory(Component name, List<ConfigOption<?, T>> options) {
		this.name = name;
		this.options = options;
	}

	@Override
	protected void init() {
		int y = 0;
		for (ConfigOption<?, T> option : this.options) {
			option.setY(y);
			this.addElement(option);
			y += option.getHeight();
		}
		super.init();
	}

	public static <T extends ConfigData> Builder<T> builder(Component name) {
		return new Builder<>(name);
	}

	public static class Builder<T extends ConfigData> {
		private final Component name;
		private final List<ConfigOption<?, T>> options = new ArrayList<>();

		public Builder(Component name) {
			this.name = name;
		}

		public void addOption(ConfigOption<?, T> configOption) {
			this.options.add(configOption);
		}

		public ConfigCategory<T> build() {
			return new ConfigCategory<>(this.name, this.options);
		}
	}
}
