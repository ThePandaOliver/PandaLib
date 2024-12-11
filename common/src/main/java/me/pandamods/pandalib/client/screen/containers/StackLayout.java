/*
 * Copyright (C) 2024 Oliver Froberg (The Panda Oliver)
 *
 * This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 * You should have received a copy of the GNU Lesser General Public License
 *  along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package me.pandamods.pandalib.client.screen.containers;

import me.pandamods.pandalib.client.screen.core.UIComponent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class StackLayout extends UIContainer {
	protected Direction direction;
	protected int gapSize = 0;

	private int contentWidth = 0;
	private int contentHeight = 0;

	public static StackLayout createHorizontalLayout() {
		return new StackLayout(Direction.HORIZONTAL);
	}

	public static StackLayout createVerticalLayout() {
		return new StackLayout(Direction.VERTICAL);
	}

	public StackLayout(Direction direction) {
		this.direction = direction;
	}
	
	@Override
	public void updateChildState(UIComponent uiComponent) {
		super.updateChildState(uiComponent);
		align();
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
		this.align();
	}

	public Direction getDirection() {
		return direction;
	}

	public void setGapSize(int gapSize) {
		this.gapSize = gapSize;
	}

	public int getGapSize() {
		return gapSize;
	}

	public void align() {
		int contentLength = 0;
		int contentSize = 0;

		Iterator<UIComponent> iterator = this.children.iterator();
		while (iterator.hasNext()) {
			UIComponent child = iterator.next();
			Consumer<Integer> setPos = this.direction == Direction.HORIZONTAL ? child::setX : child::setY;
			Supplier<Integer> getLength = this.direction == Direction.HORIZONTAL ? child::getWidth : child::getHeight;
			Supplier<Integer> getSize = this.direction == Direction.HORIZONTAL ? child::getHeight : child::getWidth;

			setPos.accept(contentLength);
			contentLength += getLength.get();
			if (iterator.hasNext()) {
				contentLength += gapSize;
			}
			contentSize = Math.max(contentSize, getSize.get());
		}

		switch (this.direction) {
			case HORIZONTAL -> {
				contentWidth = contentLength;
				contentHeight = contentSize;
			}
			case VERTICAL -> {
				contentHeight = contentLength;
				contentWidth = contentSize;
			}
		}
	}

	public int getContentWidth() {
		return contentWidth;
	}

	public int getContentHeight() {
		return contentHeight;
	}

	public enum Direction {
		HORIZONTAL,
		VERTICAL
	}
}
