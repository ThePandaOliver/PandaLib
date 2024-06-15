package me.pandamods.pandalib.api.client.screen.elements;

import me.pandamods.pandalib.api.client.screen.PLRenderable;
import me.pandamods.pandalib.api.utils.screen.PLGuiGraphics;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.narration.NarratableEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class UIElementHolder extends AbstractUIElement implements UIElementHolderAccessor, PLRenderable {
	private final List<UIElement> children = new ArrayList<>();
	private final List<NarratableEntry> narratables = new ArrayList<>();
	private final List<UIElementHolder> holders = new ArrayList<>();
	private final List<PLRenderable> renderables = new ArrayList<>();

	@Override
	public void addElement(UIElement element) {
		element.setParent(this);
		element.setScreen(getScreen());
		UIElementHolderAccessor.super.addElement(element);
	}

	@Override
	public List<UIElement> getChildren() {
		return children;
	}

	@Override
	public List<NarratableEntry> getNarratables() {
		return narratables;
	}

	@Override
	public List<PLRenderable> getRenderables() {
		return renderables;
	}

	@Override
	public List<UIElementHolder> getHolders() {
		return holders;
	}

	@Override
	public boolean isMouseOver(double mouseX, double mouseY) {
		if (!isVisible())
			return false;
		if (isOutOfBoundsInteractionAllowed() && this.getElementAt(mouseX, mouseY).isPresent())
			return true;
		return super.isMouseOver(mouseX, mouseY);
	}

	public boolean isOutOfBoundsInteractionAllowed() {
		return true;
	}

	@Override
	public void tick() {
		this.children.forEach(UIElement::tick);
	}

	@Override
	public void render(PLGuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
		if (isVisible()) {
			UIElementHolderAccessor.super.render(guiGraphics, mouseX, mouseY, partialTick);
		}
	}
}