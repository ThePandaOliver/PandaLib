package me.pandamods.pandalib.api.client.screen.layouts;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.layouts.LayoutElement;
import net.minecraft.client.gui.navigation.ScreenRectangle;

import java.util.function.Consumer;

@Environment(EnvType.CLIENT)
public interface PLLayoutElement extends LayoutElement {
	void setWidth(int width);
	void setHeight(int height);

	default void setSize(int width, int height) {
		this.setWidth(width);
		this.setHeight(height);
	}

	@Override
	default void visitWidgets(Consumer<AbstractWidget> consumer) {}
}