package me.pandamods.pandalib.client.screen;

import com.mojang.blaze3d.Blaze3D;
import me.pandamods.pandalib.client.screen.core.ParentUIComponent;
import me.pandamods.pandalib.client.screen.utils.RenderContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.function.Supplier;

public abstract class BasePLScreen<T extends ParentUIComponent> extends Screen {
	private final Supplier<T> rootComponentSupplier;
	private T rootComponent;

	protected BasePLScreen(Component title, Supplier<T> rootComponentSupplier) {
		super(title);
		this.rootComponentSupplier = rootComponentSupplier;
	}

	protected BasePLScreen(Supplier<T> rootComponentSupplier) {
		this(Component.empty(), rootComponentSupplier);
	}

	protected abstract void build(T rootComponent);

	@Override
	protected void init() {
		if (rootComponent == null) {
			this.rootComponent = rootComponentSupplier.get();
		}
		this.rootComponent.setX(0);
		this.rootComponent.setY(0);
		this.rootComponent.setWidth(this.width);
		this.rootComponent.setHeight(this.height);
		build(this.rootComponent);
	}

	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
		super.render(guiGraphics, mouseX, mouseY, partialTick);
		this.rootComponent.render(new RenderContext(guiGraphics), mouseX, mouseY, partialTick);
	}
}
