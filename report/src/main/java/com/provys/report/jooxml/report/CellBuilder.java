package com.provys.report.jooxml.report;

import com.provys.report.jooxml.tplworkbook.TplCell;
import com.provys.report.jooxml.workbook.CellCoordinates;

import javax.annotation.Nonnull;
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
 * Coordinates are transformed to be relative to region on creation. Class is used by {@code RowCellAreaBuilder} to
 * merge cell bindings and template cells
 */
class CellBuilder {

    @Nullable private CellCoordinates coordinates; // coordinates are relative to region
    @Nullable private TplCell tplCell;
    @Nullable private String bindColumn;

    /**
     * Create empty {@code CellBuilder}. It has no coordinates, cell nor bind column set.
     */
    private CellBuilder() {
        this.coordinates = null;
        this.tplCell = null;
        this.bindColumn = null;
    }

    /**
     * Creates {@code CellBuilder} from supplied template cell. Takes coordinates from cell and references cell (but
     * offsets them against start of region), column binding is left empty.
     *
     * @param tplCell is template cell new object will reference
     * @param region is region within which cell binding is defined, coordinates will be relative against this region
     */
    CellBuilder(TplCell tplCell, RowAreaBuilder region) {
        this.coordinates = tplCell.getCoordinates().shiftBy(-region.getFirstRow(), 0);
        this.tplCell = tplCell;
        this.bindColumn = null;
    }

    /**
     * Creates {@code CellBuilder} from supplied cell binding. Takes coordinates from cell binding and references them
     * (but offsets them against start of region), cell is left empty.
     *
     * @param cellBind is cell binding new object will take column name and coordinates from
     * @param region is region within which cell binding is defined, coordinates will be relative against this region
     */
    CellBuilder(CellBind cellBind, RowAreaBuilder region) {
        this.coordinates = cellBind.getCoordinates().shiftBy(-region.getFirstRow(), 0);
        this.tplCell = null;
        this.bindColumn = cellBind.getSourceColumn();
    }

    /**
     * @return row coordinate of cell (zero based, relative to region), empty optional when not set
     */
    @Nonnull
    Optional<Integer> getRowIndex() {
        return (coordinates == null) ? Optional.empty() : Optional.of(coordinates.getRow());
    }

    /**
     * @return column coordinate of cell (zero based, relative to region), empty optional when not set
     */
    @Nonnull
    Optional<Integer> getCellIndex() {
        return (coordinates == null) ? Optional.empty() : Optional.of(coordinates.getCol());
    }

    /**
     * Set coordinates to {@code CellBuilder}.
     *
     * @param coordinates are coordinates to be set; they should be relative to region cell is part of
     */
    void setCoordinates(@Nullable CellCoordinates coordinates) {
        this.coordinates = coordinates;
    }

    /**
     * @return coordinates of this cell builder, empty Optional if coordinates are not specified
     */
    Optional<CellCoordinates> getCoordinates() {
        return Optional.ofNullable(coordinates);
    }

    /**
     * Combine this cell builder with other cell builder.
     * Take coordinates, template cell and source column from the cell builder that has them specified. If both have the
     * same property specified, verify that values are the same
     *
     * @param cell is other cell to be combined with this
     * @return combined cell builder
     * @throws IllegalArgumentException in case that some property is filled in on both cell builders and values are not
     * the same
     */
    @Nonnull
    private CellBuilder combine(CellBuilder cell) {
        if (getCoordinates().isEmpty()) {
            cell.getCoordinates().ifPresent(otherCoord -> this.setCoordinates(otherCoord));
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

    /**
     * Build {@code AreaCell} from this cell builder.
     *
     * @return created area cell instance
     * @throws IllegalStateException in case coordinates are unknown or neither of template cell and source column are
     * filled in
     */
    @Nonnull
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
     * CellBuilder with the same coordinates
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
