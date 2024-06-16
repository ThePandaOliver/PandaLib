package me.pandamods.pandalib.api.client.screen.elements.widgets.inputs;

import me.pandamods.pandalib.api.client.screen.PLRenderable;
import me.pandamods.pandalib.api.client.screen.elements.AbstractUIElement;
import me.pandamods.pandalib.api.client.screen.elements.widgets.buttons.AbstractPLButton;
import me.pandamods.pandalib.api.utils.screen.PLGuiGraphics;
import org.joml.Math;

import java.awt.*;

public class Slider extends AbstractUIElement implements PLRenderable {
	private final double min;
	private final double max;
	private final float steps;
	private double value;

	private boolean dragging = false;

	private Runnable onChange;

	public Slider(double min, double max, float steps) {
		this.min = min;
		this.max = max;
		this.steps = steps;
	}

	public void onChangeListener(Runnable onChange) {
		this.onChange = onChange;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public double getValue() {
		return value;
	}

	@Override
	public void render(PLGuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
		checkHoverState(mouseX, mouseY);
		int y = this.minY() + (this.getHeight() - 2) / 2;
		guiGraphics.fill(minX(), y, maxX(), y + 2, Color.gray.getRGB());

    	int sliderX = 3 + minX() + (int) ((value - min) / (max - min) * (getWidth() - 6));
		guiGraphics.blitSprite(AbstractPLButton.SPRITES.get(this.isActive(), this.isHoveredOrFocused()), sliderX - 3, getY(), 6, getHeight());
	}

	public void moveBar(double mouseX) {
		double percentage = (mouseX - minX()) / (double) (maxX() - minX());
		double newValue = (min + (max - min) * percentage);
		newValue = Math.round(newValue / steps) * steps;
		value = Math.clamp(min, max, newValue);
		if (onChange != null)
			onChange.run();
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		dragging = true;
		moveBar(mouseX);
		return true;
	}

	@Override
	public boolean mouseReleased(double mouseX, double mouseY, int button) {
		dragging = false;
		return true;
	}

	@Override
	public void mouseMoved(double mouseX, double mouseY) {
		if (dragging) {
			moveBar(mouseX);
		}
		super.mouseMoved(mouseX, mouseY);
	}
}
