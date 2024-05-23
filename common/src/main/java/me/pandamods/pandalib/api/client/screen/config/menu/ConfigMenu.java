package me.pandamods.pandalib.api.client.screen.config.menu;

import me.pandamods.pandalib.api.client.screen.PLScreen;
import me.pandamods.pandalib.api.client.screen.UIComponentHolder;
import me.pandamods.pandalib.api.client.screen.config.AbstractConfigCategory;
import me.pandamods.pandalib.api.client.screen.config.ConfigCategory;
import me.pandamods.pandalib.api.client.screen.widget.list.QuickListWidget;
import me.pandamods.pandalib.api.config.ConfigData;
import me.pandamods.pandalib.api.config.PandaLibConfig;
import me.pandamods.pandalib.api.config.holders.ConfigHolder;
import me.pandamods.pandalib.api.utils.PLCommonComponents;
import me.pandamods.pandalib.api.utils.ScreenUtils;
import me.pandamods.pandalib.api.utils.screen.PLGridLayout;
import me.pandamods.pandalib.api.utils.screen.PLGuiGraphics;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.layouts.FrameLayout;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.navigation.CommonInputs;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;

import java.awt.*;
import java.util.*;

public class ConfigMenu<T extends ConfigData> extends PLScreen {
	private final Screen parent;
	private final ConfigSideBar sideBar = new ConfigSideBar();
	private final CategoryAddress addressBar = new CategoryAddress();
	private final ConfigHolder<T> configHolder;
	private AbstractConfigCategory category;
	private AbstractConfigCategory rootCategory;

	public ConfigMenu(Class<T> config, AbstractConfigCategory category) {
		this(null, PandaLibConfig.getConfig(config), category);
	}

	public ConfigMenu(Screen parent, Class<T> config, AbstractConfigCategory category) {
		this(parent, PandaLibConfig.getConfig(config), category);
	}

	protected ConfigMenu(Screen parent, ConfigHolder<T> configHolder, AbstractConfigCategory category) {
		super(category.getName());
		this.parent = parent;
		this.configHolder = configHolder;
		this.category = this.rootCategory = category;

		this.load();
	}

	@Override
	protected void init() {
		this.addressBar.setSize(this.width, 20);
		this.addElement(this.addressBar);

		this.sideBar.setPosition(0, this.addressBar.getHeight());
		this.sideBar.setSize(100, this.height - this.sideBar.getY());
		this.addElement(this.sideBar);

		this.category.setPosition(this.sideBar.getWidth(), this.addressBar.getHeight());
		this.category.setSize(this.width - this.category.getX(), this.height - this.category.getY());
		this.addElement(this.category);
		super.init();
	}

	public void save() {
		this.rootCategory.save();
		configHolder.save();
		this.onClose();
	}

	public void load() {
		this.rootCategory.load();
	}

	public void reset() {
		this.rootCategory.reset();
	}

	public void setCategory(AbstractConfigCategory category) {
		this.category = category;
		this.rebuildWidgets();
	}

	public AbstractConfigCategory getCategory() {
		return category;
	}

	@Override
	public void render(PLGuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
		renderDirtBackground(guiGraphics);
		super.render(guiGraphics, mouseX, mouseY, partialTick);
	}

	@Override
	public void onClose() {
		this.minecraft.setScreen(parent);
	}

	public class ConfigSideBar extends UIComponentHolder {
		@Override
		protected void init() {
			PLGridLayout categoryGrid = new PLGridLayout();
			PLGridLayout.RowHelper categoryHelper = categoryGrid.createRowHelper(1);
			for (AbstractConfigCategory category : ConfigMenu.this.getCategory().getCategories()) {
				categoryHelper.addChild(Button.builder(category.getName(), button -> setCategory(category)).width(90).build());
			}
			categoryGrid.quickArrange(this::addElement, 0, 5, this.getWidth(), this.getHeight() - 55, 0.5f, 0);

			PLGridLayout actionGrid = new PLGridLayout();
			actionGrid.spacing(4).defaultCellSetting();

			actionGrid.addChild(Button.builder(PLCommonComponents.SAVE, button -> ConfigMenu.this.save())
					.size(45, 20).build(), 0, 0);
			actionGrid.addChild(Button.builder(PLCommonComponents.RESET, button -> ConfigMenu.this.reset())
					.size(45, 20).build(), 0, 1);
			actionGrid.addChild(Button.builder(PLCommonComponents.CANCEL, button -> ConfigMenu.this.onClose())
					.size(94, 20).build(), 1, 0, 1, 2);

			actionGrid.quickArrange(this::addElement, 0, this.getHeight() - 50, this.getWidth(), 50, 0.5f, 0.5f);
			super.init();
		}

