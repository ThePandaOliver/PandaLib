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

public class CompiledPLScreen extends Screen {
	private final PLScreen screen;

	public CompiledPLScreen(PLScreen screen) {
		super(Component.empty());
		this.screen = screen;
	}

	@Override
	public Component getTitle() {
		return screen.getTitle();
	}
}