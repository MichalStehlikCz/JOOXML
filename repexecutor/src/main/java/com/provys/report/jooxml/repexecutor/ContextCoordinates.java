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
}
