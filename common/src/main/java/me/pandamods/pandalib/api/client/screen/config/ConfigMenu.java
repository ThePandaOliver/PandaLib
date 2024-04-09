package me.pandamods.pandalib.api.client.screen.config;

import me.pandamods.pandalib.api.client.screen.PLScreen;
import me.pandamods.pandalib.api.config.Config;
import me.pandamods.pandalib.api.config.ConfigData;
import me.pandamods.pandalib.api.config.PandaLibConfig;
import me.pandamods.pandalib.api.config.holders.ConfigHolder;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ConfigMenu<T extends ConfigData> extends PLScreen {
	private final Screen parent;
	private final ConfigCategoryList categoryList = new ConfigCategoryList(this);
	private final ConfigHolder<T> configHolder;
	private AbstractConfigCategory category;

	protected ConfigMenu(Screen parent, Component title, ConfigHolder<T> configHolder, List<AbstractConfigCategory> categories) {
		super(title);
		this.parent = parent;
		this.configHolder = configHolder;
		this.category = categories.get(0);
		this.categoryList.categories.addAll(categories);
	}

	@Override
	protected void init() {
		this.addElement(this.categoryList);
		this.addElement(this.category);
		super.init();
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

		private Builder(Class<T> config) {
			this.configHolder = PandaLibConfig.getConfig(config);
			this.title = Component.translatable(String.format("gui.%s.config.%s.title", configHolder.modID(), configHolder.name()));
		}

		public Builder<T> registerCategory(AbstractConfigCategory category) {
			this.categories.add(category);
			return this;
		}

		public Builder<T> registerCategories(AbstractConfigCategory... categories) {
			this.categories.addAll(List.of(categories));
			return this;
		}

		public Builder<T> registerCategories(Collection<AbstractConfigCategory> categories) {
			this.categories.addAll(categories);
			return this;
		}

		public void setTitle(Component title) {
			this.title = title;
		}

		public ConfigMenu<T> Build(Screen parent) {
			return new ConfigMenu<T>(parent, this.title, configHolder, categories);
		}
	}
}