		@Override
		public void render(PLGuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
			guiGraphics.renderSeparatorLine(this.maxX(), this.getY(), this.height, true);
			super.render(guiGraphics, mouseX, mouseY, partialTick);
		}
	}

	public class CategoryAddress extends UIComponentHolder {
		@Override
		protected void init() {
			PLGridLayout grid = new PLGridLayout();
			grid.defaultCellSetting().padding(1);
			PLGridLayout.ColumnHelper helper = grid.createColumnHelper(1);
			addCategoryDist(helper, ConfigMenu.this.category);

			grid.quickArrange(this::addElement, 5, 0, this.getWidth() - 10, this.getHeight(), 0f, 0.5f);
			super.init();
		}

		private void addCategoryDist(PLGridLayout.ColumnHelper helper, AbstractConfigCategory abstractCategory) {
			AbstractConfigCategory parentCategory = abstractCategory.getParentCategory();
			if (parentCategory != null)
				addCategoryDist(helper, parentCategory);

			helper.addChild(new CategoryButton(abstractCategory));
			if (abstractCategory instanceof ConfigCategory category && !category.getCategories().isEmpty())
				helper.addChild(new CategoryArrowButton(category));
		}

		@Override
		public void render(PLGuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
			guiGraphics.renderSeparatorLine(getX(), maxY(), getWidth(), false);
			super.render(guiGraphics, mouseX, mouseY, partialTick);
		}

		private class CategoryButton extends AbstractButton {
			private final Font font;
			private final AbstractConfigCategory category;

			public CategoryButton(AbstractConfigCategory category) {
				super(0, 0, 0, 20, category.getName());
				this.category = category;
				this.font = Minecraft.getInstance().font;
				this.width = font.width(getMessage());
			}

			@Override
			public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
				int y = this.getY() + (this.height - this.font.lineHeight) / 2;
				guiGraphics.drawString(this.font, getMessage(), this.getX(), y, Color.white.getRGB());

				y += this.font.lineHeight;
				if (this.isHoveredOrFocused())
					guiGraphics.fill(this.getX(), y, this.getX() + this.width, y + 1, Color.white.getRGB());
			}

			@Override
			public void onPress() {
				ConfigMenu.this.setCategory(category);
			}

			@Override
			protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
			}
		}

		private class CategoryArrowButton extends UIComponentHolder {
			private final Font font;
			private final QuickListWidget categoryList;

			public CategoryArrowButton(ConfigCategory category) {
				this.font = Minecraft.getInstance().font;
				this.width = font.width(">");
				this.height = 20;

				this.categoryList = QuickListWidget.builder(1)
						.addElements(category.getCategories().stream().map(child ->
								Button.builder(child.getName(), button -> ConfigMenu.this.setCategory(child))
										.size(150, font.lineHeight + 8)
										.build()
						).toList()).build();
			}

			@Override
			protected void init() {
				this.categoryList.setActive(false);
				this.categoryList.setPosition(0, this.getHeight() + 2);
				this.addElement(this.categoryList);
				super.init();
			}

			@Override
			public void render(PLGuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
				this.checkHoverState(mouseX, mouseY);
				int y = this.getY() + (this.height - this.font.lineHeight) / 2;
				guiGraphics.drawString(this.font, ">", this.getX(), y, Color.white.getRGB());

				y += this.font.lineHeight;
				if (this.isHoveredOrFocused())
					guiGraphics.fill(this.getX(), y, this.getX() + this.width, y + 1, Color.white.getRGB());

				super.render(guiGraphics, mouseX, mouseY, partialTick);
			}

			protected void onPress() {
				this.playDownSound(Minecraft.getInstance().getSoundManager());
				this.categoryList.setActive(!this.categoryList.isActive());
			}

			@Override
			public boolean mouseClicked(double mouseX, double mouseY, int button) {
				if (super.mouseClicked(mouseX, mouseY, button))
					return true;

				if (this.isActive()) {
					if (this.isValidClickButton(button)) {
						this.onPress();
						return true;
					}
					return false;
				}
				return false;
			}

			@Override
			public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
				if (super.keyReleased(keyCode, scanCode, modifiers))
					return true;

				if (!this.isActive())
					return false;
				if (CommonInputs.selected(keyCode)) {
					this.onPress();
					return true;
				}
				return false;
			}

			public void playDownSound(SoundManager handler) {
				handler.play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0f));
			}

			protected boolean isValidClickButton(int button) {
				return button == 0;
			}

			@Override
			public boolean isInteractable() {
				return true;
			}
		}
	}
}