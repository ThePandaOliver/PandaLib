package me.pandamods.pandalib.client.screen.widgets;

import com.mojang.blaze3d.platform.Window;
import me.pandamods.pandalib.client.screen.PandaLibScreen;
import me.pandamods.pandalib.utils.ScreenUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarrationElementOutput;

import java.util.ArrayList;
import java.util.List;

public abstract class Widget implements WidgetImpl, GuiEventListener, NarratableEntry {
	private final List<Widget> widgets = new ArrayList<>();
	private final List<GuiEventListener> children = new ArrayList<>();
	private final List<Renderable> renderables = new ArrayList<>();
	private final List<Renderable> renderablesCull = new ArrayList<>();
	private final WidgetImpl parent;

	private int x = 0;
	private int y = 0;
	private int width = 0;
	private int height = 0;

	private boolean hovered = false;
	private boolean focused = false;
	private boolean dragged = false;
	private boolean active = true;
	private boolean childrenActive = true;
	private boolean initialized = false;

	protected final Minecraft minecraft;
	protected final Window window;
	protected final Font font;
	private final PandaLibScreen screen;

	public Widget(WidgetImpl parent) {
		this.minecraft = Minecraft.getInstance();
		this.window = minecraft.getWindow();
		this.font = minecraft.font;
		this.parent = parent;
		if (parent instanceof PandaLibScreen pandaLibScreen) this.screen = pandaLibScreen;
		else if (parent instanceof Widget widget) this.screen = widget.getScreen();
		else this.screen = null;
	}

	public final void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
		guiGraphics.enableScissor(this.getMinX(), this.getMinY(), this.getMaxX(), this.getMaxY());
		if (this.isVisible()) {
			this.hovered = this.isMouseOver(mouseX, mouseY);
			this.renderWidget(guiGraphics, mouseX, mouseY, partialTick);
			widgets.forEach(widget -> widget.render(guiGraphics, mouseX, mouseY, partialTick));
			renderablesCull.forEach(renderable -> renderable.render(guiGraphics, mouseX, mouseY, partialTick));
		}
		guiGraphics.disableScissor();
	}

	public final void renderOverlay(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
		if (this.isVisible()) {
			renderWidgetOverlay(guiGraphics, mouseX, mouseY, partialTick);
			widgets.forEach(widget -> widget.renderOverlay(guiGraphics, mouseX, mouseY, partialTick));
			renderables.forEach(renderable -> renderable.render(guiGraphics, mouseX, mouseY, partialTick));
		}
	}

	public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {}

	public void renderWidgetOverlay(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {}

	@Override
	public List<Widget> widgets() {
		return widgets;
	}

	public void init() {
		if (isActive()) {
			initWidget();
			this.widgets().forEach(Widget::init);
			initialized = true;
		}
	}

	public void initWidget() {}

	public boolean isInitialized() {
		return initialized;
	}

	protected <T extends Renderable> T addRenderableOnly(T renderable) {
		return addRenderableOnly(renderable, true);
	}

	protected <T extends Renderable> T addRenderableOnly(T renderable, boolean cull) {
		if (cull)
			this.renderablesCull.add(renderable);
		else
			this.renderables.add(renderable);
		return renderable;
	}

	protected <T extends GuiEventListener & Renderable & NarratableEntry> T addRenderableWidget(T widget) {
		return addRenderableWidget(widget, true);
	}

	protected <T extends GuiEventListener & Renderable & NarratableEntry> T addRenderableWidget(T widget, boolean cull) {
		addRenderableOnly(widget, cull);
		return getScreen().addWidget(widget);
	}

	protected <T extends GuiEventListener & NarratableEntry> T addWidget(T listener) {
		children.add(listener);
		return getScreen().addWidget(listener);
	}

	protected Widget addWidget(Widget widget) {
		this.widgets().add(widget);
		addWidget((GuiEventListener & NarratableEntry) widget);
		return widget;
	}

	protected void removeWidget(GuiEventListener listener) {
		getScreen().removeWidget(listener);
		children.remove(listener);
	}

	public void clearWidgets() {
		renderablesCull.clear();
		renderables.clear();
		children.forEach(getScreen()::removeWidget);
		children.clear();
		widgets.forEach(Widget::clearWidgets);
		widgets.clear();
	}

	protected void rebuildWidgets() {
		if (isInitialized()) {
			this.clearWidgets();
			this.init();
		}
	}

	public List<GuiEventListener> children() {
		return children;
	}

	@Override
	public void setFocused(boolean focused) {
		this.focused = focused;
	}

	@Override
	public boolean isFocused() {
		return this.focused;
	}

	public void setActive(boolean active) {
		this.active = active;
		updateActive();
	}

	protected void updateActive() {
		this.rebuildWidgets();
		widgets().forEach(Widget::updateActive);
	}

	@Override
	public boolean isActive() {
		if (parent instanceof Widget widget)
			return active && widget.isChildrenActive() && widget.isActive();
		return active;
	}

	public void setChildrenActive(boolean active) {
		this.active = active;
		updateActive();
	}

	public boolean isChildrenActive() {
		return true;
	}

	@Override
	public NarrationPriority narrationPriority() {
		return NarrationPriority.FOCUSED;
	}

	@Override
	public void updateNarration(NarrationElementOutput narrationElementOutput) {
	}

	@Override
	public void setX(int x) {
		this.x = x;
	}

	@Override
	public int getX() {
		if (this.parent != null)
			return x + parent.getX() + parent.getChildX();
		return x;
	}

	@Override
	public void setY(int y) {
		this.y = y;
	}

	@Override
	public int getY() {
		if (this.parent != null)
			return y + parent.getY() + parent.getChildY();
		return y;
	}

	@Override
	public void setWidth(int width) {
		this.width = width;
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public void setHeight(int height) {
		this.height = height;
	}

	@Override
	public int getHeight() {
		return height;
	}

	public PandaLibScreen getScreen() {
		return this.screen;
	}

	public WidgetImpl getParent() {
		return parent;
	}

	@Override
	public boolean isMouseOver(double mouseX, double mouseY) {
		return ScreenUtils.isMouseOver(mouseX, mouseY, getMinX(), getMinY(), getMaxX(), getMaxY());
	}

	public boolean isHovered() {
		return hovered;
	}

	public boolean isVisible() {
//		return isActive() && getMinX() > 0 && getMinY() > 0 && getMaxX() < window.getWidth() && getMaxY() < window.getHeight();
		return true;
	}

	public boolean isDragged() {
		return dragged;
	}

	public void setDragged(boolean dragged) {
		this.dragged = dragged;
	}

	@Override
	public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
		for (Widget widget : this.widgets()) {
			if (widget.isMouseOver(mouseX, mouseY)) {
				return widget.mouseDragged(mouseX, mouseY, button, dragX, dragY);
			}
		}
		return false;
	}
}
