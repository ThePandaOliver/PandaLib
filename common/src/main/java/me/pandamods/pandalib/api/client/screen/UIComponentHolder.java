package me.pandamods.pandalib.api.client.screen;

import me.pandamods.pandalib.api.client.screen.converters.RenderableConverter;
import me.pandamods.pandalib.api.utils.screen.PLGuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public abstract class UIComponentHolder extends AbstractUIComponent implements PLRenderable {
	private final List<GuiEventListener> children = new ArrayList<>();
	private final List<NarratableEntry> narratables = new ArrayList<>();
	private final List<UIComponent> uiComponents = new ArrayList<>();
	private final List<UIComponentHolder> holders = new ArrayList<>();
	private final List<Renderable> renderables = new ArrayList<>();
	private final List<PLRenderable> plRenderables = new ArrayList<>();
	private final List<GuiEventListener> eventListeners = new ArrayList<>();

	protected <T> T addElement(T element) {
		if (element instanceof UIComponent uiComponent) addComponent(uiComponent);
		else {
			if (element instanceof NarratableEntry narratableEntry) {
				ScreenHooks.getNarratables(this.getScreen()).add(narratableEntry);
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

	protected <T extends UIComponent> T addComponent(T uiComponent) {
		uiComponent.setScreen(this.getScreen());
		uiComponent.setParent(this);
		uiComponents.add(uiComponent);
		this.eventListeners.add(uiComponent);
		this.children.add(uiComponent);
		if (uiComponent.isInteractable())
			ScreenHooks.getChildren(this.getScreen()).add(uiComponent);

		if (uiComponent instanceof NarratableEntry narratableEntry) {
			ScreenHooks.getNarratables(this.getScreen()).add(narratableEntry);
			this.narratables.add(narratableEntry);
		}

		if (uiComponent instanceof PLRenderable renderable)
			this.plRenderables.add(renderable);
		else if (uiComponent instanceof Renderable renderable)
			this.renderables.add(renderable);

		if (uiComponent instanceof UIComponentHolder componentHolder)
			this.holders.add(componentHolder);
		return uiComponent;
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
			this.uiComponents.remove(listener);

		if (listener instanceof Renderable)
			this.renderables.remove(listener);

		if (listener instanceof PLRenderable)
			this.plRenderables.remove(listener);

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
		this.plRenderables.clear();
		ScreenHooks.getChildren(this.getScreen()).removeAll(this.children);
		ScreenHooks.getNarratables(this.getScreen()).removeAll(this.narratables);
		this.children.clear();
		this.narratables.clear();
		this.eventListeners.clear();
		this.uiComponents.clear();
		this.holders.forEach(UIComponentHolder::clearWidgets);
		this.holders.clear();
	}

	public List<GuiEventListener> getEventListeners() {
		return eventListeners;
	}

	public List<GuiEventListener> getChildren() {
		return children;
	}

	public List<NarratableEntry> getNarratables() {
		return narratables;
	}

	public List<Renderable> getRenderables() {
		return renderables;
	}

	public List<UIComponent> getUiComponents() {
		return uiComponents;
	}

	public List<UIComponentHolder> getHolders() {
		return holders;
	}

	protected void rebuildWidgets() {
		this.clearWidgets();
		if (Objects.equals(this.getScreen().getFocused(), this))
			this.getScreen().clearFocus();
		this.init();
	}

	@Override
	public boolean isInteractable() {
		return false;
	}

	@Override
	public void render(PLGuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
		mouseX -= this.getX();
		mouseY -= this.getY();
		if (!isActive()) return;
		guiGraphics.pose().pushPose();
		guiGraphics.pose().translate(this.getX(), this.getY(), 1);
		for (Renderable renderable : renderables) {
			renderable.render(guiGraphics, mouseX, mouseY, partialTick);
		}
		for (PLRenderable renderable : plRenderables) {
			renderable.render(guiGraphics, mouseX, mouseY, partialTick);
		}
		guiGraphics.pose().popPose();
	}

	protected void init() {
		this.holders.forEach(UIComponentHolder::init);
	}

	public Optional<GuiEventListener> getChildAt(double mouseX, double mouseY) {
		mouseX -= this.getX();
		mouseY -= this.getY();
		for (GuiEventListener eventListener : this.eventListeners) {
			if (!eventListener.isMouseOver(mouseX, mouseY)) continue;
			return Optional.of(eventListener);
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
			eventListener.mouseMoved(mouseX, mouseY);
		}
	}

	@Override
	public boolean isMouseOver(double mouseX, double mouseY) {
		if (!isActive()) return false;
		if (isInteractable() && super.isMouseOver(mouseX, mouseY))
			return true;
		return this.getChildAt(mouseX, mouseY).isPresent();
	}
}