package me.pandamods.pandalib.api.client.screen;

import me.pandamods.pandalib.api.client.screen.widget.AbstractUIComponent;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public abstract class UIComponentHolder extends AbstractUIComponent implements Renderable {
	private final List<GuiEventListener> children = new ArrayList<>();
	private final List<NarratableEntry> narratables = new ArrayList<>();
	private final List<UIComponent> UIComponents = new ArrayList<>();
	private final List<UIComponentHolder> holders = new ArrayList<>();
	private final List<Renderable> renderables = new ArrayList<>();
	private final List<GuiEventListener> eventListeners = new ArrayList<>();

	protected <T> T addElement(T element) {
		if (element instanceof UIComponent UIComponent) {
			UIComponent.setScreen(this.getScreen());
			UIComponent.setParent(this);
			UIComponents.add(UIComponent);
			this.eventListeners.add(UIComponent);
			this.children.add(UIComponent);
			if (UIComponent.isFocusable())
				ScreenHooks.getChildren(this.getScreen()).add(UIComponent);
		}

		if (element instanceof NarratableEntry narratableEntry) {
			ScreenHooks.getNarratables(this.getScreen()).add(narratableEntry);
			this.narratables.add(narratableEntry);
		}

		if (element instanceof Renderable renderable)
			this.renderables.add(renderable);

		if (element instanceof UIComponentHolder componentHolder)
			this.holders.add(componentHolder);
		return element;
	}

	protected <T extends GuiEventListener & Renderable & NarratableEntry> T addRenderableWidget(T widget) {
		this.renderables.add(widget);
		return this.addWidget(widget);
	}

	protected <T extends Renderable> T addRenderableOnly(T renderable) {
		this.renderables.add(renderable);
		return renderable;
	}

	protected <T extends GuiEventListener & NarratableEntry> T addWidget(T listener) {
		this.children.add(listener);
		ScreenHooks.getChildren(this.getScreen()).add(listener);
		this.narratables.add(listener);
		ScreenHooks.getNarratables(this.getScreen()).add(listener);
		this.eventListeners.add(listener);
		return listener;
	}

	protected void removeWidget(GuiEventListener listener) {
		if (listener instanceof UIComponent)
			this.UIComponents.remove(listener);

		if (listener instanceof Renderable)
			this.renderables.remove(listener);

		if (listener instanceof NarratableEntry) {
			ScreenHooks.getNarratables(this.getScreen()).remove(listener);
			this.narratables.remove(listener);
		}

		if (listener instanceof UIComponentHolder)
			this.holders.remove(listener);

		this.eventListeners.add(listener);
		ScreenHooks.getChildren(this.getScreen()).remove(listener);
		this.children.remove(listener);
	}

	protected void clearWidgets() {
		this.renderables.clear();
		ScreenHooks.getChildren(this.getScreen()).removeAll(this.children);
		ScreenHooks.getNarratables(this.getScreen()).removeAll(this.narratables);
		this.children.clear();
		this.narratables.clear();
		this.eventListeners.clear();
		this.UIComponents.clear();
		this.holders.forEach(UIComponentHolder::clearWidgets);
		this.holders.clear();
	}

	protected void rebuildWidgets() {
		this.clearWidgets();
		if (Objects.equals(this.getScreen().getFocused(), this))
			this.getScreen().clearFocus();
		this.init();
	}

	@Override
	public boolean isFocusable() {
		return false;
	}

	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
		guiGraphics.pose().pushPose();
		guiGraphics.pose().translate(this.getX(), this.getY(), 0);
		for (Renderable renderable : renderables) {
			renderable.render(guiGraphics, mouseX - this.getX(), mouseY - this.getY(), partialTick);
		}
		guiGraphics.pose().popPose();
	}

	protected void init() {
		this.holders.forEach(UIComponentHolder::init);
	}

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
		mouseX -= this.getX();
		mouseY -= this.getY();
		for (GuiEventListener eventListener : this.eventListeners) {
			if (!eventListener.isMouseOver(mouseX, mouseY)) continue;
			this.getScreen().setFocused(eventListener);
			if (eventListener.mouseClicked(mouseX, mouseY, button)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean mouseReleased(double mouseX, double mouseY, int button) {
		mouseX -= this.getX();
		mouseY -= this.getY();
		for (GuiEventListener eventListener : this.eventListeners) {
			if (!eventListener.isMouseOver(mouseX, mouseY)) continue;
			if (eventListener.mouseReleased(mouseX, mouseY, button))
				return true;
		}
		return false;
	}

	@Override
	public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
		mouseX -= this.getX();
		mouseY -= this.getY();
		for (GuiEventListener eventListener : this.eventListeners) {
			if (!eventListener.isMouseOver(mouseX, mouseY)) continue;
			if (eventListener.mouseDragged(mouseX, mouseY, button, dragX, dragY))
				return true;
		}
		return false;
	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
		mouseX -= this.getX();
		mouseY -= this.getY();
		for (GuiEventListener eventListener : this.eventListeners) {
			if (!eventListener.isMouseOver(mouseX, mouseY)) continue;
			if (eventListener.mouseScrolled(mouseX, mouseY, delta))
				return true;
		}
		return false;
	}

	@Override
	public void mouseMoved(double mouseX, double mouseY) {
		mouseX -= this.getX();
		mouseY -= this.getY();
		for (GuiEventListener eventListener : this.eventListeners) {
			if (!eventListener.isMouseOver(mouseX, mouseY)) continue;
			eventListener.mouseMoved(mouseX, mouseY);
		}
	}
}
