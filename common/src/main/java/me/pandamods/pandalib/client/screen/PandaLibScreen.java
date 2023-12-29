package me.pandamods.pandalib.client.screen;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.platform.Window;
import me.pandamods.pandalib.client.screen.impl.WidgetImpl;
import me.pandamods.pandalib.client.screen.widgets.Widget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class PandaLibScreen extends Screen {

	protected final Screen parentScreen;
	protected final Minecraft minecraft;
	protected final Window window;
	protected final Font font;
	private final List<Widget> widgets = Lists.newArrayList();

	public PandaLibScreen(Screen parentScreen, Component title) {
		super(title);
		this.minecraft = Minecraft.getInstance();
		this.window = this.minecraft.getWindow();
		this.font = this.minecraft.font;
		this.parentScreen = parentScreen;
	}

	@Override
	public void onClose() {
		this.minecraft.setScreen(parentScreen);
	}

	@Override
	protected <T extends GuiEventListener & NarratableEntry> T addWidget(T listener) {
		if (listener instanceof Widget widget)
			widgets.add(widget);
		return super.addWidget(listener);
	}

	@Override
	protected void init() {
		this.widgets.forEach(Widget::init);
	}

	@Override
	protected void clearWidgets() {
		this.widgets.forEach(Widget::clearWidgets);
		this.widgets.clear();
		super.clearWidgets();
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		return super.mouseClicked(mouseX, mouseY, button);
	}
}
