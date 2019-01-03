package com.provys.report.jooxml.report;

import com.provys.report.jooxml.repexecutor.RowProperties;
import com.provys.report.jooxml.tplworkbook.TplRow;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;

class RowBuilder {

    private int rowIndex = -1;
    private RowProperties rowProperties = null;
    private List<CellBuilder> cells = new LinkedList<>();

    /**
     * Default constructor - leaves everything on defaults, usually used from collectors
     */
    private RowBuilder() {}

    /**
     * Constructor initializing row values from template row
     */
    RowBuilder(TplRow row, RowAreaBuilder region) {
        this.rowIndex = row.getRowIndex() - region.getFirstRow();
        this.rowProperties = row.getRowProperties();
    }

    /**
     * Adds cell to row builder. If row number was not previously initialized, it is taken from cell.
     *
     * @param cell is cell to be added
     * @throws IllegalArgumentException if cell does not belong to given row
     */
    private void addCell(CellBuilder cell) {
        if (cell.getRowIndex() != -1) {
            if (rowIndex == -1) {
                rowIndex = cell.getRowIndex();
            } else if (cell.getRowIndex() != rowIndex) {
                throw new IllegalArgumentException("Supplied cell does not belong to this row");
            }
        }
        cells.add(cell);
    }

    /**
     * Adds cells to row builder. If row number was not previously initialized, it is taken from cell.
     *
     * @param cells is collection of cells to be added
     * @throws IllegalArgumentException if cell does not belong to given row
     */
    private void addCells(Collection<CellBuilder> cells) {
        // we need to check cells one by one...
        for (CellBuilder cell : cells) {
            addCell(cell);
        }
    }

    /**
     * @return cells as read-only collection
     */
    private Collection<CellBuilder> getCells() {
        return Collections.unmodifiableCollection(cells);
    }

    /**
     * Set row index to specified value.
     *
     * @param rowIndex is new value of row index
     * @throws IllegalArgumentException when supplied row index is negative - it is allowed value when new row builder
     * is created, but cannot be set later
     */
    private void setRowIndex(int rowIndex) {
        if (rowIndex<0) {
            throw new IllegalArgumentException("RowImpl index cannot be set to negative value");
        }
        this.rowIndex = rowIndex;
    }

    /**
     * @return row index of this builder; might return -1 if row index has not been initialized yet
     */
    int getRowIndex() {
        return rowIndex;
    }

    private RowProperties getRowProperties() {
        return rowProperties;
    }

    private RowBuilder setRowProperties(RowProperties rowProperties) {
        this.rowProperties = rowProperties;
        return this;
    }

    /**
     * Combines two report region builders. Can be used if parallel processing was used on stream.
     */
    private RowBuilder combine(RowBuilder other) {
        if (other.getRowIndex() != -1) {
            if (getRowIndex() == -1) {
                setRowIndex(other.getRowIndex());
            } else if (other.getRowIndex() != getRowIndex()) {
                throw new IllegalArgumentException("Trying to combine two different rows");
            }
        }
        if (other.getRowProperties() != null) {
            if (getRowProperties() == null) {
                setRowProperties(other.getRowProperties());
            } else if (!getRowProperties().equals(other.getRowProperties())) {
                throw new IllegalArgumentException("Trying to combine two rows with different properties");
            }
        }
        addCells(other.getCells());
        return this;
    }

    /**
     * Builds report region row from this builder
     *
     * @return new report region row
     * @throws IllegalStateException is report region row builder doesn't have valid row coordinate
     */
    private Row build() {
        if (rowIndex < 0) {
            throw new IllegalStateException("Cannot build report region row from builder without valid row coordinate");
        }
        return new RowImpl(getRowIndex(), getRowProperties(), cells.stream().collect(Collectors.groupingBy(
                CellBuilder::getCellIndex, new CellBuilder.CellBuilderCollector())).values());
    }

    /**
     * Collector used to combine cells into report region rows
     */
    static class RowBuilderCollector implements Collector<CellBuilder, RowBuilder, RowBuilder> {

        @Override
        public Supplier<RowBuilder> supplier() {
            return RowBuilder::new;
        }

        @Override
        public BiConsumer<RowBuilder, CellBuilder> accumulator() {
            return RowBuilder::addCell;
        }

        @Override
        public BinaryOperator<RowBuilder> combiner() {
            return RowBuilder::combine;
        }

        @Override
        public Function<RowBuilder, RowBuilder> finisher() {
            return Function.identity();
        }

        @Override
        public Set<Characteristics> characteristics() {
            return new TreeSet<>();
        }
    }

    /**
     * Collector used to combine cells into report region rows
     */
    static class AreaRowCollector implements Collector<RowBuilder, RowBuilder, Row> {

        @Override
        public Supplier<RowBuilder> supplier() {
            return RowBuilder::new;
        }

        @Override
        public BiConsumer<RowBuilder, RowBuilder> accumulator() {
            return RowBuilder::combine;
        }

        @Override
        public BinaryOperator<RowBuilder> combiner() {
            return RowBuilder::combine;
        }

        @Override
        public Function<RowBuilder, Row> finisher() {
            return RowBuilder::build;
        }

        @Override
        public Set<Characteristics> characteristics() {
            return new TreeSet<>();
        }
    }
}
