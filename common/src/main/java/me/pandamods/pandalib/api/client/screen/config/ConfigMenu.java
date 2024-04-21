package me.pandamods.pandalib.api.client.screen.config;

import me.pandamods.pandalib.api.client.screen.PLScreen;
import me.pandamods.pandalib.api.client.screen.UIComponentHolder;
import me.pandamods.pandalib.api.config.ConfigData;
import me.pandamods.pandalib.api.config.PandaLibConfig;
import me.pandamods.pandalib.api.config.holders.ConfigHolder;
import me.pandamods.pandalib.api.utils.PLCommonComponents;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.layouts.FrameLayout;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.awt.*;
import java.util.*;
import java.util.List;

public class ConfigMenu<T extends ConfigData> extends PLScreen {
	private final Screen parent;
	private final ConfigSideBar sideBar = new ConfigSideBar();
	private final ConfigHolder<T> configHolder;
	private AbstractConfigCategory category;

	protected ConfigMenu(Screen parent, Component title, ConfigHolder<T> configHolder, List<AbstractConfigCategory> categories) {
		super(title);
		this.parent = parent;
		this.configHolder = configHolder;
		this.category = categories.get(0);
		this.sideBar.categories.addAll(categories);

		this.load();
	}

	@Override
	protected void init() {
		this.addElement(this.sideBar);
		this.addElement(this.category);
		super.init();
	}

	public void save() {
		this.sideBar.categories.forEach(AbstractConfigCategory::save);
		configHolder.save();
		this.onClose();
	}

	public void load() {
		this.sideBar.categories.forEach(AbstractConfigCategory::load);
	}

	public void reset() {
		this.sideBar.categories.forEach(AbstractConfigCategory::reset);
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

	public class ConfigSideBar extends UIComponentHolder {
		public static final int SIZE = 100;

		private static final int MENU_DRAW_EDGE_COLOR = new Color(0, 0, 0, 150).getRGB();
		private static final int MENU_DRAW_EDGE_HIGHLIGHT_COLOR = new Color(100, 100, 100, 150).getRGB();

		public final Set<AbstractConfigCategory> categories = new HashSet<>();

		@Override
		protected void init() {
			GridLayout categoryGrid = new GridLayout();
			categoryGrid.defaultCellSetting().alignHorizontallyCenter();

			int i = 0;
			for (AbstractConfigCategory category : categories) {
				categoryGrid.addChild(new CategoryButton(90, category), i++, 0);
			}

			categoryGrid.arrangeElements();
			FrameLayout.alignInRectangle(categoryGrid, 0, 0, this.getWidth(), this.getHeight() - 50, 0.5f, 0);
			categoryGrid.visitChildren(this::addElement);

			GridLayout actionGrid = new GridLayout();
			actionGrid.spacing(4).defaultCellSetting();

			actionGrid.addChild(Button.builder(PLCommonComponents.SAVE, button -> ConfigMenu.this.save())
					.size(45, 20).build(), 0, 0);
			actionGrid.addChild(Button.builder(PLCommonComponents.RESET, button -> ConfigMenu.this.reset())
					.size(45, 20).build(), 0, 1);
			actionGrid.addChild(Button.builder(PLCommonComponents.CANCEL, button -> ConfigMenu.this.onClose())
					.size(94, 20).build(), 1, 0, 1, 2);

			actionGrid.arrangeElements();
			FrameLayout.alignInRectangle(actionGrid, 0, this.getHeight() - 50, this.getWidth(), 50, 0.5f, 0.5f);
			actionGrid.visitChildren(this::addElement);
			super.init();
		}

		@Override
		public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
			guiGraphics.fill(maxX(), minY(), maxX() + 1, maxY(), MENU_DRAW_EDGE_COLOR);
			guiGraphics.fill(maxX() + 1, minY(), maxX() + 2, maxY(), MENU_DRAW_EDGE_HIGHLIGHT_COLOR);
			super.render(guiGraphics, mouseX, mouseY, partialTick);
		}

		@Override
		public int getWidth() {
			return SIZE;
		}

		@Override
		public int getHeight() {
			return ConfigMenu.this.height;
		}

		public class CategoryButton extends AbstractButton {
			private final AbstractConfigCategory category;

			public CategoryButton(int width, AbstractConfigCategory category) {
				super(0, 0, width, 20, category.getName());
				this.category = category;
			}

			@Override
			public void onPress() {
				ConfigMenu.this.setCategory(category);
			}

			@Override
			protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
				this.defaultButtonNarrationText(narrationElementOutput);
			}

			@Override
			public void renderTexture(GuiGraphics guiGraphics, ResourceLocation texture, int x, int y, int uOffset, int vOffset, int textureDifference, int width, int height, int textureWidth, int textureHeight) {
				super.renderTexture(guiGraphics, texture, x, y, uOffset, vOffset, textureDifference, width, height, textureWidth, textureHeight);
			}

			@Override
			public boolean isActive() {
				return !Objects.equals(ConfigMenu.this.getCategory(), category);
			}
		}
	}
}