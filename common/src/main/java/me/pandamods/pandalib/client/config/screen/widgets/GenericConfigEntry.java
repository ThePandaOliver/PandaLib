package me.pandamods.pandalib.client.config.screen.widgets;

import me.pandamods.pandalib.PandaLib;
import me.pandamods.pandalib.client.config.screen.ConfigScreen;
import me.pandamods.pandalib.client.screen.widgets.WidgetImpl;
import me.pandamods.pandalib.client.screen.widgets.button.ButtonIcon;
import me.pandamods.pandalib.utils.PandaLibComponents;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.layouts.FrameLayout;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.layouts.LayoutElement;
import net.minecraft.resources.ResourceLocation;

import java.util.HashSet;
import java.util.Set;

public abstract class GenericConfigEntry extends ConfigEntry {
	public static final ResourceLocation RESET_LOCATION = new ResourceLocation(PandaLib.MOD_ID, "textures/gui/config/reset_icon.png");
	public static final ResourceLocation UNDO_LOCATION = new ResourceLocation(PandaLib.MOD_ID, "textures/gui/config/undo_icon.png");
	private final GridLayout gridLayout;
	private final Set<LayoutElement> elements = new HashSet<>();
	private final ButtonIcon resetButton;
	private final ButtonIcon revertButton;

	public GenericConfigEntry(WidgetImpl parent, Data data) {
		super(parent, data);
		this.gridLayout = new GridLayout();
		this.resetButton = new ButtonIcon(RESET_LOCATION, 0, 0, 16, 16, PandaLibComponents.RESET);
		resetButton.setPressListener(this::reset);
		this.revertButton = new ButtonIcon(UNDO_LOCATION, 0, 0, 16, 16, PandaLibComponents.UNDO);
		revertButton.setPressListener(this::load);

		gridLayout.defaultCellSetting().padding(2);
	}

	protected void addElement(LayoutElement layoutElement) {
		elements.add(layoutElement);
	}

	@Override
	public void initWidget() {
		addElement(gridLayout.columnSpacing(2));
		addElement(revertButton);
		addElement(resetButton);

		gridLayout.visitWidgets(this::addRenderableWidget);
		FrameLayout.alignInRectangle(gridLayout, getX(), getY(), getWidth() - 4, getHeight(), 1f, 0.5f);
		gridLayout.arrangeElements();
		super.initWidget();
	}

	@Override
	public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
		FrameLayout.alignInRectangle(gridLayout, getX(), getY(), getWidth() - 4, getHeight(), 1f, 0.5f);
		gridLayout.arrangeElements();
		super.renderWidget(guiGraphics, mouseX, mouseY, partialTick);

		revertButton.active = canUndo();
		resetButton.active = canReset();
	}


	public abstract boolean canReset();
	public abstract boolean canUndo();

	@Override
	public int getHeight() {
		return 40;
	}
}
