package me.pandamods.pandalib.client.screen.widgets;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.layouts.LayoutElement;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.util.FormattedCharSequence;

import java.awt.*;
import java.util.List;
import java.util.function.Consumer;

public class WrappedStringWidget implements Renderable, LayoutElement {
	private final Font font;
	private final List<FormattedCharSequence> strings;
	private int x;
	private int y;
	private final int width;
	private final Color color;

	public WrappedStringWidget(Font font, Component text, int x, int y, int width, Color color) {
		this.font = font;
		this.x = x;
		this.y = y;
		this.width = width;
		this.color = color;

		this.strings = font.split(FormattedText.of(text.getString(), text.getStyle()), width);
	}

	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
		int y = this.y;
		for (FormattedCharSequence line : this.strings) {
			guiGraphics.drawString(font, line, x, y, this.color.getRGB());
			y += font.lineHeight;
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
		return this.strings.size() * font.lineHeight;
	}

	@Override
	public void visitWidgets(Consumer<AbstractWidget> consumer) {

	}
}
