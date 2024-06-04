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

@Environment(EnvType.CLIENT)
public class PLGridLayout extends AbstractLayout {
    private final List<LayoutElement> children = new ArrayList<>();
    private final List<CellInhabitant> cellInhabitants = new ArrayList<>();
    private final LayoutSettings defaultCellSettings = LayoutSettings.defaults();
    private int rowSpacing = 0;
    private int columnSpacing = 0;

	private int columns = 0;
	private int rows = 0;

    public PLGridLayout() {
        this(0, 0);
    }

    public PLGridLayout(int x, int y) {
        super(x, y, 0, 0);
    }

    @Override
    public void arrangeElements() {
        int m;
        int l;
        int k;
        super.arrangeElements();
        int i = 0;
        int j = 0;
        for (CellInhabitant cellInhabitant : this.cellInhabitants) {
            i = Math.max(cellInhabitant.getLastOccupiedRow(), i);
            j = Math.max(cellInhabitant.getLastOccupiedColumn(), j);
        }
        int[] is = new int[j + 1];
        int[] js = new int[i + 1];
        for (CellInhabitant cellInhabitant2 : this.cellInhabitants) {
            k = cellInhabitant2.getHeight() - (cellInhabitant2.occupiedRows - 1) * this.rowSpacing;
            Divisor divisor = new Divisor(k, cellInhabitant2.occupiedRows);
            for (l = cellInhabitant2.row; l <= cellInhabitant2.getLastOccupiedRow(); ++l) {
                js[l] = Math.max(js[l], divisor.nextInt());
            }
            l = cellInhabitant2.getWidth() - (cellInhabitant2.occupiedColumns - 1) * this.columnSpacing;
            Divisor divisor2 = new Divisor(l, cellInhabitant2.occupiedColumns);
            for (m = cellInhabitant2.column; m <= cellInhabitant2.getLastOccupiedColumn(); ++m) {
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

    public <T extends LayoutElement> T addChild(T child, int row, int column) {
        return this.addChild(child, row, column, this.newCellSettings());
    }

    public <T extends LayoutElement> T addChild(T child, int row, int column, LayoutSettings layoutSettings) {
        return this.addChild(child, row, column, 1, 1, layoutSettings);
    }

    public <T extends LayoutElement> T addChild(T child, int row, int column, int occupiedRows, int occupiedColumns) {
        return this.addChild(child, row, column, occupiedRows, occupiedColumns, this.newCellSettings());
    }

    public <T extends LayoutElement> T addChild(T child, int row, int column, int occupiedRows, int occupiedColumns, LayoutSettings layoutSettings) {
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

    public PLGridLayout columnSpacing(int columnSpacing) {
        this.columnSpacing = columnSpacing;
        return this;
    }

    public PLGridLayout rowSpacing(int rowSpacing) {
        this.rowSpacing = rowSpacing;
        return this;
    }

    public PLGridLayout spacing(int spacing) {
        return this.columnSpacing(spacing).rowSpacing(spacing);
    }

    @Override
    public void visitChildren(Consumer<LayoutElement> consumer) {
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

	public RowHelper createRowHelper(int columns) {
        return new RowHelper(columns);
    }
	public ColumnHelper createColumnHelper(int rows) {
        return new ColumnHelper(rows);
    }

	public void quickArrange(Consumer<LayoutElement> consumer) {
		quickArrange(consumer, 0, 0, width, height, 0f, 0f);
	}

	public void quickArrange(Consumer<LayoutElement> consumer, int x, int y, int width, int height) {
		quickArrange(consumer, x, y, width, height, 0f, 0f);
	}

	public void quickArrange(Consumer<LayoutElement> consumer, int x, int y, int width, int height, float deltaX, float deltaY) {
		this.visitChildren(consumer);
		this.arrangeElements();
		FrameLayout.alignInRectangle(this, x, y, width, height, deltaX, deltaY);
	}

    @Environment(value=EnvType.CLIENT)
    static class CellInhabitant extends AbstractLayout.AbstractChildWrapper {
        final int row;
        final int column;
        final int occupiedRows;
        final int occupiedColumns;

        CellInhabitant(LayoutElement child, int row, int column, int occupiedRows, int occupiedColumns, LayoutSettings layoutSettings) {
            super(child, layoutSettings.getExposed());
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
    }

    @Environment(value=EnvType.CLIENT)
    public final class RowHelper {
        private final int columns;
        private int index;

        RowHelper(int columns) {
            this.columns = columns;
        }

        public <T extends LayoutElement> T addChild(T child) {
            return this.addChild(child, 1);
        }

        public <T extends LayoutElement> T addChild(T child, int occupiedColumns) {
            return this.addChild(child, occupiedColumns, this.defaultCellSetting());
        }

        public <T extends LayoutElement> T addChild(T child, LayoutSettings layoutSettings) {
            return this.addChild(child, 1, layoutSettings);
        }

        public <T extends LayoutElement> T addChild(T child, int occupiedColumns, LayoutSettings layoutSettings) {
            int i = this.index / this.columns;
            int j = this.index % this.columns;
            if (j + occupiedColumns > this.columns) {
                ++i;
                j = 0;
                this.index = Mth.roundToward(this.index, this.columns);
            }
            this.index += occupiedColumns;
            return PLGridLayout.this.addChild(child, i, j, 1, occupiedColumns, layoutSettings);
        }

        public PLGridLayout getGrid() {
            return PLGridLayout.this;
        }

        public LayoutSettings newCellSettings() {
            return PLGridLayout.this.newCellSettings();
        }

        public LayoutSettings defaultCellSetting() {
            return PLGridLayout.this.defaultCellSetting();
        }
    }

	@Environment(value=EnvType.CLIENT)
	public final class ColumnHelper {
		private final int rows;
		private int index;

		ColumnHelper(int rows) {
			this.rows = rows;
		}

		public <T extends LayoutElement> T addChild(T child) {
			return this.addChild(child, 1);
		}

		public <T extends LayoutElement> T addChild(T child, int occupiedRows) {
			return this.addChild(child, occupiedRows, this.defaultCellSetting());
		}

		public <T extends LayoutElement> T addChild(T child, LayoutSettings layoutSettings) {
			return this.addChild(child, 1, layoutSettings);
		}

		public <T extends LayoutElement> T addChild(T child, int occupiedRows, LayoutSettings layoutSettings) {
			int i = this.index / this.rows;
			int j = this.index % this.rows;
			if (j + occupiedRows > this.rows) {
				++i;
				j = 0;
				this.index = Mth.roundToward(this.index, this.rows);
			}
			this.index += occupiedRows;
			return PLGridLayout.this.addChild(child, j, i, occupiedRows, 1, layoutSettings);
		}

		public PLGridLayout getGrid() {
			return PLGridLayout.this;
		}

		public LayoutSettings newCellSettings() {
			return PLGridLayout.this.newCellSettings();
		}

		public LayoutSettings defaultCellSetting() {
			return PLGridLayout.this.defaultCellSetting();
		}
	}
}