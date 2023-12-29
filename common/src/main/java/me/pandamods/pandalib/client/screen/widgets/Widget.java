package me.pandamods.pandalib.client.screen.widgets;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.platform.Window;
import me.pandamods.pandalib.client.config.screen.widgets.ConfigOptionWidget;
import me.pandamods.pandalib.client.screen.impl.WidgetImpl;
import me.pandamods.pandalib.utils.ScreenUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ComponentPath;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.AbstractContainerEventHandler;
import net.minecraft.client.gui.components.events.ContainerEventHandler;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2i;
import org.joml.Vector4i;

import java.util.List;

public abstract class Widget implements Renderable, GuiEventListener, NarratableEntry {
	@Nullable
	public final Widget parentWidget;
	public final Screen screen;
	protected final Minecraft minecraft;
	protected final Window window;
	protected final Font font;
	private final List<GuiEventListener> children = Lists.newArrayList();
	private final List<NarratableEntry> narratables = Lists.newArrayList();
	private final List<Renderable> renderables = Lists.newArrayList();
	private final List<Widget> widgets = Lists.newArrayList();

	private int x = 0;
	private int y = 0;
	private int width = 0;
	private int height = 0;

	private boolean hovered = false;
	private boolean focused = false;

	protected Widget(@Nullable Widget parentWidget, Screen screen) {
		this.parentWidget = parentWidget;
		this.screen = screen;
		this.minecraft = Minecraft.getInstance();
		this.window = this.minecraft.getWindow();
		this.font = this.minecraft.font;
	}

	@Override
	public final void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
		guiGraphics.enableScissor(getMinX(), getMinY(), getMaxX(), getMaxY());
		if (this.isVisible()) {
			this.hovered = this.isMouseOver(mouseX, mouseY);
			this.renderWidget(guiGraphics, mouseX, mouseY, partialTick);
		}
		guiGraphics.disableScissor();
		if (this.isVisible()) {
			this.renderWidgetOverlay(guiGraphics, mouseX, mouseY, partialTick);
		}
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

	public final int getLocalMinX() {
		return this.getLocalX();
	}

	public final int getLocalMinY() {
		return this.getLocalY();
	}

	public final int getLocalMaxX() {
		return this.getLocalX() + this.getWidth();
	}

	public final int getLocalMaxY() {
		return this.getLocalY() + this.getHeight();
	}

	public int getX() {
		if (this.parentWidget != null)
			return x + parentWidget.getX() + parentWidget.getChildOffsetX();
		return x;
	}

	public int getLocalX() {
		return x;
	}

	public int getChildOffsetX() {
		return 0;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		if (this.parentWidget != null)
			return y + parentWidget.getY() + parentWidget.getChildOffsetY();
		return y;
	}

	public int getLocalY() {
		return y;
	}

	public int getChildOffsetY() {
		return 0;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
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

	public boolean isVisible() {
		return true;
	}

	public boolean isHovered() {
		return hovered;
	}

	public boolean isMouseOver(double mouseX, double mouseY) {
		return ScreenUtils.isMouseOver(mouseX, mouseY, getMinX(), getMinY(), getMaxX(), getMaxY());
	}

	@Override
	public NarratableEntry.NarrationPriority narrationPriority() {
		if (this.isFocused()) {
			return NarrationPriority.FOCUSED;
		} else {
			return this.hovered ? NarrationPriority.HOVERED : NarrationPriority.NONE;
		}
	}

	@Override
	public void updateNarration(NarrationElementOutput narrationElementOutput) {}

	private void clearFocus() {
		ComponentPath componentPath = this.getCurrentFocusPath();
		if (componentPath != null) {
			componentPath.applyFocus(false);
		}
	}

	protected void rebuildWidgets() {
		this.clearWidgets();
		this.clearFocus();
		this.init();
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
		if (listener instanceof Widget widget)
			widgets.add(widget);
		this.children.add(listener);
		this.narratables.add(listener);
		return listener;
	}

	protected void removeWidget(GuiEventListener listener) {
		if (listener instanceof Renderable) {
			this.renderables.remove((Renderable)listener);
		}

		if (listener instanceof NarratableEntry) {
			this.narratables.remove((NarratableEntry)listener);
		}

		this.children.remove(listener);
	}

	public void clearWidgets() {
		this.widgets.forEach(Widget::clearWidgets);
		this.widgets.clear();
		this.renderables.clear();
		this.children.clear();
		this.narratables.clear();
	}

	@Override
	public void setFocused(@Nullable GuiEventListener focused) {
		screen.setFocused(focused);
		super.setFocused(focused);
	}

	protected void repositionElements() {
		this.rebuildWidgets();
	}

	public void init() {
		this.widgets.forEach(Widget::init);
	}

	public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
		for (Renderable renderable : this.renderables) {
			renderable.render(guiGraphics, mouseX, mouseY, partialTick);
		}
	}

	public void renderWidgetOverlay(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {}

	public List<GuiEventListener> children() {
		return this.children;
	}
}
