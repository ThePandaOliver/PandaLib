package me.pandamods.pandalib.client.config.screen;

import me.pandamods.pandalib.client.config.ConfigCategory;
import me.pandamods.pandalib.client.config.ConfigCategoryImpl;
import me.pandamods.pandalib.client.config.screen.widgets.*;
import me.pandamods.pandalib.client.screen.PandaLibScreen;
import me.pandamods.pandalib.config.Config;
import me.pandamods.pandalib.config.ConfigHolder;
import me.pandamods.pandalib.config.ConfigRegistry;
import me.pandamods.pandalib.utils.PandaLibComponents;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.layouts.FrameLayout;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.lang.reflect.Field;
import java.util.*;

public class ConfigScreen extends PandaLibScreen implements ConfigCategoryImpl {
	private final ConfigHolder<?> configHolder;
	private final Class<?> configClass;
	private final Config config;
	private final Map<String, ConfigCategory> optionCategories = new LinkedHashMap<>();
	private final Map<Field, ConfigEntry> optionEntries = new LinkedHashMap<>();
	private final ConfigEntryList optionListWidget;
	private final ConfigCategoryHistory configCategoryHistory;
	private final ConfigTabWidget optionCategoryTabs;
	private ConfigCategoryImpl currentCategory = this;
	private boolean isLoaded = false;

	private ConfigScreen(Screen parent, ConfigHolder<?> configHolder) {
		super(parent, GetTitle(configHolder));
		this.configHolder = configHolder;
		this.configClass = configHolder.getConfigClass();
		this.config = configHolder.getDefinition();

		this.optionListWidget = new ConfigEntryList(this);
		this.configCategoryHistory = new ConfigCategoryHistory(this);
		this.optionCategoryTabs = new ConfigTabWidget(this);
		createEntriesAndCategories(optionListWidget, configHolder.get(), configHolder.getDefinition());
		setCategory(this);
	}

	@Override
	public void init() {
		int top = 24;
		int historySize = 20;
		int bottom = 40;

		optionCategoryTabs.setBounds(0, 0, width, top);
		this.addWidget(optionCategoryTabs);

		configCategoryHistory.setBounds(0, top, width, top + historySize);
		this.addWidget(configCategoryHistory);

		optionListWidget.setBounds(0, top + historySize, width, height - bottom);
		this.addWidget(optionListWidget);

		GridLayout gridLayout = new GridLayout();
		gridLayout.defaultCellSetting().padding(2).alignVerticallyMiddle().alignHorizontallyCenter();
		GridLayout.RowHelper rowHelper = gridLayout.createRowHelper(3);

		rowHelper.addChild(Button.builder(PandaLibComponents.CANCEL, button -> onClose()).width(100).build());
		rowHelper.addChild(Button.builder(PandaLibComponents.RESET, button -> reset()).width(100).build());
		rowHelper.addChild(Button.builder(PandaLibComponents.SAVE, button -> save()).width(100).build());

		gridLayout.arrangeElements();
		FrameLayout.alignInRectangle(gridLayout, 0, this.height - bottom, this.width, bottom, 0.5f, 0.5f);
		gridLayout.visitWidgets(this::addRenderableWidget);

		super.init();
		if (!isLoaded)
			this.load();
		isLoaded = true;
	}

	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
		super.render(guiGraphics, mouseX, mouseY, partialTick);
//		guiGraphics.drawCenteredString(this.font, this.getTitle(), width / 2, (40 - font.lineHeight) / 2, Color.white.getRGB());
	}

	private void save() {
		this.optionListWidget.getOptions().forEach((field, widget) -> widget.save());
		this.configHolder.save();
		setScreen(getParentScreen());
	}

	private void reset() {
		this.optionListWidget.getOptions().forEach((field, widget) -> widget.reset());
	}

	public static Screen Create(String modId, Screen parentScreen) {
		Map<Class<?>, ConfigHolder<?>> configs = ConfigRegistry.getConfigs(modId);
		if (configs.size() == 1) {
			return new ConfigScreen(parentScreen, configs.values().stream().findFirst().get());
		}
		return new ConfigScreen(parentScreen, configs.values().stream().findFirst().get());
	}

	@Override
	public String getFullName() {
		return "config." + config.modId() + ".category." + config.name();
	}

	@Override
	public String getName() {
		return "origin";
	}

	@Override
	public Component getTitle() {
		return Component.translatable(getFullName() + ".title");
	}

	public static Component GetTitle(ConfigHolder<?> configHolder) {
		Config config = configHolder.getDefinition();
		return Component.literal("config." + config.modId() + "." + config.name() + ".title");
	}

	@Override
	public Map<String, ConfigCategory> optionCategories() {
		return optionCategories;
	}

	@Override
	public Map<Field, ConfigEntry> optionEntries() {
		return optionEntries;
	}

	public ConfigCategoryImpl getCategory() {
		return currentCategory;
	}

	@Override
	public List<ConfigCategoryImpl> history() {
		return new ArrayList<>();
	}

	public void setCategory(ConfigCategoryImpl currentCategory) {
		this.currentCategory = currentCategory;
		this.optionCategoryTabs.setTabs(currentCategory.optionCategories().values().stream().map(ConfigTabButton::new).toList());
		this.configCategoryHistory.setHistory(currentCategory.history());
		this.optionListWidget.setOptions(currentCategory.optionEntries());
	}

	public void setCategory(String name) {
		setCategory(optionCategories.get(name));
	}
}
