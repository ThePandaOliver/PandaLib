/*
 * Copyright (C) 2024 Oliver Froberg (The Panda Oliver)
 *
 * This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 * You should have received a copy of the GNU General Public License
 *  along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package me.pandamods.pandalib.api.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import me.pandamods.pandalib.api.client.screen.elements.UIElement;
import me.pandamods.pandalib.api.client.screen.elements.UIElementHolder;
import me.pandamods.pandalib.api.client.screen.elements.UIElementHolderAccessor;
import me.pandamods.pandalib.api.utils.screen.PLGuiGraphics;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public abstract class PLScreen implements UIElementHolderAccessor {
	private final List<UIElement> children = new ArrayList<>();
	private final List<NarratableEntry> narratables = new ArrayList<>();
	private final List<UIElementHolder> holders = new ArrayList<>();
	private final List<PLRenderable> renderables = new ArrayList<>();

	private final CompiledPLScreen compiledScreen;
	protected Font font;

	private int width = 0;
	private int height = 0;

	private boolean initialized = false;

	public PLScreen() {
		this.compiledScreen = new CompiledPLScreen(this);
		this.font = getMinecraft().font;
	}

	protected Minecraft getMinecraft() {
		return Minecraft.getInstance();
	}

	public final int getWidth() {
		return width;
	}

	public final int getHeight() {
		return height;
	}

	@Override
	public List<UIElement> getChildren() {
		return children;
	}

	@Override
	public List<NarratableEntry> getNarratables() {
		return narratables;
	}

	@Override
	public List<UIElementHolder> getHolders() {
		return holders;
	}

	@Override
	public List<PLRenderable> getRenderables() {
		return renderables;
	}

	@Override
	public void addElement(UIElement element) {
		element.setParent(null);
		element.setScreen(this);
		UIElementHolderAccessor.super.addElement(element);
	}

	public CompiledPLScreen compileScreen() {
		return compiledScreen;
	}

	public abstract Component getTitle();

	public final void init(Minecraft minecraft, int width, int height) {
		this.font = minecraft.font;
		this.width = width;
		this.height = height;
		if (!this.initialized) {
			this.init();
		} else {
			this.repositionElements();
		}

		this.initialized = true;
	}

	public void resize(Minecraft minecraft, int width, int height) {
		this.width = width;
		this.height = height;
		this.repositionElements();
	}

	public void setFocused(GuiEventListener element) {
		compiledScreen.setFocused(element);
	}

	public GuiEventListener getFocused() {
		return compiledScreen.getFocused();
	}

	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		if (keyCode == 256 && this.shouldCloseOnEsc()) {
			this.close();
			return true;
		} else if (this.getFocused() != null && this.getFocused().keyPressed(keyCode, scanCode, modifiers)) {
			return true;
		}

//		FocusNavigationEvent navigationEvent = switch (keyCode) {
//			case 258 -> this.createTabEvent();
//			case 262 -> this.createArrowEvent(ScreenDirection.RIGHT);
//			case 263 -> this.createArrowEvent(ScreenDirection.LEFT);
//			case 264 -> this.createArrowEvent(ScreenDirection.DOWN);
//			case 265 -> this.createArrowEvent(ScreenDirection.UP);
//			default -> null;
//		};
//
//		if (navigationEvent != null) {
//			ComponentPath componentPath = super.nextFocusPath(navigationEvent);
//
//			if (componentPath == null && navigationEvent instanceof FocusNavigationEvent.TabNavigation) {
//				this.clearFocus();
//				componentPath = super.nextFocusPath(navigationEvent);
//			}
//
//			if (componentPath != null) {
//				this.changeFocus(componentPath);
//			}
//		}
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

//	private FocusNavigationEvent.TabNavigation createTabEvent() {
//		boolean bl = !Screen.hasShiftDown();
//		return new FocusNavigationEvent.TabNavigation(bl);
//	}

//	private FocusNavigationEvent.ArrowNavigation createArrowEvent(ScreenDirection direction) {
//		return new FocusNavigationEvent.ArrowNavigation(direction);
//	}

//	protected void setInitialFocus(GuiEventListener listener) {
//		ComponentPath componentPath = ComponentPath.path(this, listener.nextFocusPath(new FocusNavigationEvent.InitialFocus()));
//		if (componentPath != null) {
//			this.changeFocus(componentPath);
//		}
//	}

//	public final void clearFocus() {
//		ComponentPath componentPath = this.getCurrentFocusPath();
//		if (componentPath != null) {
//			componentPath.applyFocus(false);
//		}
//	}

//	@VisibleForTesting
//	protected void changeFocus(ComponentPath path) {
//		this.clearFocus();
//		path.applyFocus(true);
//	}

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

	public void close() {
		this.getMinecraft().setScreen(null);
	}

	public void open() {
		this.getMinecraft().setScreen(this.compileScreen());
	}

	public void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
		if (this.getMinecraft().level == null) {
			this.renderPanorama(guiGraphics, partialTick);
		}

		this.renderBlurredBackground(partialTick);
		this.renderMenuBackground(guiGraphics);
	}

	protected void renderBlurredBackground(float partialTick) {
		this.getMinecraft().gameRenderer.processBlurEffect(partialTick);
		this.getMinecraft().getMainRenderTarget().bindWrite(false);
	}

	protected void renderPanorama(GuiGraphics guiGraphics, float partialTick) {
		Screen.PANORAMA.render(guiGraphics, this.width, this.height, 1.0F, partialTick);
	}

	protected void renderMenuBackground(GuiGraphics partialTick) {
		this.renderMenuBackground(partialTick, 0, 0, this.width, this.height);
	}

	protected void renderMenuBackground(GuiGraphics guiGraphics, int x, int y, int width, int height) {
		renderMenuBackgroundTexture(guiGraphics, this.getMinecraft().level == null ? Screen.MENU_BACKGROUND :
				Screen.INWORLD_MENU_BACKGROUND, x, y, 0.0F, 0.0F, width, height);
	}

	public static void renderMenuBackgroundTexture(GuiGraphics guiGraphics, ResourceLocation texture, int x, int y,
												   float uOffset, float vOffset, int width, int height) {
		RenderSystem.enableBlend();
		guiGraphics.blit(texture, x, y, 0, uOffset, vOffset, width, height, 32, 32);
		RenderSystem.disableBlend();
	}

	public void renderTransparentBackground(GuiGraphics guiGraphics) {
		guiGraphics.fillGradient(0, 0, this.width, this.height, -1072689136, -804253680);
	}

	public static float getDeltaSeconds() {
		return Minecraft.getInstance().getTimer().getGameTimeDeltaTicks() / 20.0f;
	}
}