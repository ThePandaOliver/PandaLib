package me.pandamods.pandalib.client.screen.api.widgets.buttons;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class PLButton extends PLAbstractButton {
	public static final int SMALL_WIDTH = 120;
	public static final int DEFAULT_WIDTH = 150;
	public static final int DEFAULT_HEIGHT = 20;
	protected static final Button.CreateNarration DEFAULT_NARRATION = Supplier::get;
	protected final Button.OnPress onPress;
	protected final Button.CreateNarration createNarration;

	public PLButton(int x, int y, int width, int height, Component component, Button.OnPress onPress, Button.CreateNarration createNarration) {
		super(x, y, width, height, component);
		this.onPress = onPress;
		this.createNarration = createNarration;
	}

	@Override
	public void onPress() {

	}

	@Override
	protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
		this.defaultButtonNarrationText(narrationElementOutput);
	}

	@Override
	protected MutableComponent createNarrationMessage() {
		return this.createNarration.createNarrationMessage(super::createNarrationMessage);
	}

	@Environment(value= EnvType.CLIENT)
	public static class Builder {
		private final Component message;
		private final Button.OnPress onPress;
		@Nullable
		private Tooltip tooltip;
		private int x;
		private int y;
		private int width = DEFAULT_WIDTH;
		private int height = DEFAULT_HEIGHT;
		private Button.CreateNarration createNarration = DEFAULT_NARRATION;

		public Builder(Component message, Button.OnPress onPress) {
			this.message = message;
			this.onPress = onPress;
		}

		public PLButton.Builder pos(int x, int y) {
			this.x = x;
			this.y = y;
			return this;
		}

		public PLButton.Builder width(int width) {
			this.width = width;
			return this;
		}

		public PLButton.Builder size(int width, int height) {
			this.width = width;
			this.height = height;
			return this;
		}

		public PLButton.Builder bounds(int x, int y, int width, int height) {
			return this.pos(x, y).size(width, height);
		}

		public PLButton.Builder tooltip(@Nullable Tooltip tooltip) {
			this.tooltip = tooltip;
			return this;
		}

		public PLButton.Builder createNarration(Button.CreateNarration createNarration) {
			this.createNarration = createNarration;
			return this;
		}

		public PLButton build() {
			PLButton button = new PLButton(this.x, this.y, this.width, this.height, this.message, this.onPress, this.createNarration);
			button.setTooltip(this.tooltip);
			return button;
		}
	}
}
