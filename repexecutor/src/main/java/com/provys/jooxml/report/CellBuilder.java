package com.provys.jooxml.report;

import com.provys.jooxml.repexecutor.ReportRegionCell;

import java.util.Optional;
import java.util.Set;
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
    private TemplateCell templateCell;
    private String bindColumn;

    CellBuilder() {
        this.index = null;
        this.templateCell = null;
        this.bindColumn = null;
    }

    CellBuilder(TemplateCell templateCell, RowRegionBuilder region) {
        this.index = new CellCoordinates(templateCell.getRowIndex() - region.getFirstTemplateRow()
                , templateCell.getColumnIndex());
        this.templateCell = templateCell;
        this.bindColumn = null;
    }

    CellBuilder(FieldBind fieldBind, RowRegionBuilder region) {
        this.index = new CellCoordinates(fieldBind.getCellReference().getRow() - region.getFirstTemplateRow()
                , fieldBind.getCellReference().getCol());
        this.templateCell = null;
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
            return index.row;
        }
        return -1;
    }

    CellBuilder combine(CellBuilder cell) {
        if (this.index == null) {
            this.index = cell.index;
        } else if ((cell.index != null) && (!cell.index.equals(cell.index))) {
            throw new IllegalArgumentException("Cannot merge two combined cells with different coordinates");
        }
        if (this.templateCell == null) {
            this.templateCell = cell.templateCell;
        } else if (cell.templateCell != null) {
            throw new IllegalArgumentException("Cannot merge two template cells");
        }
        if (this.bindColumn == null) {
            this.bindColumn = cell.bindColumn;
        } else if (cell.bindColumn != null) {
            throw new IllegalArgumentException("Cannot merge two bind cells");
        }
        return this;
    }

    ReportRegionCell build() {
        if (index == null) {
            throw new IllegalStateException("Cannot build region cell from empty combined cell");
        }
        ReportRegionCell cell;
        if (templateCell != null) {
            return new TemplateCellWithBind(index.column, templateCell, Optional.ofNullable(bindColumn));
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
     * Used to collect cells from stream that might contain TemplateCell-based and FiledBind-based instances of
     * CellBuilder with same coordinates
     */
    static class CellBuilderCollector implements Collector<CellBuilder, CellBuilder
            , ReportRegionCell> {

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
        public Function<CellBuilder, ReportRegionCell> finisher() {
            return CellBuilder::build;
        }

        @Override
        public Set<Characteristics> characteristics() {
            return null;
        }
    }

}
