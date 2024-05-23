package me.pandamods.pandalib.api.client.screen.config.option;

import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import me.pandamods.pandalib.PandaLib;
import me.pandamods.pandalib.api.client.screen.UIComponent;
import me.pandamods.pandalib.api.client.screen.UIComponentHolder;
import me.pandamods.pandalib.api.client.screen.widget.IconButton;
import me.pandamods.pandalib.api.utils.PLCommonComponents;
import me.pandamods.pandalib.api.utils.screen.PLGridLayout;
import me.pandamods.pandalib.api.utils.screen.PLGuiGraphics;
import me.pandamods.pandalib.api.utils.screen.WidgetImage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.layouts.SpacerElement;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.awt.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class AbstractConfigOption<T> extends UIComponentHolder {
	WidgetImage SAVE_ICON = new WidgetImage(
			new ResourceLocation(PandaLib.MOD_ID, "textures/gui/icon/save.png"),
			new ResourceLocation(PandaLib.MOD_ID, "textures/gui/icon/save_disabled.png")
	);
	WidgetImage RESET_ICON = new WidgetImage(
			new ResourceLocation(PandaLib.MOD_ID, "textures/gui/icon/reset.png"),
			new ResourceLocation(PandaLib.MOD_ID, "textures/gui/icon/reset_disabled.png")
	);
	WidgetImage UNDO_ICON = new WidgetImage(
			new ResourceLocation(PandaLib.MOD_ID, "textures/gui/icon/undo.png"),
			new ResourceLocation(PandaLib.MOD_ID, "textures/gui/icon/undo_disabled.png")
	);

	public final Component name;

	public final Event<Supplier<T>> onLoadEvent = EventFactory.createLoop();
	public final Event<Consumer<T>> onSaveEvent = EventFactory.createLoop();
	public final Event<Supplier<T>> onResetEvent = EventFactory.createLoop();

	public AbstractConfigOption(Component name) {
		this.name = name;
	}

	@Override
	public int getWidth() {
		return this.getParent().map(UIComponent::getWidth).orElse(0);
	}

	@Override
	public int getHeight() {
		return 24;
	}

	@Override
	public void render(PLGuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
		Font font = Minecraft.getInstance().font;
		guiGraphics.drawString(font, name, this.getX() + 5, this.getY() + (this.getHeight() - font.lineHeight) / 2, 0xFFFFFF);

		super.render(guiGraphics, mouseX, mouseY, partialTick);
	}

	@Override
	public boolean isInteractable() {
		return true;
	}

	protected abstract void setValue(T value);
	protected abstract T getValue();

	public void save() {
		onSaveEvent.invoker().accept(getValue());
	}
	public void load() {
		setValue(onLoadEvent.invoker().get());
	}
	public void reset() {
		setValue(onResetEvent.invoker().get());
	}

	protected void addActionButtons(PLGridLayout grid, int spacing) {
		int column = grid.getColumns();
		grid.addChild(SpacerElement.width(spacing), 0, column + 1);
		grid.addChild(IconButton.builder(PLCommonComponents.SAVE, SAVE_ICON.get(true),
				iconButton -> this.save()).build(), 0, column + 2);
		grid.addChild(IconButton.builder(PLCommonComponents.UNDO, UNDO_ICON.get(true),
				iconButton -> this.load()).build(), 0, column + 3);
		grid.addChild(IconButton.builder(PLCommonComponents.RESET, RESET_ICON.get(true),
				iconButton -> this.reset()).build(), 0, column + 4);
	}
}