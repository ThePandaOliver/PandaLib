package me.pandamods.pandalib.api.client.screen.elements.widgets.buttons;

import com.mojang.blaze3d.systems.RenderSystem;
import me.pandamods.pandalib.api.client.screen.PLRenderable;
import me.pandamods.pandalib.api.client.screen.elements.AbstractUIElement;
import me.pandamods.pandalib.api.utils.screen.PLGuiGraphics;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.navigation.CommonInputs;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.sounds.SoundEvents;

@Environment(EnvType.CLIENT)
public abstract class AbstractPLButton extends AbstractUIElement implements PLRenderable, NarratableEntry {
    protected static final int TEXTURE_Y_OFFSET = 46;
    protected static final int TEXTURE_WIDTH = 200;
    protected static final int TEXTURE_HEIGHT = 20;
    protected static final int TEXTURE_BORDER_X = 20;
    protected static final int TEXTURE_BORDER_Y = 4;
    protected static final int TEXT_MARGIN = 2;
	private final Component message;

	public AbstractPLButton(Component message) {
		this.message = message;
	}

    public abstract void onPress();

	public Component getMessage() {
		return message;
	}

	@Override
    public void render(PLGuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
		this.checkHoverState(mouseX, mouseY);
        Minecraft minecraft = Minecraft.getInstance();
        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();
        guiGraphics.blitNineSliced(AbstractWidget.WIDGETS_LOCATION, this.getX(), this.getY(), this.getWidth(), this.getHeight(),
				20, 4, 200, 20, 0, this.getTextureY());
        int i = this.isActive() ? 16777215 : 10526880;
        this.renderString(guiGraphics, minecraft.font, i);
    }

    public void renderString(PLGuiGraphics guiGraphics, Font font, int color) {
		guiGraphics.drawScrollingString(font, getMessage(), minX() + 2, minY(), maxX() - 2, maxY(), color);
    }

    private int getTextureY() {
        int i = 1;
        if (!this.isActive()) {
            i = 0;
        } else if (this.isHoveredOrFocused()) {
            i = 2;
        }

        return 46 + i * 20;
    }

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		if (isActiveAndVisible()) {
			this.playDownSound(Minecraft.getInstance().getSoundManager());
			this.onPress();
		}
		return true;
	}

	@Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (this.isActiveAndVisible()) {
            if (CommonInputs.selected(keyCode)) {
                this.playDownSound(Minecraft.getInstance().getSoundManager());
                this.onPress();
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

	private void playDownSound(SoundManager soundManager) {
		soundManager.play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
	}

	@Override
	public NarrationPriority narrationPriority() {
		if (this.isFocused()) {
			return NarrationPriority.FOCUSED;
		} else {
			return this.isHovered() ? NarrationPriority.HOVERED : NarrationPriority.NONE;
		}
	}

	@Override
	public void updateNarration(NarrationElementOutput narrationElementOutput) {
		narrationElementOutput.add(NarratedElementType.TITLE, AbstractWidget.wrapDefaultNarrationMessage(this.getMessage()));
		if (this.isActive()) {
			if (this.isFocused()) {
				narrationElementOutput.add(NarratedElementType.USAGE, Component.translatable("narration.button.usage.focused"));
			} else {
				narrationElementOutput.add(NarratedElementType.USAGE, Component.translatable("narration.button.usage.hovered"));
			}
		}
	}
}