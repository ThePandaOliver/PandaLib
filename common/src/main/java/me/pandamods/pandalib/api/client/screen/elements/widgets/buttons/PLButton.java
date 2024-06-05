package me.pandamods.pandalib.api.client.screen.elements.widgets.buttons;

import java.util.function.Supplier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

@Environment(EnvType.CLIENT)
public class PLButton extends AbstractPLButton {
    public static final int SMALL_WIDTH = 120;
    public static final int DEFAULT_WIDTH = 150;
    public static final int DEFAULT_HEIGHT = 20;
    protected final OnPress onPress;

    public static Builder builder(Component message, OnPress onPress) {
        return new Builder(message, onPress);
    }

    protected PLButton(int x, int y, int width, int height, Component message, OnPress onPress) {
        super(message);
		this.setPosition(x, y);
		this.setSize(width, height);
        this.onPress = onPress;
    }

    public void onPress() {
        this.onPress.onPress(this);
    }

	@Environment(EnvType.CLIENT)
    public static class Builder {
        private final Component message;
        private final OnPress onPress;
        private int x;
        private int y;
        private int width = 150;
        private int height = 20;

        public Builder(Component message, OnPress onPress) {
            this.message = message;
            this.onPress = onPress;
        }

        public Builder pos(int x, int y) {
            this.x = x;
            this.y = y;
            return this;
        }

        public Builder width(int width) {
            this.width = width;
            return this;
        }

        public Builder size(int width, int height) {
            this.width = width;
            this.height = height;
            return this;
        }

        public Builder bounds(int x, int y, int width, int height) {
            return this.pos(x, y).size(width, height);
        }

        public PLButton build() {
            PLButton button = new PLButton(this.x, this.y, this.width, this.height, this.message, this.onPress);
            return button;
        }
    }

    @Environment(EnvType.CLIENT)
    public interface OnPress {
        void onPress(PLButton button);
    }
}
