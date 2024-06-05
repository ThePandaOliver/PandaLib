package me.pandamods.pandalib.api.client.screen;

import me.pandamods.pandalib.api.client.screen.elements.UIElement;
import me.pandamods.pandalib.api.client.screen.elements.UIElementHolder;
import me.pandamods.pandalib.api.utils.screen.PLGuiGraphics;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public abstract class PLScreen extends UIElementHolder {
	private final CompiledPLScreen compiledScreen;
	protected Minecraft minecraft;
	protected Font font;

	private boolean initialized = false;

	public PLScreen() {
		this.compiledScreen = new CompiledPLScreen(this);
		this.minecraft = Minecraft.getInstance();
		this.font = this.minecraft.font;
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
		this.minecraft = minecraft;
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
		if (this.getFocused() != null) {
    		return this.getFocused().keyPressed(keyCode, scanCode, modifiers);
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

	public void tick() {
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
		this.minecraft.setScreen(null);
	}

	public void renderBackground(PLGuiGraphics guiGraphics) {
		if (this.minecraft.level != null) {
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