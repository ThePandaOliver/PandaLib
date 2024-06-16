/*
 * Copyright (C) 2024 Oliver Froberg (The Panda Oliver)
 *
 * This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 * You should have received a copy of the GNU General Public License
 *  along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package me.pandamods.pandalib.api.client.screen.config.option;

import me.pandamods.pandalib.PandaLib;
import me.pandamods.pandalib.api.client.screen.elements.UIElementHolder;
import me.pandamods.pandalib.api.client.screen.elements.widgets.buttons.IconButton;
import me.pandamods.pandalib.api.client.screen.layouts.PLGrid;
import me.pandamods.pandalib.api.utils.PLCommonComponents;
import me.pandamods.pandalib.api.client.screen.layouts.PLGridLayout;
import me.pandamods.pandalib.api.utils.screen.PLGuiGraphics;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.layouts.SpacerElement;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.awt.*;
import java.lang.reflect.Field;
import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class AbstractConfigOption<T> extends UIElementHolder {
	WidgetSprites RESET_ICON = new WidgetSprites(
			ResourceLocation.fromNamespaceAndPath(PandaLib.MOD_ID, "textures/gui/icon/reset.png"),
			ResourceLocation.fromNamespaceAndPath(PandaLib.MOD_ID, "textures/gui/icon/reset_disabled.png"),
			ResourceLocation.fromNamespaceAndPath(PandaLib.MOD_ID, "textures/gui/icon/reset.png")
	);
	WidgetSprites UNDO_ICON = new WidgetSprites(
			ResourceLocation.fromNamespaceAndPath(PandaLib.MOD_ID, "textures/gui/icon/undo.png"),
			ResourceLocation.fromNamespaceAndPath(PandaLib.MOD_ID, "textures/gui/icon/undo_disabled.png"),
			ResourceLocation.fromNamespaceAndPath(PandaLib.MOD_ID, "textures/gui/icon/undo.png")
	);

	public final Component name;
	private Component errorMessage = null;

	private Supplier<T> onLoad;
	private Consumer<T> onSave;
	private Supplier<T> onReset;

	public AbstractConfigOption(Component name, Field field) {
		this.name = name;
		this.setHeight(24);
	}

	@Override
	public void render(PLGuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
		renderText(guiGraphics);
		super.render(guiGraphics, mouseX, mouseY, partialTick);
	}

	protected void renderText(PLGuiGraphics guiGraphics) {
		int color = hasError() ? Color.red.getRGB() : Color.white.getRGB();
		Font font = this.getMinecraft().font;
		guiGraphics.drawString(font, name, this.getX() + 5, this.getY() + (this.getHeight() - font.lineHeight) / 2, color);
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

	protected void addActionButtons(PLGrid grid, int spacing) {
		int column = grid.getColumns();
//		grid.addChild(SpacerElement.width(spacing), 0, column + 1);
		grid.addChild(IconButton.builder(PLCommonComponents.UNDO, UNDO_ICON,
				iconButton -> this.load()).build(), 0, column + 3);
		grid.addChild(IconButton.builder(PLCommonComponents.RESET, RESET_ICON,
				iconButton -> this.reset()).build(), 0, column + 4);
	}

	public boolean hasError() {
		return false;
	}

	public Component getErrorMessage() {
		return errorMessage;
	}
}