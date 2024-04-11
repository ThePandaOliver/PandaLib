package me.pandamods.pandalib.api.client.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class PLScreen extends Screen {
	private final List<Element> elements = new ArrayList<>();
	private final List<ElementHolder> holders = new ArrayList<>();
	private final List<GuiEventListener> eventListeners = new ArrayList<>();

	protected PLScreen(Component title) {
		super(title);
	}

	protected <T extends Element> T addElement(T element) {
		element.setScreen(this);
		element.setParent(null);
		elements.add(element);
		this.eventListeners.add(element);
		if (element.isFocusable())
			ScreenHooks.getChildren(this).add(element);

		if (element instanceof NarratableEntry narratableEntry)
			ScreenHooks.getNarratables(this).add(narratableEntry);

		if (element instanceof Renderable renderable)
			ScreenHooks.getRenderables(this).add(renderable);

		if (element instanceof ElementHolder elementHolder)
			this.holders.add(elementHolder);
		return element;
	}

	@Override
	protected <T extends GuiEventListener & NarratableEntry> T addWidget(T listener) {
		this.eventListeners.add(listener);
		return super.addWidget(listener);
	}

	protected void removeWidget(GuiEventListener listener) {
		if (listener instanceof Element)
			this.elements.remove(listener);

		if (listener instanceof ElementHolder)
			this.holders.remove(listener);

		this.eventListeners.add(listener);
		super.removeWidget(listener);
	}

	@Override
	protected void clearWidgets() {
		super.clearWidgets();
		this.eventListeners.clear();
		this.elements.clear();
		this.holders.forEach(ElementHolder::clearWidgets);
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
		this.holders.forEach(ElementHolder::init);
	}

	@Override
	public Optional<GuiEventListener> getChildAt(double mouseX, double mouseY) {
		for (GuiEventListener eventListener : this.eventListeners) {
			if (eventListener.isMouseOver(mouseX, mouseY)) {
				if (eventListener instanceof ElementHolder elementHolder) {
					Optional<GuiEventListener> possibleChild = elementHolder.getChildAt(mouseX, mouseY);
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
			if (eventListener.isMouseOver(mouseX, mouseY)) {
				this.setFocused(eventListener);
				if (eventListener.mouseClicked(mouseX, mouseY, button))
					return true;
			}
		}
		return false;
	}

	@Override
	public boolean mouseReleased(double mouseX, double mouseY, int button) {
		for (GuiEventListener eventListener : this.eventListeners) {
			if (eventListener.isMouseOver(mouseX, mouseY)) {
				if (eventListener.mouseReleased(mouseX, mouseY, button))
					return true;
			}
		}
		return false;
	}

	@Override
	public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
		for (GuiEventListener eventListener : this.eventListeners) {
			if (eventListener.isMouseOver(mouseX, mouseY)) {
				if (eventListener.mouseDragged(mouseX, mouseY, button, dragX, dragY))
					return true;
			}
		}
		return false;
	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
		for (GuiEventListener eventListener : this.eventListeners) {
			if (eventListener.isMouseOver(mouseX, mouseY)) {
				if (eventListener.mouseScrolled(mouseX, mouseY, delta))
					return true;
			}
		}
		return false;
	}

	@Override
	public void mouseMoved(double mouseX, double mouseY) {
		for (GuiEventListener eventListener : this.eventListeners) {
			if (eventListener.isMouseOver(mouseX, mouseY)) {
				eventListener.mouseMoved(mouseX, mouseY);
			}
		}
	}
}