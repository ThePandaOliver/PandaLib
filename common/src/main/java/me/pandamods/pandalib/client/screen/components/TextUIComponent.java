/*
 * Copyright (C) 2024 Oliver Froberg (The Panda Oliver)
 *
 * This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 * You should have received a copy of the GNU Lesser General Public License
 *  along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package me.pandamods.pandalib.client.screen.components;

import me.pandamods.pandalib.client.screen.BaseUIComponent;
import me.pandamods.pandalib.client.screen.utils.RenderContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class TextUIComponent extends BaseUIComponent {
	private Font font;
	private Component text;

	private Color color = Color.WHITE;

	public TextUIComponent(Font font, Component text) {
		this.font = font;
		this.text = text;

		this.setHeight(font.lineHeight);
		this.setWidth(font.width(text));
	}

	public TextUIComponent(Font font) {
		this(font, Component.empty());
	}

	public TextUIComponent() {
		this(Minecraft.getInstance().font, Component.empty());
	}

	@Override
	public void render(RenderContext context, int mouseX, int mouseY, float partialTicks) {
		GuiGraphics graphics = context.guiGraphics;
		graphics.drawString(this.font, this.text, this.x, this.y, color.getRGB());
	}

	public void setText(@NotNull String text) {
		this.text = Component.literal(text);
	}

	public void setText(@NotNull Component text) {
		this.text = text;
	}

	public Component getText() {
		return text;
	}

	public void setFont(Font font) {
		this.font = font;
	}

	public Font getFont() {
		return font;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public Color getColor() {
		return color;
	}
}
