package me.pandamods.pandalib.api.client.screen.widget.list;

import me.pandamods.pandalib.api.client.screen.elements.UIElementHolder;
import me.pandamods.pandalib.api.client.screen.layouts.PLGridLayout;
import me.pandamods.pandalib.api.utils.screen.PLGuiGraphics;
import net.minecraft.client.gui.layouts.LayoutElement;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class QuickListWidget extends UIElementHolder {
	private final int rows;
	public final LayoutElement[] elements;

	private QuickListWidget(int rows, LayoutElement[] elements) {
		this.rows = rows;
		this.elements = elements;
	}

	@Override
	public void init() {
		PLGridLayout grid = new PLGridLayout();
		PLGridLayout.RowHelper helper = grid.createRowHelper(this.rows);
		for (LayoutElement element : elements) {
			helper.addChild(element);
		}
		grid.quickArrange(this::addElement);
		this.setSize(grid.getWidth(), grid.getHeight());
		super.init();
	}

	@Override
	public void render(PLGuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
		if (this.isActive()) {
			guiGraphics.fill(this.getRelativeX() - 1, this.getRelativeY() - 1,
					this.getRelativeX() + this.width + 1, this.getRelativeY() + this.height + 1, Color.WHITE.getRGB());

			guiGraphics.fill(this.getRelativeX(), this.getRelativeY(),
					this.getRelativeX() + this.width, this.getRelativeY() + this.height, Color.BLACK.getRGB());
		}
		super.render(guiGraphics, mouseX, mouseY, partialTick);
	}

	public static Builder builder(int rows) {
		return new Builder(rows);
	}

	public static class Builder {
		private final int rows;
		private final List<LayoutElement> elements;

		private Builder(int rows) {
			this.rows = rows;
			this.elements = new ArrayList<>();
		}

		public Builder addElement(LayoutElement element) {
			this.elements.add(element);
			return this;
		}

		public Builder addElements(Collection<? extends LayoutElement> elements) {
			this.elements.addAll(elements);
			return this;
		}

		public QuickListWidget build() {
			return new QuickListWidget(rows, elements.toArray(new LayoutElement[0]));
		}
	}
}
