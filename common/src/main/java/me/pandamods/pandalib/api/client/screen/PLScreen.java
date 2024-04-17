package me.pandamods.pandalib.api.client.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class PLScreen extends Screen {
	private final List<UIComponent> UIComponents = new ArrayList<>();
	private final List<UIComponentHolder> holders = new ArrayList<>();
	private final List<GuiEventListener> eventListeners = new ArrayList<>();

	protected PLScreen(net.minecraft.network.chat.Component title) {
		super(title);
	}

	protected <T> T addElement(T uiElement) {
		if (uiElement instanceof UIComponent UIComponent) {
			UIComponent.setScreen(this);
			UIComponent.setParent(null);
			UIComponents.add(UIComponent);
			this.eventListeners.add(UIComponent);
			this.children.add(UIComponent);
			if (UIComponent.isFocusable())
				ScreenHooks.getChildren(this).add(UIComponent);
		}

		if (uiElement instanceof NarratableEntry narratableEntry) {
			ScreenHooks.getNarratables(this).add(narratableEntry);
			this.narratables.add(narratableEntry);
		}

		if (uiElement instanceof Renderable renderable)
			this.renderables.add(renderable);

		if (uiElement instanceof UIComponentHolder componentHolder)
			this.holders.add(componentHolder);
		return uiElement;
	}

	@Override
	protected <T extends GuiEventListener & NarratableEntry> T addWidget(T listener) {
		this.eventListeners.add(listener);
		return super.addWidget(listener);
	}

	protected void removeWidget(GuiEventListener listener) {
		if (listener instanceof UIComponent)
			this.UIComponents.remove(listener);

		if (listener instanceof UIComponentHolder)
			this.holders.remove(listener);

		this.eventListeners.add(listener);
		super.removeWidget(listener);
	}

	@Override
	protected void clearWidgets() {
		super.clearWidgets();
		this.eventListeners.clear();
		this.UIComponents.clear();
		this.holders.forEach(UIComponentHolder::clearWidgets);
		this.holders.clear();
	}

	@Override
	protected void rebuildWidgets() {
		super.rebuildWidgets();
	}

	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
		for (Renderable renderable : renderables) {
			renderable.render(guiGraphics, mouseX, mouseY, partialTick);
		}
	}

	@Override
	protected void init() {
		this.holders.forEach(UIComponentHolder::init);
	}

	@Override
	public Optional<GuiEventListener> getChildAt(double mouseX, double mouseY) {
		for (GuiEventListener eventListener : this.eventListeners) {
			if (eventListener.isMouseOver(mouseX, mouseY)) {
				if (eventListener instanceof UIComponentHolder componentHolder) {
					Optional<GuiEventListener> possibleChild = componentHolder.getChildAt(mouseX, mouseY);
					if (possibleChild.isPresent())
						return possibleChild;
				}
				return Optional.of(eventListener);
			}
		}
		return Optional.empty();
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		for (GuiEventListener eventListener : this.eventListeners) {
			if (!eventListener.isMouseOver(mouseX, mouseY)) continue;
			if (eventListener.mouseClicked(mouseX, mouseY, button)) {
				this.setFocused(eventListener);
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean mouseReleased(double mouseX, double mouseY, int button) {
		for (GuiEventListener eventListener : this.eventListeners) {
			if (!eventListener.isMouseOver(mouseX, mouseY)) continue;
			if (eventListener.mouseReleased(mouseX, mouseY, button))
				return true;
		}
		return false;
	}

	@Override
	public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
		for (GuiEventListener eventListener : this.eventListeners) {
			if (!eventListener.isMouseOver(mouseX, mouseY)) continue;
			if (eventListener.mouseDragged(mouseX, mouseY, button, dragX, dragY))
				return true;
		}
		return false;
	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
		for (GuiEventListener eventListener : this.eventListeners) {
			if (!eventListener.isMouseOver(mouseX, mouseY)) continue;
			if (eventListener.mouseScrolled(mouseX, mouseY, delta))
				return true;
		}
		return false;
	}

	@Override
	public void mouseMoved(double mouseX, double mouseY) {
		for (GuiEventListener eventListener : this.eventListeners) {
			if (!eventListener.isMouseOver(mouseX, mouseY)) continue;
			eventListener.mouseMoved(mouseX, mouseY);
		}
	}
}