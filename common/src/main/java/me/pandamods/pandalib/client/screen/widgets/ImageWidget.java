package me.pandamods.pandalib.client.screen.widgets;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.layouts.LayoutElement;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Consumer;

@Environment(EnvType.CLIENT)
public class ImageWidget implements Renderable, LayoutElement {
	private final ResourceLocation imageLocation;
	private int x;
	private int y;
	private final int width;
	private final int height;
	private final int uOffset;
	private final int vOffset;
	private final int textureWidth;
	private final int textureHeight;

	public ImageWidget(ResourceLocation imageLocation, int x, int y, int width, int height) {
		this(imageLocation, x, y, width, height, 0, 0, width, height);
	}

	public ImageWidget(ResourceLocation imageLocation, int x, int y, int width, int height,
					   int uOffset, int vOffset, int textureWidth, int textureHeight) {
		this.imageLocation = imageLocation;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.uOffset = uOffset;
		this.vOffset = vOffset;
		this.textureWidth = textureWidth;
		this.textureHeight = textureHeight;
	}

	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
		guiGraphics.blit(imageLocation, this.x, this.y, this.uOffset, this.vOffset,
				this.width, this.height, this.textureWidth, this.textureHeight);
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
		return height;
	}

	@Override
	public void visitWidgets(Consumer<AbstractWidget> consumer) {

	}
}
