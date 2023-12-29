package me.pandamods.pandalib.client.screen.widgets;

import net.minecraft.Util;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.layouts.LayoutElement;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;

import java.awt.*;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class StringWidget implements Renderable, LayoutElement {
	private final Font font;
	private final Component text;
	private int x;
	private int y;
	private final int width;
	private final Color color;

	public StringWidget(Font font, Component text, int x, int y, int width, Color color) {
		this.font = font;
		this.text = text;
		this.x = x;
		this.y = y;
		this.width = width;
		this.color = color;
	}

	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
		int textWidth = font.width(text);
		if (textWidth > this.width) {
			guiGraphics.enableScissor(this.x, this.y, this.x + this.getWidth(), this.y + this.getHeight());
			guiGraphics.drawString(font, text, this.x, this.y, color.getRGB());
			guiGraphics.disableScissor();
		} else {
			guiGraphics.drawCenteredString(font, text, this.x + (this.width / 2), this.y, color.getRGB());
		}
	}

	@Override
	public void setX(int x) {
		this.x = x;
	}

	@Override
	public void setY(int y) {
		this.y = y;
	}

	@Override
	public int getX() {
		return x;
	}

	@Override
	public int getY() {
		return y;
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getHeight() {
		return font.lineHeight;
	}

	@Override
	public void visitWidgets(Consumer<AbstractWidget> consumer) {}
}
