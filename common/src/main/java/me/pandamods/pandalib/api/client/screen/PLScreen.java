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
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class PLScreen extends Screen {
	private final List<GuiEventListener> interactables = new ArrayList<>();

	private final List<UIElement> children = new ArrayList<>();
	private final List<UIElementHolder> holders = new ArrayList<>();
	private final List<PLRenderable> renderables = new ArrayList<>();

	protected PLScreen(Component title) {
		super(title);
	}

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
		element.setScreen(this);
		element.setParent(null);
		this.children.add(element);
		if (element.isInteractable())
			this.interactables.add(element);

		if (element instanceof NarratableEntry) {
			this.narratables.add((NarratableEntry) element);
		}

		if (element instanceof UIElementHolder)
			this.holders.add((UIElementHolder) element);

		if (element instanceof PLRenderable)
			this.renderables.add((PLRenderable) element);
	}

	@SuppressWarnings("SuspiciousMethodCalls")
	public void removeElement(Object listener) {
		this.interactables.remove(listener);
		this.children.remove(listener);
		this.narratables.remove(listener);
		this.renderables.remove(listener);
		this.holders.remove(listener);
	}

	public void clearElements() {
		this.renderables.clear();
		this.interactables.clear();
		this.children.clear();
		this.narratables.clear();
		this.holders.forEach(UIElementHolder::clearElements);
		this.holders.clear();
	}

	/**
	 * @deprecated
	 * This method will not support all acceptable objects.
	 * Use {@link PLScreen#removeElement(Object)} instead.
	 */
	@Deprecated
	@Override
	protected void removeWidget(GuiEventListener listener) {
		this.removeElement(listener);
	}

	/**
	 * @deprecated
	 * Calls {@link PLScreen#clearElements()}.
	 */
	@Deprecated
	@Override
	protected void clearWidgets() {
		this.clearElements();
	}

	/**
	 * @deprecated
	 * Calls {@link PLScreen#addElement(Object)}.
	 */
	@Deprecated
	@Override
	protected <T extends GuiEventListener & NarratableEntry> T addWidget(T listener) {
		return addElement(listener);
	}

	/**
	 * @deprecated
	 * Calls {@link PLScreen#addElement(Object)}.
	 */
	@Deprecated
	@Override
	protected <T extends Renderable> T addRenderableOnly(T renderable) {
		return addElement(renderable);
	}

	/**
	 * @deprecated
	 * Calls {@link PLScreen#addElement(Object)}.
	 */
	@Deprecated
	@Override
	protected <T extends GuiEventListener & Renderable & NarratableEntry> T addRenderableWidget(T widget) {
		return addElement(widget);
	}

	@Override
	protected void rebuildWidgets() {
		super.rebuildWidgets();
	}

	/**
	 * @deprecated
	 * Returns {@link PLScreen#getInteractables()} instead.
	 */
	@Deprecated
	@Override
	public List<? extends GuiEventListener> children() {
		return this.getInteractables();
	}

	public List<GuiEventListener> getInteractables() {
		return this.interactables;
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

	@Override
	public final void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
		PLGuiGraphics graphics = new PLGuiGraphics(guiGraphics);
		render(graphics, mouseX, mouseY, partialTick);
	}

	protected void render(PLGuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
		for (PLRenderable renderable : renderables) {
			renderable.render(guiGraphics, mouseX, mouseY, partialTick);
		}
	}

	@Override
	protected void init() {
		this.holders.forEach(UIElementHolder::init);
	}

	/**
	 * @deprecated
	 * Returns {@link PLScreen#getElementAt(double, double)} or null if it cant find element.
	 */
	@Deprecated
	@Override
	public Optional<GuiEventListener> getChildAt(double mouseX, double mouseY) {
		return Optional.ofNullable(getElementAt(mouseX, mouseY).orElse(null));
	}

	public Optional<UIElement> getElementAt(double mouseX, double mouseY) {
		for (UIElement element : this.getChildren()) {
			if (!element.isMouseOver(mouseX, mouseY)) continue;
			return Optional.of(element);
		}
		return Optional.empty();
	}

	@Override
	public void setFocused(@Nullable GuiEventListener focused) {
		GuiEventListener lastFocused = ScreenHooks.getFocused(this);
		if (focused != null) {
			focused.setFocused(true);
		}
		this.focused = focused;
		if (lastFocused != null) {
			lastFocused.setFocused(false);
		}
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		for (UIElement element : this.getChildren()) {
			if (!element.isMouseOver(mouseX, mouseY)) continue;
			this.setFocused(element);
			if (element.mouseClicked(mouseX, mouseY, button)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean mouseReleased(double mouseX, double mouseY, int button) {
		for (UIElement element : this.getChildren()) {
			if (!element.isMouseOver(mouseX, mouseY)) continue;
			if (element.mouseReleased(mouseX, mouseY, button))
				return true;
		}
		return false;
	}

	@Override
	public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
		for (UIElement element : this.getChildren()) {
			if (!element.isMouseOver(mouseX, mouseY)) continue;
			if (element.mouseDragged(mouseX, mouseY, button, dragX, dragY))
				return true;
		}
		return false;
	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
		for (UIElement element : this.getChildren()) {
			if (!element.isMouseOver(mouseX, mouseY)) continue;
			if (element.mouseScrolled(mouseX, mouseY, delta))
				return true;
		}
		return false;
	}

	@Override
	public void mouseMoved(double mouseX, double mouseY) {
		for (UIElement element : this.getChildren()) {
			element.mouseMoved(mouseX, mouseY);
		}
	}
}