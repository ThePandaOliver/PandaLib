package me.pandamods.pandalib.client.screen;

import com.mojang.blaze3d.platform.Window;
import me.pandamods.pandalib.client.screen.widgets.Widget;
import me.pandamods.pandalib.client.screen.widgets.WidgetImpl;
import me.pandamods.pandalib.config.ConfigHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public abstract class PandaLibScreen extends Screen implements WidgetImpl {
	private final Screen parent;
	private final Window window;

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
}
