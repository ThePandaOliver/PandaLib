package me.pandamods.pandalib.api.client.screen.config.option;

import me.pandamods.pandalib.PandaLib;
import me.pandamods.pandalib.api.client.screen.elements.UIElementHolder;
import me.pandamods.pandalib.api.client.screen.elements.widgets.buttons.IconButton;
import me.pandamods.pandalib.api.utils.PLCommonComponents;
import me.pandamods.pandalib.api.client.screen.layouts.PLGridLayout;
import me.pandamods.pandalib.api.utils.screen.PLGuiGraphics;
import me.pandamods.pandalib.api.utils.screen.WidgetImage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.layouts.SpacerElement;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.lang.reflect.Field;
import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class AbstractConfigOption<T> extends UIElementHolder {
	WidgetImage SAVE_ICON = new WidgetImage(
			new ResourceLocation(PandaLib.MOD_ID, "textures/gui/icon/save.png"),
			new ResourceLocation(PandaLib.MOD_ID, "textures/gui/icon/save_disabled.png")
	);
	WidgetImage RESET_ICON = new WidgetImage(
			new ResourceLocation(PandaLib.MOD_ID, "textures/gui/icon/reset.png"),
			new ResourceLocation(PandaLib.MOD_ID, "textures/gui/icon/reset_disabled.png")
	);
	WidgetImage UNDO_ICON = new WidgetImage(
			new ResourceLocation(PandaLib.MOD_ID, "textures/gui/icon/undo.png"),
			new ResourceLocation(PandaLib.MOD_ID, "textures/gui/icon/undo_disabled.png")
	);

	public final Component name;

	private Supplier<T> onLoad;
	private Consumer<T> onSave;
	private Supplier<T> onReset;

	public AbstractConfigOption(Component name, Field field) {
		this.name = name;
		this.setHeight(24);
	}

	@Override
	public void render(PLGuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
		Font font = Minecraft.getInstance().font;
		guiGraphics.drawString(font, name, this.getX() + 5, this.getY() + (this.getHeight() - font.lineHeight) / 2, 0xFFFFFF);

		super.render(guiGraphics, mouseX, mouseY, partialTick);
	}

	protected abstract void setValue(T value);
	protected abstract T getValue();

	public void save() {
		if (onSave != null) {
			onSave.accept(getValue());
		}
	}
	public void load() {
		if (onLoad != null) {
			setValue(onLoad.get());
		}
	}
	public void reset() {
		if (onReset != null) {
			setValue(onReset.get());
		}
	}

	public void setSaveListener(Consumer<T> onSave) {
		this.onSave = onSave;
	}

	public void setLoadListener(Supplier<T> onLoad) {
		this.onLoad = onLoad;
	}

	public void setResetListener(Supplier<T> onReset) {
		this.onReset = onReset;
	}

	protected void addActionButtons(PLGridLayout grid, int spacing) {
		int column = grid.getColumns();
		grid.addChild(SpacerElement.width(spacing), 0, column + 1);
//		grid.addChild(IconButton.builder(PLCommonComponents.SAVE, SAVE_ICON.get(true),
//				iconButton -> this.save()).build(), 0, column + 2);
		grid.addChild(IconButton.builder(PLCommonComponents.UNDO, UNDO_ICON.get(true),
				iconButton -> this.load()).build(), 0, column + 3);
		grid.addChild(IconButton.builder(PLCommonComponents.RESET, RESET_ICON.get(true),
				iconButton -> this.reset()).build(), 0, column + 4);
	}
}