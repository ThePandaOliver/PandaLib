package me.pandamods.pandalib.client.config.screen;

import me.pandamods.pandalib.client.config.screen.widgets.ConfigOptionList;
import me.pandamods.pandalib.client.screen.PandaLibScreen;
import me.pandamods.pandalib.config.Config;
import me.pandamods.pandalib.config.ConfigHolder;
import me.pandamods.pandalib.config.ConfigRegistry;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.layouts.FrameLayout;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.Map;

public class ConfigScreen extends PandaLibScreen {
	private final ConfigHolder<?> configHolder;
	private final ConfigOptionList optionListWidget;

	public static final Component CANCEL = Component.translatable("gui.pandalib.cancel");
	public static final Component SAVE = Component.translatable("gui.pandalib.save");
	public static final Component RESET = Component.translatable("gui.pandalib.reset");

	public ConfigScreen(Screen parent, ConfigHolder<?> configHolder) {
		super(parent, GetTitle(configHolder));
		this.configHolder = configHolder;

		this.optionListWidget = new ConfigOptionList(this, null, configHolder);
	}

	@Override
	public void init() {
		int bottom = 40;
		optionListWidget.setBounds(0, 30, width, height - bottom);
		this.addWidget(optionListWidget);

		GridLayout gridLayout = new GridLayout();
		gridLayout.defaultCellSetting().padding(2).alignVerticallyMiddle().alignHorizontallyCenter();
		GridLayout.RowHelper rowHelper = gridLayout.createRowHelper(3);

		rowHelper.addChild(Button.builder(CANCEL, button -> setScreen(getParentScreen())).width(100).build());
		rowHelper.addChild(Button.builder(RESET, button -> reset()).width(100).build());
		rowHelper.addChild(Button.builder(SAVE, button -> save()).width(100).build());

		gridLayout.arrangeElements();
		FrameLayout.alignInRectangle(gridLayout, 0, this.height - bottom, this.width, bottom, 0.5f, 0.5f);
		gridLayout.visitWidgets(this::addRenderableWidget);

		super.init();
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
		this.optionListWidget.getOptions().forEach((field, widget) -> {
//			try {
//				widget.load(field.get(this.configHolder.getNewDefault()));
//			} catch (IllegalAccessException e) {
//				throw new RuntimeException(e);
//			}
		});
	}

	public static Screen Create(String modId, Screen parentScreen) {
		Map<Class<?>, ConfigHolder<?>> configs = ConfigRegistry.getConfigs(modId);
		if (configs.size() == 1) {
			return new ConfigScreen(parentScreen, configs.values().stream().findFirst().get());
		}
		return new ConfigScreen(parentScreen, configs.values().stream().findFirst().get());
	}

	public static Component GetTitle(ConfigHolder<?> configHolder) {
		Config config = configHolder.getDefinition();
		return Component.literal("config." + config.modId() + "." + config.name() + ".title");
	}
}
