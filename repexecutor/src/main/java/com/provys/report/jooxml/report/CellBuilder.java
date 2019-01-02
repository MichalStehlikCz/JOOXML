package com.provys.report.jooxml.report;

import com.provys.report.jooxml.tplworkbook.TplCell;

import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

/**
 * Class used to merge template cells and field bind.
 * Coordinates are transformed to be relative to region on creation
 */
class CellBuilder {

    private CellCoordinates index; // might be null when new empty cell builder is created. Coordinates are relative to
                                   // region
    private TplCell tplCell;
    private String bindColumn;

    CellBuilder() {
        this.index = null;
        this.tplCell = null;
        this.bindColumn = null;
    }

    CellBuilder(TplCell tplCell, RowAreaBuilder region) {
        this.index = new CellCoordinates(tplCell.getRowIndex() - region.getFirstRow()
                , tplCell.getColumnIndex());
        this.tplCell = tplCell;
        this.bindColumn = null;
    }

    CellBuilder(FieldBind fieldBind, RowAreaBuilder region) {
        this.index = new CellCoordinates(fieldBind.getCellReference().getRow() - region.getFirstRow()
                , fieldBind.getCellReference().getCol());
        this.tplCell = null;
        this.bindColumn = fieldBind.getSourceColumn();
    }

    int getRowIndex() {
        if (index != null) {
            return index.row;
        }
        return -1;
    }

    int getCellIndex() {
        if (index != null) {
            return index.column;
        }
        return -1;
    }

    CellBuilder combine(CellBuilder cell) {
        if (this.index == null) {
            this.index = cell.index;
        } else if ((cell.index != null) && (!cell.index.equals(cell.index))) {
            throw new IllegalArgumentException("Cannot merge two combined cells with different coordinates");
        }
        if (this.tplCell == null) {
            this.tplCell = cell.tplCell;
        } else if (cell.tplCell != null) {
            throw new IllegalArgumentException("Cannot merge two template cells");
        }
        if (this.bindColumn == null) {
            this.bindColumn = cell.bindColumn;
        } else if (cell.bindColumn != null) {
            throw new IllegalArgumentException("Cannot merge two bind cells");
        }
        return this;
    }

    AreaCell build() {
        if (index == null) {
            throw new IllegalStateException("Cannot build region cell from empty combined cell");
        }
        AreaCell cell;
        if (tplCell != null) {
            return new TemplateCellWithBind(index.column, tplCell, Optional.ofNullable(bindColumn));
        } else if (bindColumn == null) {
            throw new IllegalStateException("Cannot build region cell from empty combined cell");
        }
        return new EmptyCellWithBind(index.column, bindColumn);
    }

    /**
     * Class used to track and merge coordinates in combined cell
     */
    private static class CellCoordinates {
        private final int row;
        private final int column;

        CellCoordinates(int row, int column) {
            if ((row < 0) || (column < 0)) {
                throw new IllegalArgumentException("Cell coordinates cannot be negative");
            }
            this.row = row;
            this.column = column;
        }

        @Override
        public boolean equals(Object other) {
            if (other == null) {
                return false;
            }
            if (other instanceof CellCoordinates) {
                return ((row == ((CellCoordinates) other).row) && (column == ((CellCoordinates) other).column));
            }
            return false;
        }

        @Override
        public int hashCode() {
            return column + 31*row;
        }
    }

    /**
     * Collector used to combine partial cells into ReportRegionCells.
     * Used to collect cells from stream that might contain TplCell-based and FiledBind-based instances of
     * CellBuilder with same coordinates
     */
    static class CellBuilderCollector implements Collector<CellBuilder, CellBuilder
            , AreaCell> {

        @Override
        public Supplier<CellBuilder> supplier() {
            return CellBuilder::new;
        }

        @Override
        public BiConsumer<CellBuilder, CellBuilder> accumulator() {
            return CellBuilder::combine;
        }

        @Override
        public BinaryOperator<CellBuilder> combiner() {
            return CellBuilder::combine;
        }

        @Override
        public Function<CellBuilder, AreaCell> finisher() {
            return CellBuilder::build;
        }

        @Override
        public Set<Characteristics> characteristics() {
            return new TreeSet<>();
        }
    }

}
