package me.pandamods.pandalib.client.screen.widgets.tab;

import me.pandamods.pandalib.client.screen.widgets.ScrollableWidget;
import me.pandamods.pandalib.client.screen.widgets.WidgetImpl;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.worldselection.CreateWorldScreen;
import org.joml.Math;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractTabWidget<T extends AbstractTab> extends ScrollableWidget {
	private List<T> tabs =  new ArrayList<>();

	public AbstractTabWidget(WidgetImpl parent) {
		super(parent);
	}

	public void addTab(T tab) {
		this.tabs.add(tab);
		rebuildWidgets();
	}

	public void addTabs(List<T> tabs) {
		this.tabs.addAll(tabs);
		rebuildWidgets();
	}

	public void setTabs(List<T> tabs) {
		this.tabs = tabs;
		rebuildWidgets();
	}

	public void removeTab(T tab) {
		this.tabs.remove(tab);
		rebuildWidgets();
	}

	@Override
	public void initWidget() {
		updateTabs();
		for (T tab : tabs) {
			this.addRenderableWidget(tab);
		}
	}

	public List<T> getTabs() {
		return tabs;
	}

	@Override
	protected void updateScroll() {
		super.updateScroll();
		updateTabs();
	}

	public final void updateTabs() {
		for (int i = 0; i < tabs.size(); i++) {
			AbstractTab tab = tabs.get(i);
			tab.prepare(this, getTabWidth() * i + this.getX(), this.getY(), getTabWidth(), getHeight());
		}
	}

	public int getTabWidth() {
		if (!tabs.isEmpty())
			return Math.max(100, this.getWidth() / tabs.size());
		else
			return 0;
	}

	@Override
	public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
		updateTabs();
		guiGraphics.fill(this.getMinX(), this.getMinY(), this.getMaxX(), this.getMaxY(), Color.black.getRGB());
		guiGraphics.blit(CreateWorldScreen.HEADER_SEPERATOR, getX(), getMaxY() - 2,
				0.0F, 0.0F, getWidth(), 2, 32, 2);
		super.renderWidget(guiGraphics, mouseX, mouseY, partialTick);
	}

	@Override
	public int getMaxScrollDistanceX() {
		return getTabWidth() * tabs.size();
	}

	@Override
	public int getMaxScrollDistanceY() {
		return 0;
	}

	@Override
	public void clearWidgets() {
		super.clearWidgets();
	}

	@SuppressWarnings("unchecked")
	protected final void onClick(AbstractTab tab) {
		onTabClick((T) tab);
	}

	public void onTabClick(T tab) {}
}
