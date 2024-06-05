package me.pandamods.pandalib.api.client.screen.elements.widgets.buttons;

import me.pandamods.pandalib.api.utils.screen.PLGuiGraphics;
import net.minecraft.network.chat.Component;

import java.awt.*;

public class ToggleButton extends AbstractPLButton {
	private boolean state = true;

	public ToggleButton(int x, int y, int size) {
		super(Component.empty());
		this.setPosition(x, y);
		this.setSize(size, size);
	}

	@Override
	public void render(PLGuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
		guiGraphics.fill(minX(), minY(), maxX(), maxY(), -6250336);
		guiGraphics.fill(minX() + 1, minY() + 1, maxX() - 1, maxY() - 1, Color.black.getRGB());
		if (state)
			guiGraphics.fill(minX() + 3, minY() + 3, maxX() - 3, maxY() - 3, Color.white.getRGB());
	}

	@Override
	public void onPress() {
		this.state = !state;
	}

	public void setState(boolean state) {
		this.state = state;
	}

	public boolean getState() {
		return state;
	}
}
