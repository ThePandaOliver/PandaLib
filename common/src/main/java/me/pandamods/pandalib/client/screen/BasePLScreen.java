package me.pandamods.pandalib.client.screen;

import me.pandamods.pandalib.client.screen.core.ParentUIComponent;
import me.pandamods.pandalib.client.screen.utils.RenderContext;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.function.Supplier;

public abstract class BasePLScreen<T extends ParentUIComponent> extends Screen {
	private final Supplier<T> rootComponentSupplier;
	protected T rootComponent;

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
			build(this.rootComponent);
		}

		this.rootComponent.mount(null);
		this.rootComponent.position(0, 0);
		this.rootComponent.size(this.width, this.height);
	}

	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
		super.render(guiGraphics, mouseX, mouseY, partialTick);
		this.rootComponent.render(new RenderContext(guiGraphics), mouseX, mouseY, partialTick);
	}
}
