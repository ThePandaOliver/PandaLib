package me.pandamods.pandalib.client.screen;

import com.mojang.blaze3d.platform.Window;
import com.mojang.datafixers.types.Func;
import me.pandamods.pandalib.client.screen.widgets.Widget;
import me.pandamods.pandalib.client.screen.widgets.WidgetImpl;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class PandaLibScreen extends Screen implements WidgetImpl {
	private final Screen parent;
	public final Window window;

	private final List<Widget> widgets = new ArrayList<>();

	public PandaLibScreen(Screen parent, Component title) {
		super(title);
		this.minecraft = Minecraft.getInstance();
		this.font = this.minecraft.font;
		this.window = this.minecraft.getWindow();
		this.parent = parent;
	}

	@Override
	public void onClose() {
		this.minecraft.setScreen(getParentScreen());
	}

	public Screen getParentScreen() {
		return parent;
	}

	@Override
	public List<Widget> widgets() {
		return widgets;
	}

	@Override
	public void init() {
		this.widgets().forEach(Widget::init);
	}

	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
		this.renderBackground(guiGraphics);
		super.render(guiGraphics, mouseX, mouseY, partialTick);
		this.widgets().forEach(widget -> {
			widget.render(guiGraphics, mouseX, mouseY, partialTick);
			widget.renderOverlay(guiGraphics, mouseX, mouseY, partialTick);
		});
	}

	@Override
	public <T extends Renderable> T addRenderableOnly(T renderable) {
		return super.addRenderableOnly(renderable);
	}

	@Override
	public <T extends GuiEventListener & Renderable & NarratableEntry> T addRenderableWidget(T widget) {
		return super.addRenderableWidget(widget);
	}

	@Override
	public <T extends GuiEventListener & NarratableEntry> T addWidget(T listener) {
		return super.addWidget(listener);
	}

	protected Widget addWidget(Widget widget) {
		this.widgets().add(widget);
		addWidget((GuiEventListener & NarratableEntry) widget);
		return widget;
	}

	@Override
	public void removeWidget(GuiEventListener listener) {
		super.removeWidget(listener);
	}

	@Override
	protected void clearWidgets() {
		widgets.forEach(Widget::clearWidgets);
		widgets.clear();
		super.clearWidgets();
	}

	protected void setScreen(Screen screen) {
		minecraft.setScreen(screen);
	}

	@Override
	public final List<? extends GuiEventListener> children() {
		return super.children();
	}

	@Override
	public final void setX(int x) {}

	@Override
	public final int getX() {
		return 0;
	}

	@Override
	public final void setY(int y) {}

	@Override
	public final int getY() {
		return 0;
	}

	@Override
	public final void setWidth(int width) {}

	@Override
	public final int getWidth() {
		return this.width;
	}

	@Override
	public final void setHeight(int height) {}

	@Override
	public final int getHeight() {
		return this.height;
	}
}
