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
import java.util.function.Consumer;

public abstract class Widget implements WidgetImpl, GuiEventListener, NarratableEntry {
	private final List<Widget> widgets = new ArrayList<>();
	private final List<GuiEventListener> children = new ArrayList<>();
	private final List<Renderable> renderables = new ArrayList<>();
	private final List<Renderable> renderablesCull = new ArrayList<>();
	private final PandaLibScreen screen;
	private final Widget parent;

	private int x = 0;
	private int y = 0;
	private int width = 0;
	private int height = 0;

	private boolean hovered = false;
	private boolean focused = false;
	private boolean dragged = false;

	protected final Minecraft minecraft;
	protected final Window window;
	protected final Font font;

	public Widget(PandaLibScreen screen, Widget parent) {
		this.minecraft = Minecraft.getInstance();
		this.window = minecraft.getWindow();
		this.font = minecraft.font;
		this.screen = screen;
		this.parent = parent;
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
		this.widgets().forEach(Widget::init);
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
		return screen.addWidget(widget);
	}

	protected <T extends GuiEventListener & NarratableEntry> T addWidget(T listener) {
		children.add(listener);
		return screen.addWidget(listener);
	}

	protected Widget addWidget(Widget widget) {
		this.widgets().add(widget);
		addWidget((GuiEventListener & NarratableEntry) widget);
		return widget;
	}

	public void clearWidgets() {
		renderablesCull.clear();
		renderables.clear();
		children.forEach(screen.children()::remove);
		children.clear();
		widgets.forEach(Widget::clearWidgets);
		widgets.clear();
	}

	protected void rebuildWidgets() {
		this.clearWidgets();
		this.init();
	}

	@Override
	public void setFocused(boolean focused) {
		this.focused = focused;
	}

	@Override
	public boolean isFocused() {
		return this.focused;
	}

	@Override
	public NarrationPriority narrationPriority() {
		return NarrationPriority.FOCUSED;
	}

	@Override
	public void updateNarration(NarrationElementOutput narrationElementOutput) {
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getX() {
		if (this.parent != null)
			return x + parent.getX() + parent.getChildX();
		return x;
	}

	public int getChildX() {
		return 0;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getY() {
		if (this.parent != null)
			return y + parent.getY() + parent.getChildY();
		return y;
	}

	public int getChildY() {
		return 0;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getWidth() {
		return width;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getHeight() {
		return height;
	}

	public final int getMinX() {
		return this.getX();
	}

	public final int getMinY() {
		return this.getY();
	}

	public final int getMaxX() {
		return this.getX() + this.getWidth();
	}

	public final int getMaxY() {
		return this.getY() + this.getHeight();
	}

	public PandaLibScreen getScreen() {
		return screen;
	}

	public Widget getParentWidget() {
		return parent;
	}

	public void setPosition(int x, int y) {
		this.setX(x);
		this.setY(y);
	}

	public void setSize(int width, int height) {
		this.setWidth(width);
		this.setHeight(height);
	}

	public void setBounds(int minX, int minY, int maxX, int maxY) {
		this.setPosition(minX, minY);
		this.setSize(maxX - minX, maxY - minY);
	}

	@Override
	public boolean isMouseOver(double mouseX, double mouseY) {
		return ScreenUtils.isMouseOver(mouseX, mouseY, getMinX(), getMinY(), getMaxX(), getMaxY());
	}

	public boolean isHovered() {
		return hovered;
	}

	public boolean isVisible() {
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
