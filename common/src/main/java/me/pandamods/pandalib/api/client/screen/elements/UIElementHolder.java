package me.pandamods.pandalib.api.client.screen.elements;

import me.pandamods.pandalib.api.client.screen.PLRenderable;
import me.pandamods.pandalib.api.utils.screen.PLGuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class UIElementHolder extends AbstractUIElement implements PLRenderable {
	private final List<UIElement> children = new ArrayList<>();
	private final List<NarratableEntry> narratables = new ArrayList<>();
	private final List<UIElementHolder> holders = new ArrayList<>();
	private final List<PLRenderable> renderables = new ArrayList<>();

	public void init() {}

	public <T> T addElement(T element) {
		if (element instanceof UIElement)
			addElement((UIElement) element);
		return element;
	}

	public void addElement(UIElement element) {
		element.setParent(this);
		this.children.add(element);

		if (element instanceof NarratableEntry)
			this.narratables.add((NarratableEntry) element);

		if (element instanceof UIElementHolder holder) {
			this.holders.add(holder);
			holder.init();
		}

		if (element instanceof PLRenderable)
			this.renderables.add((PLRenderable) element);
	}

	@SuppressWarnings("SuspiciousMethodCalls")
	public void removeElement(Object listener) {
		this.children.remove(listener);
		this.narratables.remove(listener);
		this.renderables.remove(listener);
		this.holders.remove(listener);
	}

	public void clearElements() {
		this.renderables.clear();
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
		this.init();
	}

	public void repositionElements() {
		this.rebuildWidgets();
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
			element.setFocused();
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
		if (!isVisible()) return false;
		return super.isMouseOver(mouseX, mouseY);
	}

	protected boolean isMouseOverChild(double mouseX, double mouseY) {
    	return this.getElementAt(mouseX, mouseY).isPresent();
	}
}