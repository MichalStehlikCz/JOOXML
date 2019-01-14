package com.provys.report.jooxml.report;

import com.provys.report.jooxml.tplworkbook.TplCell;
import com.provys.report.jooxml.workbook.CellCoordinates;

import javax.annotation.Nullable;
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

    @Nullable private CellCoordinates coordinates; // coordinates are relative to region
    @Nullable private TplCell tplCell;
    @Nullable private String bindColumn;

    private CellBuilder() {
        this.coordinates = null;
        this.tplCell = null;
        this.bindColumn = null;
    }

    CellBuilder(TplCell tplCell, RowAreaBuilder region) {
        this.coordinates = CellCoordinates.of(tplCell.getRowIndex() - region.getFirstRow()
                , tplCell.getColIndex());
        this.tplCell = tplCell;
        this.bindColumn = null;
    }

    CellBuilder(CellBind cellBind, RowAreaBuilder region) {
        this.coordinates = cellBind.getCoordinates().shiftBy(-region.getFirstRow(), 0);
        this.tplCell = null;
        this.bindColumn = cellBind.getSourceColumn();
    }

    Optional<Integer> getRowIndex() {
        return (coordinates == null) ? Optional.empty() : Optional.of(coordinates.getRow());
    }

    Optional<Integer> getCellIndex() {
        return (coordinates == null) ? Optional.empty() : Optional.of(coordinates.getCol());
    }

    void setCoordinates(@Nullable CellCoordinates coordinates) {
        this.coordinates = coordinates;
    }

    Optional<CellCoordinates> getCoordinates() {
        return Optional.ofNullable(coordinates);
    }

    private CellBuilder combine(CellBuilder cell) {
        if (getCoordinates().isEmpty()) {
            setCoordinates(cell.getCoordinates().orElse(null));
        } else {
            if ((cell.getCoordinates().isPresent()) && (!getCoordinates().equals(cell.getCoordinates()))) {
                throw new IllegalArgumentException("Cannot merge two combined cells with different coordinates");
            }
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

    private AreaCell build() {
        if (getCellIndex().isEmpty()) {
            throw new IllegalStateException("Cannot build region cell from empty combined cell");
        }
        if (tplCell != null) {
            return new TemplateCellWithBind(getCellIndex().get(), tplCell, Optional.ofNullable(bindColumn));
        } else if (bindColumn == null) {
            throw new IllegalStateException("Cannot build region cell from empty combined cell");
        }
        return new EmptyCellWithBind(getCellIndex().get(), bindColumn);
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
