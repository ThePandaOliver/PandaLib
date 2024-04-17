package me.pandamods.pandalib.api.client.screen.config;

import me.pandamods.pandalib.api.client.screen.PLScreen;
import me.pandamods.pandalib.api.config.ConfigData;
import me.pandamods.pandalib.api.config.PandaLibConfig;
import me.pandamods.pandalib.api.config.holders.ConfigHolder;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.layouts.FrameLayout;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ConfigMenu<T extends ConfigData> extends PLScreen {
	private final Screen parent;
	private final ConfigSideBar categoryList = new ConfigSideBar(this);
	private final ConfigHolder<T> configHolder;
	private AbstractConfigCategory category;

	protected ConfigMenu(Screen parent, Component title, ConfigHolder<T> configHolder, List<AbstractConfigCategory> categories) {
		super(title);
		this.parent = parent;
		this.configHolder = configHolder;
		this.category = categories.get(0);
		this.categoryList.categories.addAll(categories);

		this.load();
	}

	@Override
	protected void init() {
		this.addElement(this.categoryList);
		this.addElement(this.category);
		super.init();
	}

	public void save() {
		this.categoryList.categories.forEach(AbstractConfigCategory::save);
		configHolder.save();
		this.onClose();
	}

	public void load() {
		this.categoryList.categories.forEach(AbstractConfigCategory::load);
	}

	public void setCategory(AbstractConfigCategory category) {
		this.category = category;
		this.rebuildWidgets();
	}

	public AbstractConfigCategory getCategory() {
		return category;
	}

	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
		renderDirtBackground(guiGraphics);
		super.render(guiGraphics, mouseX, mouseY, partialTick);
	}

	@Override
	public void onClose() {
		this.minecraft.setScreen(parent);
	}

	public static <T extends ConfigData> Builder<T> builder(Class<T> config) {
		return new Builder<>(config);
	}

	public static class Builder<T extends ConfigData> {
		private final ConfigHolder<T> configHolder;
		private final List<AbstractConfigCategory> categories = new ArrayList<>();
		private Component title;
		private Screen parent;

		private Builder(Class<T> config) {
			this.configHolder = PandaLibConfig.getConfig(config);
			this.title = configHolder.getName();
		}

		public Builder<T> registerCategory(AbstractConfigCategory category) {
			this.categories.add(category);
			return this;
		}

		public Builder<T> registerCategories(AbstractConfigCategory... categories) {
			this.categories.addAll(List.of(categories));
			return this;
		}

		public Builder<T> registerCategories(Collection<? extends AbstractConfigCategory> categories) {
			this.categories.addAll(categories);
			return this;
		}

		public Builder<T> setTitle(Component title) {
			this.title = title;
			return this;
		}

		public Builder<T> setParent(Screen parent) {
			this.parent = parent;
			return this;
		}

		public ConfigMenu<T> Build() {
			return new ConfigMenu<T>(this.parent, this.title, configHolder, categories);
		}
	}
}