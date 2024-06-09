package me.pandamods.pandalib.api.client.screen.elements.widgets.buttons;

import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Consumer;

public class IconButton extends AbstractIconButton {
	private final Consumer<IconButton> onPress;

	IconButton(int x, int y, int size, Component message, WidgetSprites iconSprite, int textureSize, Consumer<IconButton> onPress) {
		super(x, y, size, message, iconSprite, textureSize);
		this.onPress = onPress;
	}

	public static Builder builder(Component message, WidgetSprites iconSprite, Consumer<IconButton> onPress) {
		return new Builder(message, iconSprite, onPress);
	}

	@Override
	public void onPress() {
		this.onPress.accept(this);
	}

	public static class Builder {
		private int x = 0;
		private int y = 0;
		private int size  = 20;
		private final Component message;
		private final WidgetSprites iconSprite;
		private int textureSize = 16;
		private final Consumer<IconButton> onPress;

		Builder(Component message, WidgetSprites iconSprite, Consumer<IconButton> onPress) {
			this.message = message;
			this.iconSprite = iconSprite;
			this.onPress = onPress;
		}

		public Builder setX(int x) {
			this.x = x;
			return this;
		}

		public Builder setY(int y) {
			this.y = y;
			return this;
		}

		public Builder setPosition(int x, int y) {
			this.x = x;
			this.y = y;
			return this;
		}

		public Builder setSize(int size) {
			this.size = size;
			return this;
		}

		public Builder setTextureSize(int textureSize) {
			this.textureSize = textureSize;
			return this;
		}

		public IconButton build() {
			return new IconButton(x, y, size, message, iconSprite, textureSize, onPress);
		}
	}
}
