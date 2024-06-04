package me.pandamods.pandalib.api.client.screen.elements;

import com.mojang.blaze3d.vertex.PoseStack;
import me.pandamods.pandalib.api.utils.screen.PLGuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import org.joml.Math;

import java.util.Optional;

public abstract class ScrollableUIElementHolder extends UIElementHolder {
	protected int scrollHorizontalAmount = 0;
	protected int scrollVerticalAmount = 0;

	public abstract int scrollHorizontalLength();
	public abstract int scrollVerticalLength();

	public boolean shouldShowHorizontalScrollbar() {
		return scrollHorizontalLength() > this.getWidth();
	}

	public boolean shouldShowVerticalScrollbar() {
		return scrollVerticalLength() > this.getHeight();
	}

	public int getScrollHorizontalAmount() {
		return scrollHorizontalAmount;
	}

	public int getScrollVerticalAmount() {
		return scrollVerticalAmount;
	}

	@Override
	public void render(PLGuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
		scrollHorizontalAmount = Math.clamp(0, scrollHorizontalLength() - this.getWidth(), scrollHorizontalAmount);
		scrollVerticalAmount = Math.clamp(0, scrollVerticalLength() - this.getHeight(), scrollVerticalAmount);

		PoseStack poseStack = guiGraphics.pose();
		poseStack.pushPose();
		poseStack.translate(-scrollHorizontalAmount, -scrollVerticalAmount, 0);
		guiGraphics.enableScissor(minX(), minY(), maxX(), maxY());
		renderElement(guiGraphics, mouseX, mouseY, partialTick);
		super.render(guiGraphics, mouseX + scrollHorizontalAmount, mouseY + scrollVerticalAmount, partialTick);
		guiGraphics.disableScissor();
		poseStack.popPose();
	}

	protected void renderElement(PLGuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {}

	@Override
	public Optional<UIElement> getElementAt(double mouseX, double mouseY) {
		mouseX += getScrollHorizontalAmount();
		mouseY += getScrollVerticalAmount();
		return super.getElementAt(mouseX, mouseY);
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		mouseX += getScrollHorizontalAmount();
		mouseY += getScrollVerticalAmount();
		return super.mouseClicked(mouseX, mouseY, button);
	}

	@Override
	public boolean mouseReleased(double mouseX, double mouseY, int button) {
		mouseX += getScrollHorizontalAmount();
		mouseY += getScrollVerticalAmount();
		return super.mouseReleased(mouseX, mouseY, button);
	}

	@Override
	public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
		mouseX += getScrollHorizontalAmount();
		mouseY += getScrollVerticalAmount();
		return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
	}

	@Override
	public void mouseMoved(double mouseX, double mouseY) {
		mouseX += getScrollHorizontalAmount();
		mouseY += getScrollVerticalAmount();
		super.mouseMoved(mouseX, mouseY);
	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
		mouseX += getScrollHorizontalAmount();
		mouseY += getScrollVerticalAmount();
		if (super.mouseScrolled(mouseX, mouseY, delta)) return true;

		if (Screen.hasShiftDown())
			scrollHorizontalAmount -= (int) delta * 5;
		else
			scrollVerticalAmount -= (int) delta * 5;
		return true;
	}

	@Override
	public boolean isInteractable() {
		return true;
	}
}
