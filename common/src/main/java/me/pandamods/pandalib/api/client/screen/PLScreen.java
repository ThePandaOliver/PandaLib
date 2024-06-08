package me.pandamods.pandalib.api.client.screen;

import com.google.common.annotations.VisibleForTesting;
import me.pandamods.pandalib.api.client.screen.elements.UIElement;
import me.pandamods.pandalib.api.client.screen.elements.UIElementHolder;
import me.pandamods.pandalib.api.utils.screen.PLGuiGraphics;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ComponentPath;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.navigation.FocusNavigationEvent;
import net.minecraft.client.gui.navigation.ScreenDirection;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public abstract class PLScreen extends UIElementHolder {
	private final CompiledPLScreen compiledScreen;
	protected Font font;

	private boolean initialized = false;

	public PLScreen() {
		this.compiledScreen = new CompiledPLScreen(this);
		this.font = this.getMinecraft().font;
	}

	public CompiledPLScreen compileScreen() {
		return compiledScreen;
	}

	public abstract Component getTitle();

	@Override
	public PLScreen getScreen() {
		return this;
	}

	public final void init(Minecraft minecraft, int width, int height) {
		this.font = minecraft.font;
		setSize(width, height);
		if (!this.initialized) {
			this.init();
		} else {
			this.repositionElements();
		}

		this.initialized = true;
	}

	public void resize(Minecraft minecraft, int width, int height) {
		setSize(width, height);
		this.repositionElements();
	}

	public void setFocused(GuiEventListener element) {
		compiledScreen.setFocused(element);
	}

	public GuiEventListener getFocused() {
		return compiledScreen.getFocused();
	}

	@Override
	public void setFocused(boolean focused) {
		compiledScreen.setFocused(focused);
	}

	@Override
	public boolean isFocused() {
		return compiledScreen.isFocused();
	}

	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		if (keyCode == 256 && this.shouldCloseOnEsc()) {
			this.onClose();
			return true;
		} else if (this.getFocused() != null && this.getFocused().keyPressed(keyCode, scanCode, modifiers)) {
			return true;
		}

		FocusNavigationEvent navigationEvent = switch (keyCode) {
			case 258 -> this.createTabEvent();
			case 262 -> this.createArrowEvent(ScreenDirection.RIGHT);
			case 263 -> this.createArrowEvent(ScreenDirection.LEFT);
			case 264 -> this.createArrowEvent(ScreenDirection.DOWN);
			case 265 -> this.createArrowEvent(ScreenDirection.UP);
			default -> null;
		};

		if (navigationEvent != null) {
			ComponentPath componentPath = super.nextFocusPath(navigationEvent);

			if (componentPath == null && navigationEvent instanceof FocusNavigationEvent.TabNavigation) {
				this.clearFocus();
				componentPath = super.nextFocusPath(navigationEvent);
			}

			if (componentPath != null) {
				this.changeFocus(componentPath);
			}
		}
		return false;
	}

	@Override
	public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
		if (this.getFocused() != null) {
    		return this.getFocused().keyReleased(keyCode, scanCode, modifiers);
    	}
		return false;
	}

	@Override
	public boolean charTyped(char codePoint, int modifiers) {
		if (this.getFocused() != null) {
    		return this.getFocused().charTyped(codePoint, modifiers);
    	}
		return false;
	}

	private FocusNavigationEvent.TabNavigation createTabEvent() {
		boolean bl = !Screen.hasShiftDown();
		return new FocusNavigationEvent.TabNavigation(bl);
	}

	private FocusNavigationEvent.ArrowNavigation createArrowEvent(ScreenDirection direction) {
		return new FocusNavigationEvent.ArrowNavigation(direction);
	}

//	protected void setInitialFocus(GuiEventListener listener) {
//		ComponentPath componentPath = ComponentPath.path(this, listener.nextFocusPath(new FocusNavigationEvent.InitialFocus()));
//		if (componentPath != null) {
//			this.changeFocus(componentPath);
//		}
//	}

	public final void clearFocus() {
		ComponentPath componentPath = this.getCurrentFocusPath();
		if (componentPath != null) {
			componentPath.applyFocus(false);
		}

	}

	@VisibleForTesting
	protected void changeFocus(ComponentPath path) {
		this.clearFocus();
		path.applyFocus(true);
	}

	public void removed() {
	}

	public void added() {
	}

	public boolean shouldCloseOnEsc() {
		return true;
	}

	public boolean isPauseScreen() {
		return true;
	}

	public void onClose() {
		this.getMinecraft().setScreen(null);
	}

	public void renderBackground(PLGuiGraphics guiGraphics) {
		if (this.getMinecraft().level != null) {
			guiGraphics.fillGradient(0, 0, this.getWidth(), this.getHeight(), -1072689136, -804253680);
		} else {
			this.renderDirtBackground(guiGraphics);
		}
	}

	public void renderDirtBackground(PLGuiGraphics guiGraphics) {
		guiGraphics.setColor(0.25F, 0.25F, 0.25F, 1.0F);
		guiGraphics.blit(Screen.BACKGROUND_LOCATION, 0, 0, 0, 0.0F, 0.0F,
				this.getWidth(), this.getHeight(), 32, 32);
		guiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
	}
}