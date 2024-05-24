package me.pandamods.pandalib.api.client.screen;

import me.pandamods.pandalib.api.client.screen.converters.AbstractWidgetConverter;
import me.pandamods.pandalib.api.client.screen.converters.RenderableConverter;
import me.pandamods.pandalib.api.client.screen.elements.UIElement;
import me.pandamods.pandalib.api.client.screen.elements.UIElementHolder;
import me.pandamods.pandalib.api.utils.screen.PLGuiGraphics;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class PLScreen extends Screen {
	private final List<UIElement> uiElements = new ArrayList<>();
	private final List<UIElementHolder> holders = new ArrayList<>();
	private final List<GuiEventListener> eventListeners = new ArrayList<>();
	private final List<PLRenderable> plRenderables = new ArrayList<>();

	protected PLScreen(Component title) {
		super(title);
	}

	protected <T> T addElement(T element) {
		if (element instanceof UIElement uiElement) addComponent(uiElement);
		else {
			if (element instanceof NarratableEntry narratableEntry) {
				ScreenHooks.getNarratables(this).add(narratableEntry);
				this.narratables.add(narratableEntry);
			}

			if (element instanceof GuiEventListener guiEventListener) {
				this.eventListeners.add(guiEventListener);
				this.children.add(guiEventListener);
			}

			if (element instanceof PLRenderable renderable)
				this.plRenderables.add(renderable);
			else if (element instanceof Renderable renderable)
				this.renderables.add(renderable);
		}
		return element;
	}

	protected <T extends UIElement> T addComponent(T uiComponent) {
		uiComponent.setScreen(this);
		uiComponent.setParent(null);
		uiElements.add(uiComponent);
		this.eventListeners.add(uiComponent);
		this.children.add(uiComponent);
		if (uiComponent.isInteractable())
			ScreenHooks.getChildren(this).add(uiComponent);

		if (uiComponent instanceof NarratableEntry narratableEntry) {
			ScreenHooks.getNarratables(this).add(narratableEntry);
			this.narratables.add(narratableEntry);
		}

		if (uiComponent instanceof PLRenderable renderable)
			this.plRenderables.add(renderable);
		else if (uiComponent instanceof Renderable renderable)
			this.renderables.add(renderable);

		if (uiComponent instanceof UIElementHolder componentHolder)
			this.holders.add(componentHolder);
		return uiComponent;
	}

	@Override
	protected <T extends GuiEventListener & NarratableEntry> T addWidget(T listener) {
		this.eventListeners.add(listener);
		return super.addWidget(listener);
	}

	protected void removeWidget(GuiEventListener listener) {
		if (listener instanceof UIElement)
			this.uiElements.remove(listener);

		if (listener instanceof UIElementHolder)
			this.holders.remove(listener);

		if (listener instanceof PLRenderable)
			this.plRenderables.remove(listener);

		this.eventListeners.add(listener);
		super.removeWidget(listener);
	}

	@Override
	protected void clearWidgets() {
		super.clearWidgets();
		this.plRenderables.clear();
		this.eventListeners.clear();
		this.uiElements.clear();
		this.holders.forEach(UIElementHolder::clearElements);
		this.holders.clear();
	}

	@Override
	protected void rebuildWidgets() {
		super.rebuildWidgets();
	}

	@Override
	public final void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
		PLGuiGraphics graphics = new PLGuiGraphics(guiGraphics);
		render(graphics, mouseX, mouseY, partialTick);
	}

	protected void render(PLGuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
		for (Renderable renderable : renderables) {
			renderable.render(guiGraphics, mouseX, mouseY, partialTick);
		}
		for (PLRenderable renderable : plRenderables) {
			renderable.render(guiGraphics, mouseX, mouseY, partialTick);
		}
	}

	@Override
	protected void init() {
		this.holders.forEach(UIElementHolder::init);
	}

	@Override
	public Optional<GuiEventListener> getChildAt(double mouseX, double mouseY) {
		for (GuiEventListener eventListener : this.eventListeners) {
			if (!eventListener.isMouseOver(mouseX, mouseY)) continue;
			return Optional.of(eventListener);
		}
		return Optional.empty();
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		for (GuiEventListener eventListener : this.eventListeners) {
			if (!eventListener.isMouseOver(mouseX, mouseY)) continue;
			this.setFocused(eventListener);
			if (eventListener.mouseClicked(mouseX, mouseY, button)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean mouseReleased(double mouseX, double mouseY, int button) {
		for (GuiEventListener eventListener : this.eventListeners) {
			if (!eventListener.isMouseOver(mouseX, mouseY)) continue;
			if (eventListener.mouseReleased(mouseX, mouseY, button)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
		for (GuiEventListener eventListener : this.eventListeners) {
			if (!eventListener.isMouseOver(mouseX, mouseY)) continue;
			if (eventListener.mouseDragged(mouseX, mouseY, button, dragX, dragY)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
		for (GuiEventListener eventListener : this.eventListeners) {
			if (!eventListener.isMouseOver(mouseX, mouseY)) continue;
			if (eventListener.mouseScrolled(mouseX, mouseY, delta)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void mouseMoved(double mouseX, double mouseY) {
		for (GuiEventListener eventListener : this.eventListeners) {
			eventListener.mouseMoved(mouseX, mouseY);
		}
	}
}