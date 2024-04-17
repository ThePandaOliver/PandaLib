package me.pandamods.pandalib.api.client.screen.popup;

import me.pandamods.pandalib.api.client.screen.config.ConfigMenu;
import me.pandamods.pandalib.api.utils.PLCommonComponents;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.layouts.FrameLayout;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

import java.util.List;
import java.util.function.Consumer;

public class AlertPopup extends PopupScreen<Screen> {
	private final List<Component> description;
	private final Runnable onAccept;

	public AlertPopup(Screen parent, Component title, List<Component> description, int width, int height, Runnable onAccept) {
		super(parent, title);
		this.description = description;
		this.onAccept = onAccept;
		this.width = width;
		this.height = height;
	}

		@Override
		protected void init() {
			GridLayout grid = new GridLayout();
			grid.spacing(6).defaultCellSetting().alignVerticallyMiddle().alignHorizontallyCenter();

			grid.addChild(Button.builder(PLCommonComponents.ACCEPT, button -> this.onAccept.run())
					.size(100, 20).build(), 0, 0);
			grid.addChild(Button.builder(PLCommonComponents.CANCEL, button -> this.onClose())
					.size(100, 20).build(), 0, 1);

			grid.arrangeElements();
			FrameLayout.alignInRectangle(grid, 0, this.height - 30, this.width, 30, 0.5f, 0.5f);
			grid.visitChildren(this::addElement);
			super.init();
		}
	}