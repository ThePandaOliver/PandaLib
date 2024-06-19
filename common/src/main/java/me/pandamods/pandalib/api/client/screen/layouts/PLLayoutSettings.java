/*
 * Copyright (C) 2024 Oliver Froberg (The Panda Oliver)
 *
 * This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 * You should have received a copy of the GNU General Public License
 *  along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package me.pandamods.pandalib.api.client.screen.layouts;

public interface PLLayoutSettings {
    PLLayoutSettings padding(int size);
	PLLayoutSettings padding(int horizontal, int vertical);
    PLLayoutSettings padding(int left, int top, int right, int bottom);

    PLLayoutSettings paddingLeft(int size);
    PLLayoutSettings paddingTop(int size);
    PLLayoutSettings paddingRight(int size);
    PLLayoutSettings paddingBottom(int size);

	PLLayoutSettings paddingHorizontal(int size);
	PLLayoutSettings paddingVertical(int size);

    PLLayoutSettings align(float deltaX, float deltaY);
    PLLayoutSettings alignHorizontally(float delta);
    PLLayoutSettings alignVertically(float delta);

    default PLLayoutSettings alignHorizontallyLeft() {
        return this.alignHorizontally(0.0f);
    }
    default PLLayoutSettings alignHorizontallyCenter() {
        return this.alignHorizontally(0.5f);
    }
    default PLLayoutSettings alignHorizontallyRight() {
        return this.alignHorizontally(1.0f);
    }
    default PLLayoutSettings alignVerticallyTop() {
        return this.alignVertically(0.0f);
    }
    default PLLayoutSettings alignVerticallyMiddle() {
        return this.alignVertically(0.5f);
    }
    default PLLayoutSettings alignVerticallyBottom() {
        return this.alignVertically(1.0f);
    }

   PLLayoutSettings copy();

   LayoutSettingsImpl getExposed();

   static PLLayoutSettings defaults() {
	   return new LayoutSettingsImpl();
   }

   class LayoutSettingsImpl implements PLLayoutSettings {
       public int paddingLeft;
       public int paddingTop;
       public int paddingRight;
       public int paddingBottom;
       public float xAlignment;
       public float yAlignment;

	   public LayoutSettingsImpl() {}

       public LayoutSettingsImpl(LayoutSettingsImpl other) {
           this.paddingLeft = other.paddingLeft;
           this.paddingTop = other.paddingTop;
           this.paddingRight = other.paddingRight;
           this.paddingBottom = other.paddingBottom;
           this.xAlignment = other.xAlignment;
           this.yAlignment = other.yAlignment;
       }

	   @Override
	   public LayoutSettingsImpl padding(int padding) {
		   return this.padding(padding, padding);
	   }

	   @Override
	   public LayoutSettingsImpl padding(int horizontalPadding, int verticalPadding) {
		   return this.paddingHorizontal(horizontalPadding).paddingVertical(verticalPadding);
	   }

	   @Override
	   public LayoutSettingsImpl padding(int paddingLeft, int paddingTop, int paddingRight, int paddingBottom) {
		   return this.paddingLeft(paddingLeft).paddingRight(paddingRight).paddingTop(paddingTop).paddingBottom(paddingBottom);
	   }

	   @Override
	   public LayoutSettingsImpl paddingLeft(int paddingLeft) {
		   this.paddingLeft = paddingLeft;
		   return this;
	   }

	   @Override
	   public LayoutSettingsImpl paddingTop(int paddingTop) {
		   this.paddingTop = paddingTop;
		   return this;
	   }

	   @Override
	   public LayoutSettingsImpl paddingRight(int paddingRight) {
		   this.paddingRight = paddingRight;
		   return this;
	   }

	   @Override
	   public LayoutSettingsImpl paddingBottom(int paddingBottom) {
		   this.paddingBottom = paddingBottom;
		   return this;
	   }

	   @Override
	   public LayoutSettingsImpl paddingHorizontal(int horizontalPadding) {
		   return this.paddingLeft(horizontalPadding).paddingRight(horizontalPadding);
	   }

	   @Override
	   public LayoutSettingsImpl paddingVertical(int verticalPadding) {
		   return this.paddingTop(verticalPadding).paddingBottom(verticalPadding);
	   }

	   @Override
	   public LayoutSettingsImpl align(float xAlignment, float yAlignment) {
		   this.xAlignment = xAlignment;
		   this.yAlignment = yAlignment;
		   return this;
	   }

	   @Override
	   public LayoutSettingsImpl alignHorizontally(float xAlignment) {
		   this.xAlignment = xAlignment;
		   return this;
	   }

	   @Override
	   public LayoutSettingsImpl alignVertically(float yAlignment) {
		   this.yAlignment = yAlignment;
		   return this;
	   }

	   @Override
	   public LayoutSettingsImpl copy() {
		   return new LayoutSettingsImpl(this);
	   }

	   @Override
	   public LayoutSettingsImpl getExposed() {
		   return this;
	   }
   }
}