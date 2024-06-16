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

import me.pandamods.pandalib.api.utils.screen.PLGuiGraphics;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ComponentPath;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.navigation.FocusNavigationEvent;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.sounds.Music;
import net.minecraft.util.FormattedCharSequence;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

public class CompiledPLScreen extends Screen {
	private final PLScreen screen;

	public CompiledPLScreen(PLScreen screen) {
		super(Component.empty());
		this.screen = screen;
	}

	@Override
	public Component getTitle() {
		return screen.getTitle();
	}

	@Override
	protected void init() {
		screen.init(this.minecraft, this.width, this.height);
	}

	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
		PLGuiGraphics graphics = new PLGuiGraphics(guiGraphics);
		screen.render(graphics, mouseX, mouseY, partialTick);
	}

	@Override
	public Component getNarrationMessage() {
		return super.getNarrationMessage();
	}

	@Override
	protected void setInitialFocus(GuiEventListener listener) {
		super.setInitialFocus(listener);
	}

	@Override
	protected void changeFocus(ComponentPath path) {
		super.changeFocus(path);
	}

	@Override
	public boolean shouldCloseOnEsc() {
		return screen.shouldCloseOnEsc();
	}

	@Override
	public void onClose() {
		screen.close();
	}

	@Override
	protected <T extends GuiEventListener & Renderable & NarratableEntry> T addRenderableWidget(T widget) {
		return widget;
	}

	@Override
	protected <T extends Renderable> T addRenderableOnly(T renderable) {
		return renderable;
	}

	@Override
	protected <T extends GuiEventListener & NarratableEntry> T addWidget(T listener) {
		return listener;
	}

	@Override
	protected void removeWidget(GuiEventListener listener) {
	}

	@Override
	protected void clearWidgets() {
		screen.clearElements();
	}

	@Override
	protected void insertText(String text, boolean overwrite) {
		super.insertText(text, overwrite);
	}

	@Override
	public boolean handleComponentClicked(@Nullable Style style) {
		return super.handleComponentClicked(style);
	}

	@Override
	protected void rebuildWidgets() {
		screen.rebuildWidgets();
	}

	@Override
	public List<? extends GuiEventListener> children() {
		return screen.getChildren();
	}

	@Override
	public void tick() {
		screen.tick();
	}

	@Override
	public void removed() {
		screen.removed();
	}

	@Override
	public void added() {
		screen.added();
	}

	@Override
	public void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
		super.renderBackground(guiGraphics, mouseX, mouseY, partialTick);
	}

	@Override
	public boolean isPauseScreen() {
		return screen.isPauseScreen();
	}

	@Override
	protected void repositionElements() {
		screen.repositionElements();
	}

	@Override
	public void resize(Minecraft minecraft, int width, int height) {
		screen.resize(minecraft, width, height);
	}

	@Override
	protected boolean isValidCharacterForName(String text, char charTyped, int cursorPos) {
		return super.isValidCharacterForName(text, charTyped, cursorPos);
	}

	@Override
	public boolean isMouseOver(double mouseX, double mouseY) {
		return screen.isMouseOver(mouseX, mouseY);
	}

	@Override
	public void onFilesDrop(List<Path> packs) {
		super.onFilesDrop(packs);
	}

	@Override
	public void afterMouseMove() {
		super.afterMouseMove();
	}

	@Override
	public void afterMouseAction() {
		super.afterMouseAction();
	}

	@Override
	public void afterKeyboardAction() {
		super.afterKeyboardAction();
	}

	@Override
	public void handleDelayedNarration() {
		super.handleDelayedNarration();
	}

	@Override
	public void triggerImmediateNarration(boolean onlyNarrateNew) {
		super.triggerImmediateNarration(onlyNarrateNew);
	}

	@Override
	protected boolean shouldNarrateNavigation() {
		return super.shouldNarrateNavigation();
	}

	@Override
	protected void updateNarrationState(NarrationElementOutput output) {
		super.updateNarrationState(output);
	}

	@Override
	protected void updateNarratedWidget(NarrationElementOutput narrationElementOutput) {
		super.updateNarratedWidget(narrationElementOutput);
	}

	@Override
	public void setTooltipForNextRenderPass(List<FormattedCharSequence> tooltip) {
		super.setTooltipForNextRenderPass(tooltip);
	}

	@Override
	public void setTooltipForNextRenderPass(List<FormattedCharSequence> tooltip, ClientTooltipPositioner positioner, boolean override) {
		super.setTooltipForNextRenderPass(tooltip, positioner, override);
	}

	@Override
	public void setTooltipForNextRenderPass(Component tooltip) {
		super.setTooltipForNextRenderPass(tooltip);
	}

	@Override
	public void setTooltipForNextRenderPass(Tooltip tooltip, ClientTooltipPositioner positioner, boolean override) {
		super.setTooltipForNextRenderPass(tooltip, positioner, override);
	}

	@Override
	public ScreenRectangle getRectangle() {
		return super.getRectangle();
	}

	@Nullable
	@Override
	public Music getBackgroundMusic() {
		return super.getBackgroundMusic();
	}

	@Nullable
	@Override
	public GuiEventListener getFocused() {
		return super.getFocused();
	}

	@Override
	public void setFocused(@Nullable GuiEventListener focused) {
		super.setFocused(focused);
	}

	@Override
	public Optional<GuiEventListener> getChildAt(double mouseX, double mouseY) {
		return super.getChildAt(mouseX, mouseY);
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		return screen.mouseClicked(mouseX, mouseY, button);
	}

	@Override
	public boolean mouseReleased(double mouseX, double mouseY, int button) {
		return screen.mouseReleased(mouseX, mouseY, button);
	}

	@Override
	public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
		return screen.mouseDragged(mouseX, mouseY, button, dragX, dragY);
	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
		return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
	}

	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		return screen.keyPressed(keyCode, scanCode, modifiers);
	}

	@Override
	public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
		return screen.keyReleased(keyCode, scanCode, modifiers);
	}

	@Override
	public boolean charTyped(char codePoint, int modifiers) {
		return screen.charTyped(codePoint, modifiers);
	}

	@Override
	public void setFocused(boolean focused) {
		super.setFocused(focused);
	}

	@Override
	public boolean isFocused() {
		return super.isFocused();
	}

	@Nullable
	@Override
	public ComponentPath getCurrentFocusPath() {
		return super.getCurrentFocusPath();
	}

	@Nullable
	@Override
	public ComponentPath nextFocusPath(FocusNavigationEvent event) {
		return super.nextFocusPath(event);
	}

	@Override
	public void mouseMoved(double mouseX, double mouseY) {
		screen.mouseMoved(mouseX, mouseY);
	}

	@Override
	public int getTabOrderGroup() {
		return super.getTabOrderGroup();
	}
}