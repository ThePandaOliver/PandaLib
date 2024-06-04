package me.pandamods.pandalib.api.client.screen.converters;

import me.pandamods.pandalib.api.client.screen.PLRenderable;
import me.pandamods.pandalib.api.client.screen.PLScreen;
import me.pandamods.pandalib.api.client.screen.elements.UIElement;
import me.pandamods.pandalib.api.utils.screen.PLGuiGraphics;
import me.pandamods.pandalib.api.utils.screen.WidgetHooks;
import net.minecraft.client.gui.ComponentPath;
import net.minecraft.client.gui.components.AbstractWidget;
import me.pandamods.pandalib.api.client.screen.layouts.PLLayoutElement;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.navigation.FocusNavigationEvent;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.screens.Screen;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;

/**
 * A class that converts a {@link AbstractWidget} object to a class implementing {@link UIElement}, {@link PLLayoutElement}, {@link PLRenderable} and {@link NarratableEntry}.
 */
public class AbstractWidgetConverter implements UIElement, PLLayoutElement, PLRenderable, NarratableEntry {
	private final AbstractWidget widget;

	private PLScreen screen;
	private UIElement parent;

	public AbstractWidgetConverter(AbstractWidget widget) {
		this.widget = widget;
	}

	@Override
	public PLScreen getScreen() {
		return this.screen;
	}

	@Override
	public void setScreen(PLScreen screen) {
		this.screen = screen;
	}

	@Override
	public Optional<UIElement> getParent() {
		return Optional.ofNullable(this.parent);
	}

	@Override
	public void setParent(UIElement parent) {
		this.parent = parent;
	}

	@Override
	public void setX(int x) {
		WidgetHooks.setX(this.widget, x);
	}

	@Override
	public void setY(int y) {
		WidgetHooks.setY(this.widget, y);
	}

	@Override
	public void setWidth(int width) {
		WidgetHooks.setWidth(this.widget, width);
	}

	@Override
	public void setHeight(int height) {
		WidgetHooks.setHeight(this.widget, height);
	}

	@Override
	public int getX() {
		return this.widget.getX();
	}

	@Override
	public int getY() {
		return this.widget.getY();
	}

	@Override
	public int getRelativeX() {
		return this.widget.getX();
	}

	@Override
	public int getRelativeY() {
		return this.widget.getY();
	}

	@Override
	public int getWidth() {
		return this.widget.getWidth();
	}

	@Override
	public int getHeight() {
		return this.widget.getHeight();
	}

	@Override
	public void setFocused(boolean focused) {
		this.widget.setFocused(focused);
	}

	@Override
	public boolean isFocused() {
		return this.widget.isFocused();
	}

	@Override
	public void mouseMoved(double mouseX, double mouseY) {
		this.widget.mouseMoved(mouseX, mouseY);
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		return this.widget.mouseClicked(mouseX, mouseY, button);
	}

	@Override
	public boolean mouseReleased(double mouseX, double mouseY, int button) {
		return this.widget.mouseReleased(mouseX, mouseY, button);
	}

	@Override
	public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
		return this.widget.mouseDragged(mouseX, mouseY, button, dragX, dragY);
	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
		return this.widget.mouseScrolled(mouseX, mouseY, delta);
	}

	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		return this.widget.keyPressed(keyCode, scanCode, modifiers);
	}

	@Override
	public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
		return this.widget.keyReleased(keyCode, scanCode, modifiers);
	}

	@Override
	public boolean charTyped(char codePoint, int modifiers) {
		return this.widget.charTyped(codePoint, modifiers);
	}

	@Nullable
	@Override
	public ComponentPath nextFocusPath(FocusNavigationEvent event) {
		return this.widget.nextFocusPath(event);
	}

	@Nullable
	@Override
	public ComponentPath getCurrentFocusPath() {
		return this.widget.getCurrentFocusPath();
	}

	@Override
	public ScreenRectangle getRectangle() {
		return this.widget.getRectangle();
	}

	@Override
	public int getTabOrderGroup() {
		return this.widget.getTabOrderGroup();
	}

	@Override
	public void render(PLGuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
//		guiGraphics.pose().pushPose();
//		getParent().ifPresent(uiElement -> guiGraphics.pose().translate(-uiElement.getX(), -uiElement.getY(), 0));
		this.widget.render(guiGraphics, mouseX, mouseY, partialTick);
//		guiGraphics.pose().popPose();
	}

	@Override
	public NarrationPriority narrationPriority() {
		return this.widget.narrationPriority();
	}

	@Override
	public boolean isActive() {
		return this.widget.isActive();
	}

	public void setActive(boolean active) {
		this.widget.active = active;
	}

	@Override
	public void updateNarration(NarrationElementOutput narrationElementOutput) {
		this.widget.updateNarration(narrationElementOutput);
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) return true;
		if (object == null) return false;
		return Objects.equals(widget, object);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(widget);
	}
}
