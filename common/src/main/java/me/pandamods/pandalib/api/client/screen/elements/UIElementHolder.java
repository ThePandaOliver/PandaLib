package me.pandamods.pandalib.api.client.screen.elements;

import me.pandamods.pandalib.api.client.screen.PLRenderable;
import me.pandamods.pandalib.api.client.screen.ScreenHooks;
import me.pandamods.pandalib.api.client.screen.converters.AbstractWidgetConverter;
import me.pandamods.pandalib.api.client.screen.converters.RenderableConverter;
import me.pandamods.pandalib.api.utils.screen.PLGuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.narration.NarratableEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public abstract class UIElementHolder extends AbstractUIElement implements PLRenderable {
	private final List<UIElement> children = new ArrayList<>();
	private final List<NarratableEntry> narratables = new ArrayList<>();
	private final List<UIElementHolder> holders = new ArrayList<>();
	private final List<PLRenderable> renderables = new ArrayList<>();

	public void init() {}

	@SuppressWarnings("unchecked")
	public <T> T addElement(T element) {
		if (element instanceof AbstractWidget)
			element = (T) new AbstractWidgetConverter((AbstractWidget) element);
		else if (element instanceof Renderable)
			element = (T) new RenderableConverter((Renderable) element);

		if (element instanceof UIElement)
			addElement((UIElement) element);
		return element;
	}

	private void addElement(UIElement element) {
		element.setScreen(this.getScreen());
		element.setParent(this);
		this.children.add(element);
		if (element.isInteractable())
			this.getScreen().getInteractables().add(element);

		if (element instanceof NarratableEntry) {
			this.narratables.add((NarratableEntry) element);
			ScreenHooks.getNarratables(this.getScreen()).add((NarratableEntry) element);
		}

		if (element instanceof UIElementHolder holder) {
			this.holders.add(holder);
			holder.init();
		}

		if (element instanceof PLRenderable)
			this.renderables.add((PLRenderable) element);
	}

	@SuppressWarnings("SuspiciousMethodCalls")
	public void removeElement(Object listener) {
		this.getScreen().getInteractables().remove(listener);
		this.children.remove(listener);
		ScreenHooks.getNarratables(this.getScreen()).remove(listener);
		this.narratables.remove(listener);
		this.renderables.remove(listener);
		this.holders.remove(listener);
	}

	public void clearElements() {
		this.renderables.clear();
		this.getScreen().getInteractables().removeAll(this.children);
		ScreenHooks.getNarratables(this.getScreen()).removeAll(this.narratables);
		this.children.clear();
		this.narratables.clear();
		this.holders.forEach(UIElementHolder::clearElements);
		this.holders.clear();
	}

	public List<UIElement> getChildren() {
		return children;
	}

	public List<NarratableEntry> getNarratables() {
		return narratables;
	}

	public List<PLRenderable> getRenderables() {
		return renderables;
	}

	public List<UIElementHolder> getHolders() {
		return holders;
	}

	public void rebuildWidgets() {
		this.clearElements();
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
		if (!isActive()) return;
		for (PLRenderable renderable : renderables) {
			renderable.render(guiGraphics, mouseX, mouseY, partialTick);
		}
	}

	public Optional<UIElement> getElementAt(double mouseX, double mouseY) {
		for (UIElement element : this.children) {
			if (!element.isMouseOver(mouseX, mouseY)) continue;
			return Optional.of(element);
		}
		return Optional.empty();
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		for (UIElement element : this.children) {
			if (!element.isMouseOver(mouseX, mouseY)) continue;
			this.getScreen().setFocused(element);
			if (element.mouseClicked(mouseX, mouseY, button)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean mouseReleased(double mouseX, double mouseY, int button) {
		for (UIElement element : this.children) {
			if (!element.isMouseOver(mouseX, mouseY)) continue;
			if (element.mouseReleased(mouseX, mouseY, button))
				return true;
		}
		return false;
	}

	@Override
	public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
		for (UIElement element : this.children) {
			if (!element.isMouseOver(mouseX, mouseY)) continue;
			if (element.mouseDragged(mouseX, mouseY, button, dragX, dragY))
				return true;
		}
		return false;
	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
		for (UIElement element : this.children) {
			if (!element.isMouseOver(mouseX, mouseY)) continue;
			if (element.mouseScrolled(mouseX, mouseY, delta))
				return true;
		}
		return false;
	}

	@Override
	public void mouseMoved(double mouseX, double mouseY) {
		for (UIElement element : this.children) {
			element.mouseMoved(mouseX, mouseY);
		}
	}

	@Override
	public boolean isMouseOver(double mouseX, double mouseY) {
		if (!isActive()) return false;
		if (isInteractable() && super.isMouseOver(mouseX, mouseY))
			return true;
		return this.getElementAt(mouseX, mouseY).isPresent();
	}
}