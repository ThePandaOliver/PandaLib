package me.pandamods.pandalib.api.client.screen.layouts;

import com.mojang.math.Divisor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.layouts.AbstractLayout;
import net.minecraft.client.gui.layouts.FrameLayout;
import net.minecraft.client.gui.layouts.LayoutElement;
import net.minecraft.client.gui.layouts.LayoutSettings;
import net.minecraft.util.Mth;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class PLGrid implements PLLayoutElement2 {
	private int x;
	private int y;
	private int width;
	private int height;

	private final List<PLLayoutElement2> children = new ArrayList<>();
	private final List<CellInhabitant> cellInhabitants = new ArrayList<>();
	private final LayoutSettings defaultCellSettings = LayoutSettings.defaults();
	private int rowSpacing = 0;
	private int columnSpacing = 0;

	private int columns = 0;
	private int rows = 0;

	public PLGrid() {
		this(0, 0);
	}

	public PLGrid(int x, int y) {
		this(x, y, -1, -1);
	}

	public PLGrid(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	@Override
	public void setX(int x) {
		this.x = x;
	}

	@Override
	public void setY(int y) {
		this.y = y;
	}

	@Override
	public int getX() {
		return x;
	}

	@Override
	public int getY() {
		return y;
	}

	@Override
	public void setWidth(int width) {
		this.width = width;
	}

	@Override
	public void setHeight(int height) {
		this.height = height;
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getHeight() {
		return height;
	}

	public void arrangeElements() {
		int m;
		int l;
		int k;
		int i = 0;
		int j = 0;
		for (CellInhabitant cellInhabitant : this.cellInhabitants) {
			i = Math.max(cellInhabitant.getLastOccupiedRow(), i);
			j = Math.max(cellInhabitant.getLastOccupiedColumn(), j);
		}
		int[] is = new int[j + 1];
		int[] js = new int[i + 1];
		for (CellInhabitant cellInhabitant : this.cellInhabitants) {
			k = cellInhabitant.getHeight() - (cellInhabitant.occupiedRows - 1) * this.rowSpacing;
			Divisor divisor = new Divisor(k, cellInhabitant.occupiedRows);
			for (l = cellInhabitant.row; l <= cellInhabitant.getLastOccupiedRow(); ++l) {
				js[l] = Math.max(js[l], divisor.nextInt());
			}
			l = cellInhabitant.getWidth() - (cellInhabitant.occupiedColumns - 1) * this.columnSpacing;
			Divisor divisor2 = new Divisor(l, cellInhabitant.occupiedColumns);
			for (m = cellInhabitant.column; m <= cellInhabitant.getLastOccupiedColumn(); ++m) {
				is[m] = Math.max(is[m], divisor2.nextInt());
			}
		}
		int[] ks = new int[j + 1];
		int[] ls = new int[i + 1];
		ks[0] = 0;
		for (k = 1; k <= j; ++k) {
			ks[k] = ks[k - 1] + is[k - 1] + this.columnSpacing;
		}
		ls[0] = 0;
		for (k = 1; k <= i; ++k) {
			ls[k] = ls[k - 1] + js[k - 1] + this.rowSpacing;
		}
		for (CellInhabitant cellInhabitant3 : this.cellInhabitants) {
			int n;
			l = 0;
			for (n = cellInhabitant3.column; n <= cellInhabitant3.getLastOccupiedColumn(); ++n) {
				l += is[n];
			}
			cellInhabitant3.setX(this.getX() + ks[cellInhabitant3.column], l += this.columnSpacing * (cellInhabitant3.occupiedColumns - 1));
			n = 0;
			for (m = cellInhabitant3.row; m <= cellInhabitant3.getLastOccupiedRow(); ++m) {
				n += js[m];
			}
			cellInhabitant3.setY(this.getY() + ls[cellInhabitant3.row], n += this.rowSpacing * (cellInhabitant3.occupiedRows - 1));
		}
		this.width = ks[j] + is[j];
		this.height = ls[i] + js[i];
	}

	public <T extends PLLayoutElement2> T addChild(T child, int row, int column) {
		return this.addChild(child, row, column, this.newCellSettings());
	}

	public <T extends PLLayoutElement2> T addChild(T child, int row, int column, LayoutSettings layoutSettings) {
		return this.addChild(child, row, column, 1, 1, layoutSettings);
	}

	public <T extends PLLayoutElement2> T addChild(T child, int row, int column, int occupiedRows, int occupiedColumns) {
		return this.addChild(child, row, column, occupiedRows, occupiedColumns, this.newCellSettings());
	}

	public <T extends PLLayoutElement2> T addChild(T child, int row, int column, int occupiedRows, int occupiedColumns, LayoutSettings layoutSettings) {
		if (occupiedRows < 1)
			throw new IllegalArgumentException("Occupied rows must be at least 1");
		if (occupiedColumns < 1)
			throw new IllegalArgumentException("Occupied columns must be at least 1");
		this.cellInhabitants.add(new CellInhabitant(child, row, column, occupiedRows, occupiedColumns, layoutSettings));
		this.children.add(child);

		if (column >  this.columns)
			this.columns = column;
		if (row >  this.rows)
			this.rows = row;
		return child;
	}

	public PLGrid columnSpacing(int columnSpacing) {
		this.columnSpacing = columnSpacing;
		return this;
	}

	public PLGrid rowSpacing(int rowSpacing) {
		this.rowSpacing = rowSpacing;
		return this;
	}

	public PLGrid spacing(int spacing) {
		return this.columnSpacing(spacing).rowSpacing(spacing);
	}

	public void visitChildren(Consumer<PLLayoutElement2> consumer) {
		this.children.forEach(consumer);
	}

	public LayoutSettings newCellSettings() {
		return this.defaultCellSettings.copy();
	}

	public LayoutSettings defaultCellSetting() {
		return this.defaultCellSettings;
	}

	public int getRows() {
		return rows;
	}

	public int getColumns() {
		return columns;
	}

	public void quickArrange(Consumer<PLLayoutElement2> consumer) {
		quickArrange(consumer, 0, 0, width, height, 0f, 0f);
	}

	public void quickArrange(Consumer<PLLayoutElement2> consumer, int x, int y, int width, int height) {
		quickArrange(consumer, x, y, width, height, 0f, 0f);
	}

	public void quickArrange(Consumer<PLLayoutElement2> consumer, int x, int y, int width, int height, float deltaX, float deltaY) {
		this.setPosition(x, y);
		this.visitChildren(consumer);
		this.arrangeElements();
	}

	public void quickArrange(Consumer<PLLayoutElement2> consumer, int x, int y) {
		this.setPosition(x, y);
		this.visitChildren(consumer);
		this.arrangeElements();
	}

	@Environment(value= EnvType.CLIENT)
	static class CellInhabitant {
		public final PLLayoutElement2 child;
		public final LayoutSettings.LayoutSettingsImpl layoutSettings;

		final int row;
		final int column;
		final int occupiedRows;
		final int occupiedColumns;

		CellInhabitant(PLLayoutElement2 child, int row, int column, int occupiedRows, int occupiedColumns, LayoutSettings layoutSettings) {
			this.child = child;
			this.layoutSettings = layoutSettings.getExposed();
			this.row = row;
			this.column = column;
			this.occupiedRows = occupiedRows;
			this.occupiedColumns = occupiedColumns;
		}

		public int getLastOccupiedRow() {
			return this.row + this.occupiedRows - 1;
		}

		public int getLastOccupiedColumn() {
			return this.column + this.occupiedColumns - 1;
		}

		public void setX(int x, int width) {
			float f = this.layoutSettings.paddingLeft;
			float g = width - this.child.getWidth() - this.layoutSettings.paddingRight;
			int i = (int) Mth.lerp(this.layoutSettings.xAlignment, f, g);
			this.child.setX(i + x);
		}

		public void setY(int y, int height) {
			float f = this.layoutSettings.paddingTop;
			float g = height - this.child.getHeight() - this.layoutSettings.paddingBottom;
			int i = Math.round(Mth.lerp(this.layoutSettings.yAlignment, f, g));
			this.child.setY(i + y);
		}

		public int getHeight() {
			return this.child.getHeight() + this.layoutSettings.paddingTop + this.layoutSettings.paddingBottom;
		}

		public int getWidth() {
			return this.child.getWidth() + this.layoutSettings.paddingLeft + this.layoutSettings.paddingRight;
		}
	}
}
