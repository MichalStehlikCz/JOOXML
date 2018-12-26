package com.provys.jooxml.report;

import com.provys.jooxml.repexecutor.ReportRegionRow;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class RowBuilder {
    private int rowIndex = -1;
    private List<CellBuilder> cells = new LinkedList<>();

    /**
     * Adds cell to row builder. If row number was not previously initialized, it is taken from cell.
     *
     * @param cell is cell to be added
     * @throws IllegalArgumentException if cell does not belong to given row
     */
    public void addCell(CellBuilder cell) {
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
     * Set row index to specified value.
     *
     * @param rowIndex is new value of row index
     * @throws IllegalArgumentException when supplied row index is negative - it is allowed value when new row builder
     * is created, but cannot be set later
     */
    public void setRowIndex(int rowIndex) {
        if (rowIndex<0) {
            throw new IllegalArgumentException("Row index cannot be set to negative value");
        }
        this.rowIndex = rowIndex;
    }

    /**
     * @return row index of this builder; might return -1 if row index has not been initialized yet
     */
    public int getRowIndex() {
        return rowIndex;
    }

    /**
     * Combines two report region builders. Can be used if parallel processing was used on stream.
     */
    public RowBuilder combine(RowBuilder other) {
        if (other.getRowIndex() != -1) {
            if (rowIndex == -1) {
                setRowIndex(other.getRowIndex());
            } else if (other.getRowIndex() != rowIndex) {
                throw new IllegalArgumentException("Supplied cell does not belong to this row");
            }
        }

        return this;
    }

    /**
     * Builds report region row from this builder
     *
     * @return new report region row
     * @throws IllegalStateException is report region row builder doesn't have valid row coordinate
     */
    public ReportRegionRow build() {
        if (rowIndex < 0) {
            throw new IllegalStateException("Cannot build report region row from builder without valid row coordinate");
        }
        return new Row(rowIndex, cells.stream().collect(Collectors.groupingBy(
                CellBuilder::getCellIndex, new CellBuilder.CellBuilderCollector())).values());
    }

    /**
     * Collector used to combine cells into report region rows
     */
    static class RowBuilderCollector implements Collector<CellBuilder, RowBuilder, ReportRegionRow> {

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
        public Function<RowBuilder, ReportRegionRow> finisher() {
            return RowBuilder::build;
        }

        @Override
        public Set<Characteristics> characteristics() {
            return null;
        }
    }
}
