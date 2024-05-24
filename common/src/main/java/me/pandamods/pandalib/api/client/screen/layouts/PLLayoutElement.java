package me.pandamods.pandalib.api.client.screen.layouts;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.navigation.ScreenRectangle;

@Environment(EnvType.CLIENT)
public interface PLLayoutElement {
    void setX(int x);
    void setY(int y);
	void setWidth(int width);
	void setHeight(int height);

    int getX();
    int getY();
    int getWidth();
    int getHeight();

    default ScreenRectangle getRectangle() {
        return new ScreenRectangle(this.getX(), this.getY(), this.getWidth(), this.getHeight());
    }

    default void setPos(int x, int y) {
        this.setX(x);
        this.setY(y);
    }

	default void setSize(int width, int height) {
		this.setWidth(width);
		this.setHeight(height);
	}
}