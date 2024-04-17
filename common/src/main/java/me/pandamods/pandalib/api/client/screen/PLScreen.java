package me.pandamods.pandalib.api.client.screen;

import me.pandamods.pandalib.api.client.screen.component.UIComponent;
import me.pandamods.pandalib.api.client.screen.component.UIComponentHolder;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class PLScreen extends Screen {
	private final List<UIComponent> uiComponents = new ArrayList<>();
	private final List<UIComponentHolder> holders = new ArrayList<>();
	private final List<GuiEventListener> eventListeners = new ArrayList<>();

	protected PLScreen(net.minecraft.network.chat.Component title) {
		super(title);
	}

	protected <T> T addElement(T element) {
		if (element instanceof UIComponent uiComponent) addComponent(uiComponent);
		else {
			if (element instanceof NarratableEntry narratableEntry) {
				ScreenHooks.getNarratables(this).add(narratableEntry);
				this.narratables.add(narratableEntry);
			}

			if (element instanceof GuiEventListener guiEventListener) {
				this.eventListeners.add(guiEventListener);
				this.children.add(guiEventListener);
			}

			if (element instanceof Renderable renderable)
				this.renderables.add(renderable);
		}
		return element;
	}

	protected <T extends UIComponent> T addComponent(T uiComponent) {
		uiComponent.setScreen(this);
		uiComponent.setParent(null);
		uiComponents.add(uiComponent);
		this.eventListeners.add(uiComponent);
		this.children.add(uiComponent);
		if (uiComponent.isFocusable())
			ScreenHooks.getChildren(this).add(uiComponent);

		if (uiComponent instanceof NarratableEntry narratableEntry) {
			ScreenHooks.getNarratables(this).add(narratableEntry);
			this.narratables.add(narratableEntry);
		}

		if (uiComponent instanceof Renderable renderable)
			this.renderables.add(renderable);

		if (uiComponent instanceof UIComponentHolder componentHolder)
			this.holders.add(componentHolder);
		return uiComponent;
	}

	@Override
	protected <T extends GuiEventListener & NarratableEntry> T addWidget(T listener) {
		this.eventListeners.add(listener);
		return super.addWidget(listener);
	}

	protected void removeWidget(GuiEventListener listener) {
		if (listener instanceof UIComponent)
			this.uiComponents.remove(listener);

		if (listener instanceof UIComponentHolder)
			this.holders.remove(listener);

		this.eventListeners.add(listener);
		super.removeWidget(listener);
	}

	@Override
	protected void clearWidgets() {
		super.clearWidgets();
		this.eventListeners.clear();
		this.uiComponents.clear();
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
		return this.getChildAt(mouseX, mouseY)
				.filter(guiEventListener -> guiEventListener.mouseReleased(mouseX, mouseY, button)).isPresent();
	}

	@Override
	public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
		return this.getChildAt(mouseX, mouseY)
				.filter(guiEventListener -> guiEventListener.mouseDragged(mouseX, mouseY, button, dragX, dragY)).isPresent();
	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
		return this.getChildAt(mouseX, mouseY)
				.filter(guiEventListener -> guiEventListener.mouseScrolled(mouseX, mouseY, delta)).isPresent();
	}

	@Override
	public void mouseMoved(double mouseX, double mouseY) {
		for (GuiEventListener eventListener : this.eventListeners) {
			eventListener.mouseMoved(mouseX, mouseY);
		}
	}
}