package com.provys.report.jooxml.repexecutor;

import com.provys.report.jooxml.repworkbook.RepSheet;

import java.util.Objects;

public class ContextCoordinates {
    private final RepSheet sheet;
    private int rowIndex;
    private int columnIndex;

    public ContextCoordinates(RepSheet sheet, int rowIndex, int columnIndex) {
        this.sheet = Objects.requireNonNull(sheet);
        this.rowIndex = rowIndex;
        this.columnIndex = columnIndex;
    }

    /**
     * Create new context coordinates as clone of original one. ContextCoordinates object is mutable, thus if we want to
     * keep it private, we have to clone them
     *
     * @param coordinates are source coordinates that are to be cloned
     */
    public ContextCoordinates(ContextCoordinates coordinates) {
        this.sheet = coordinates.getSheet();
        this.rowIndex = coordinates.getRowIndex();
        this.columnIndex = coordinates.getColumnIndex();
    }

    public RepSheet getSheet() {
        return sheet;
    }

    public void incRowBy(int height) {
        rowIndex += height;
    }

    public int getRowIndex() {
        return rowIndex;
    }

    public void incColumnBy(int width) {
        columnIndex += width;
    }

    public int getColumnIndex() {
        return columnIndex;
    }

    @Override
    public String toString() {
        return "ContextCoordinates{" +
                "sheet=" + sheet +
                ", rowIndex=" + rowIndex +
                ", columnIndex=" + columnIndex +
                '}';
    }
}
