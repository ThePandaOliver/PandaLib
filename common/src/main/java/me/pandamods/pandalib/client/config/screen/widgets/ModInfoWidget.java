package me.pandamods.pandalib.client.config.screen.widgets;

import dev.architectury.platform.Mod;
import me.pandamods.pandalib.client.screen.widgets.StringWidget;
import me.pandamods.pandalib.client.screen.widgets.WrappedStringWidget;
import me.pandamods.pandalib.config.ModConfigData;
import me.pandamods.pandalib.client.screen.widgets.ImageWidget;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.layouts.FrameLayout;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.layouts.SpacerElement;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.awt.*;
import java.net.URI;
import java.util.function.Consumer;

public class ModInfoWidget implements Renderable {
	private final int x;
	private final int y;
	private final int width;
	private final int height;
	private final ModConfigData modConfigData;
	private final Consumer<AbstractWidget> addRenderableWidget;
	private final Consumer<Renderable> addRenderableOnly;

	public ModInfoWidget(int x, int y, int width, int height, ModConfigData modConfigData,
						 Consumer<AbstractWidget> addRenderableWidget, Consumer<Renderable> addRenderableOnly) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.modConfigData = modConfigData;
		this.addRenderableWidget = addRenderableWidget;
		this.addRenderableOnly = addRenderableOnly;

		GridLayout gridLayout = new GridLayout();
		gridLayout.defaultCellSetting().alignHorizontallyCenter();
		GridLayout.RowHelper rowHelper = gridLayout.createRowHelper(2);

		if (this.modConfigData.iconLocation() != null) {
			rowHelper.addChild(new ImageWidget(this.modConfigData.iconLocation(), x, y, width, width), 2);
			rowHelper.addChild(SpacerElement.height(4), 1);
		}

		rowHelper.addChild(new StringWidget(Minecraft.getInstance().font, this.modConfigData.title(),
				x, y, width, Color.WHITE), 2);
		rowHelper.addChild(SpacerElement.height(2), 2);
		rowHelper.addChild(new WrappedStringWidget(Minecraft.getInstance().font, this.modConfigData.description(),
				x, y, width, Color.WHITE), 2);

		gridLayout.arrangeElements();
		FrameLayout.alignInRectangle(gridLayout, x, y,
				width, height, 0, 0);
		gridLayout.visitChildren(layoutElement -> {
			if (layoutElement instanceof AbstractWidget abstractWidget) {
				addRenderableWidget.accept(abstractWidget);
			} else if (layoutElement instanceof Renderable renderable) {
				addRenderableOnly.accept(renderable);
			}
		});
	}

	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {}
}
