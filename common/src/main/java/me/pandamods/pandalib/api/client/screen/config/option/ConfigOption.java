package me.pandamods.pandalib.api.client.screen.config.option;

import me.pandamods.pandalib.PandaLib;
import me.pandamods.pandalib.api.client.screen.UIComponentHolder;
import me.pandamods.pandalib.api.client.screen.widget.IconButton;
import me.pandamods.pandalib.api.utils.PLCommonComponents;
import me.pandamods.pandalib.api.utils.screen.PLGridLayout;
import me.pandamods.pandalib.api.utils.screen.WidgetImage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.layouts.SpacerElement;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class ConfigOption<T> extends UIComponentHolder {
	public static final WidgetImage SAVE_ICON = new WidgetImage(
			new ResourceLocation(PandaLib.MOD_ID, "textures/gui/icon/save.png"),
			new ResourceLocation(PandaLib.MOD_ID, "textures/gui/icon/save_disabled.png")
	);
	public static final WidgetImage RESET_ICON = new WidgetImage(
			new ResourceLocation(PandaLib.MOD_ID, "textures/gui/icon/reset.png"),
			new ResourceLocation(PandaLib.MOD_ID, "textures/gui/icon/reset_disabled.png")
	);
	public static final WidgetImage UNDO_ICON = new WidgetImage(
			new ResourceLocation(PandaLib.MOD_ID, "textures/gui/icon/undo.png"),
			new ResourceLocation(PandaLib.MOD_ID, "textures/gui/icon/undo_disabled.png")
	);

	public final Component name;
	protected final Supplier<T> load;
	protected final Consumer<T> save;
	protected final Supplier<T> loadDefault;

	public ConfigOption(Component name, Supplier<T> load, Consumer<T> save, Supplier<T> loadDefault) {
		this.name = name;
		this.load = load;
		this.save = save;
		this.loadDefault = loadDefault;
	}

	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
		Font font = Minecraft.getInstance().font;
		guiGraphics.drawString(font, name, this.getX() + 5, this.getY() + (this.getHeight() - font.lineHeight) / 2, 0xFFFFFF);

		super.render(guiGraphics, mouseX, mouseY, partialTick);
	}

	protected abstract void setValue(T value);
	protected abstract T getValue();

	public void save() {
		this.save.accept(this.getValue());
	}
	public void load() {
		this.setValue(this.load.get());
	}
	public void reset() {
		this.setValue(this.loadDefault.get());
	}

	protected void addActionButtons(PLGridLayout grid, int spacing) {
		int column = grid.getColumns();
		grid.addChild(SpacerElement.width(spacing), 0, column + 1);
		grid.addChild(IconButton.builder(PLCommonComponents.SAVE, SAVE_ICON.get(true),
				iconButton -> this.save()).build(), 0, column + 2);
		grid.addChild(IconButton.builder(PLCommonComponents.UNDO, UNDO_ICON.get(true),
				iconButton -> this.load()).build(), 0, column + 3);
		grid.addChild(IconButton.builder(PLCommonComponents.RESET, RESET_ICON.get(true),
				iconButton -> this.reset()).build(), 0, column + 4);
	}
}