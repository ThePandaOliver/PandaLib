package me.pandamods.pandalib.client.screen.api.config;

import me.pandamods.pandalib.client.screen.api.PLScreen;
import me.pandamods.pandalib.client.screen.api.WidgetImpl;
import me.pandamods.pandalib.utils.GuiUtils;
import me.pandamods.pandalib.utils.RenderUtils;
import me.pandamods.pandalib.utils.animation.interpolation.NumberInterpolator;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;

import java.awt.*;

public class ConfigCategoryList extends WidgetImpl {
	public static final int COLLAPSED_SIZE = 20;
	public static final int OPEN_SIZE = 100;

	private final NumberInterpolator widthInterpolator = new NumberInterpolator(COLLAPSED_SIZE).setDuration(1f);

	public ConfigCategoryList(PLScreen screen) {
		super(screen);
	}

	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
		widthInterpolator.update();
		guiGraphics.fill(maxX()-2, minY(), maxX(), maxY(), Color.black.getRGB());
		super.render(guiGraphics, mouseX, mouseY, partialTick);
	}

	@Override
	public int getLocalX() {
		return 0;
	}

	@Override
	public int getLocalY() {
		return 0;
	}

	@Override
	public int width() {
		return widthInterpolator.getAsInt();
	}

	@Override
	public int height() {
		return this.screen.height;
	}
}
